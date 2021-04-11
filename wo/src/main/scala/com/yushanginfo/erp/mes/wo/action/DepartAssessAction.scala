/*
 * Agile Enterprice Resource Planning Solution.
 *
 * Copyright © 2020, The YushangInfo Software.
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
package com.yushanginfo.erp.mes.wo.action

import com.yushanginfo.erp.base.model.{Factory, User}
import com.yushanginfo.erp.mes.model._
import com.yushanginfo.erp.mes.service.OrderService
import org.beangle.commons.collection.Order
import org.beangle.commons.web.util.RequestUtils
import org.beangle.data.dao.OqlBuilder
import org.beangle.security.Securities
import org.beangle.webmvc.api.annotation.{mapping, param}
import org.beangle.webmvc.api.context.ActionContext
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction

import java.time.{Instant, ZoneId}

class DepartAssessAction extends RestfulAction[WorkOrder] {

  var orderService: OrderService = _

  override protected def indexSetting(): Unit = {
    put("orderTypes", entityDao.getAll(classOf[WorkOrderType]))
    put("orderStatuses", entityDao.getAll(classOf[WorkOrderStatus]))
  }

  override def search(): View = {
    val members = entityDao.findBy(classOf[AssessMember], "user.code", List(Securities.user))
    val myGroups = members.map(_.group).toSet
    val myFactories = members.map(_.factory).toSet
    put("myGroups", myGroups)

    val builder = OqlBuilder.from(classOf[WorkOrder], "workOrder")
    populateConditions(builder)
    get(Order.OrderStr) foreach { orderClause =>
      builder.orderBy(orderClause)
    }
    if (myGroups.isEmpty || myFactories.isEmpty) {
      builder.where("workOrder.id < 0")
    } else {
      builder.where("exists(from workOrder.technics wt where wt.technic.assessGroup in(:groups) and wt.factory in(:factories))", myGroups, myFactories)
      builder.tailOrder("workOrder.id")
      builder.limit(getPageLimit)
    }
    //builder.where("workOrder.materialAssess is not null")
    getDate("assessStartOn") foreach { assessStartOn =>
      val tomorrow = assessStartOn.plusDays(1)
      val s = assessStartOn.atTime(0, 0).atZone(ZoneId.systemDefault()).toInstant
      val e = tomorrow.atTime(0, 0).atZone(ZoneId.systemDefault()).toInstant
      builder.where("workOrder.materialAssess.createdAt between :s and :e", s, e)
    }
    getInt("assessDuration") foreach { assessDuration =>
      assessDuration match {
        case 12 =>
          builder.where("workOrder.assessStatus not in(:endStatuses) and :time<= workOrder.materialAssess.createdAt",
            Array(AssessStatus.Passed, AssessStatus.Cancel), Instant.now().minusSeconds(12 * 2600))
        case 24 =>
          builder.where("workOrder.assessStatus not in(:endStatuses) and :time<= workOrder.materialAssess.createdAt",
            Array(AssessStatus.Passed, AssessStatus.Cancel), Instant.now().minusSeconds(24 * 2600))
        case _ =>
          builder.where("workOrder.assessStatus not in(:endStatuses) and :time > workOrder.materialAssess.createdAt",
            Array(AssessStatus.Passed, AssessStatus.Cancel), Instant.now().minusSeconds(24 * 2600))
      }
    }
    val workOrders = entityDao.search(builder)
    put("workOrders", workOrders)
    forward()
  }

  def assess(): View = {
    put("factories", entityDao.getAll(classOf[Factory]))
    put("assessGroup", entityDao.get(classOf[AssessGroup], longId("assessGroup")))
    val order = entityDao.get(classOf[WorkOrder], longId("workOrder"))
    if (order.assessStatus == AssessStatus.Passed) {
      forward(to(classOf[WorkOrderAction], "info", "id=" + order.id))
    } else {
      put("workOrder", order)
      forward()
    }
  }

  def saveAssess(): View = {
    val group = entityDao.get(classOf[AssessGroup], longId("assessGroup"))
    val order = entityDao.get(classOf[WorkOrder], longId("workOrder"))
    val originStatus = order.assessStatus;
    val users = entityDao.findBy(classOf[User], "code", List(Securities.user))
    order.technics foreach { wt =>
      if (wt.technic.assessGroup.contains(group)) {
        val days = getInt(wt.id + ".days")
        if (days.isDefined) {
          wt.updatedAt = Instant.now
          wt.passed = Some(true)
          wt.days = days
          wt.factory = entityDao.get(classOf[Factory], getInt(wt.id + ".factory.id", 0))
          wt.assessedBy = users.headOption
          entityDao.saveOrUpdate(wt)
        }
      } else {
        if (wt.technic.duration > 0 && wt.days.isEmpty) {
          wt.days = Some(wt.technic.duration)
          wt.updatedAt = Instant.now
          wt.passed = Some(true)
          wt.factory = order.factory
          wt.assessedBy = None
        }
      }
    }

    entityDao.saveOrUpdate(order)
    orderService.recalcState(order,users.head, RequestUtils.getIpAddr(ActionContext.current.request))
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
    forward()
  }
}
