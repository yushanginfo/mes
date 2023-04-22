/*
 * Copyright (C) 2020, The YushangInfo Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.yushanginfo.erp.mes.wo.action

import net.yushanginfo.erp.base.model.User
import net.yushanginfo.erp.mes.model._
import net.yushanginfo.erp.mes.service.OrderService
import org.beangle.commons.lang.Strings
import org.beangle.web.servlet.util.RequestUtils
import org.beangle.data.dao.OqlBuilder
import org.beangle.ems.app.Ems
import org.beangle.security.Securities
import org.beangle.web.action.annotation.{mapping, param}
import org.beangle.web.action.context.ActionContext
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.RestfulAction
import org.beangle.webmvc.support.helper.QueryHelper

import java.time.Instant

class SaleAssessAction extends RestfulAction[WorkOrder] {
  var orderService: OrderService = _

  override protected def indexSetting(): Unit = {
    put("orderTypes", entityDao.getAll(classOf[WorkOrderType]))
    put("orderStatuses", entityDao.getAll(classOf[WorkOrderStatus]))
  }

  protected override def getQueryBuilder: OqlBuilder[WorkOrder] = {
    val query = super.getQueryBuilder
    get("customerCode") foreach { a =>
      if (Strings.isNotEmpty(a)) {
        var i = 0
        var str = Strings.replace(a, "，", ",");
        str = Strings.replace(str, ";", ",");
        val codeQueryStr = Strings.split(str, ",") map { b =>
          i += 1
          query.param(s"customerCode${i}", b + "%")
          s"workOrder.product.specification like :customerCode${i}"
        }
        query.where(codeQueryStr.mkString(" or "))
      }
    }
    QueryHelper.dateBetween(query, null, "createdAt", "createdOn", "createdOn")
    getInt("reviewRound") foreach { round =>
      query.where("size(workOrder.reviewEvents) = :round", round)
    }
    query
  }

  def review(): View = {
    val id = getLongId("workOrder")
    val order = entityDao.get(classOf[WorkOrder], id)
    val query = OqlBuilder.from(classOf[Reviewer].getName, "r")
    query.where(":factory in elements (r.factories)", order.factory)
    query.where(":roundIdx in elements (r.rounds)", order.reviewEvents.size + 1)
    query.select("distinct r.user")
    put("watchers", entityDao.search(query))
    put("workOrder", order)
    put("ems", Ems)
    forward()
  }

  def accept(): View = {
    val id = getLongId("workOrder")
    val order = entityDao.get(classOf[WorkOrder], id)
    val users = entityDao.findBy(classOf[User], "code", List(Securities.user))
    if (order.assessStatus == AssessStatus.Unpassed) {
      order.assessStatus = AssessStatus.Passed
      order.remark = Some("业务接受评审交期，直接通过")
      val log = new AssessLog(AssessStatus.Unpassed, order, users.head, RequestUtils.getIpAddr(ActionContext.current.request))
      entityDao.saveOrUpdate(log, order)
      redirect("search", "info.action.success")
    } else {
      redirect("search", order.assessStatus.name + " 中的工单不能直接通过")
    }

  }

  def issueReview(): View = {
    val id = getLongId("workOrder")
    val order = entityDao.get(classOf[WorkOrder], id)
    val users = entityDao.findBy(classOf[User], "code", List(Securities.user))
    val event = populateEntity(classOf[ReviewEvent], "reviewEvent")
    event.workOrder = order
    event.issueBy = users.head
    event.updatedAt = Instant.now
    val watcherIds = getLongIds("watcher")
    if (watcherIds.nonEmpty) {
      event.watchers ++= entityDao.find(classOf[User], watcherIds)
    }
    orderService.issueReview(order, event, users.head, RequestUtils.getIpAddr(ActionContext.current.request))
    entityDao.saveOrUpdate(event, order)

    redirect("search", "info.save.success")
  }

  def cancel(): View = {
    val ids = getLongIds("workOrder")
    val workOrders = entityDao.find(classOf[WorkOrder], ids)
    workOrders.foreach(workOrder => {
      workOrder.assessStatus = AssessStatus.Cancel
      get("remark").foreach(remark => {
        workOrder.remark = Option(remark)
      })
    })
    entityDao.saveOrUpdate(workOrders)
    redirect("search", "info.save.success")
  }

  @mapping(value = "{id}")
  override def info(@param("id") id: String): View = {
    val order = entityDao.get(classOf[WorkOrder], id.toLong)
    val logQuery = OqlBuilder.from(classOf[AssessLog], "al").where("al.orderId=:orderId", order.id)
    logQuery.orderBy("al.updatedAt")
    val logs = entityDao.search(logQuery)
    put("logs", logs)
    put("workOrder", order)

    val recQuery= OqlBuilder.from(classOf[AssessRecord],"r")
    recQuery.where("r.order=:order",order)
    recQuery.orderBy("r.updatedAt")
    put("assessRecords",entityDao.search(recQuery))
    forward()
  }
}
