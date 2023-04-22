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

import net.yushanginfo.erp.base.model.{Factory, User}
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

import java.time.{Instant, LocalDate}

/**
 * 经理复审
 */
class ReviewAssessAction extends RestfulAction[WorkOrder] {
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
        println(codeQueryStr.mkString(" or "))
        query.where(codeQueryStr.mkString(" or "))
      }
    }
    getInt("reviewRound") foreach { round =>
      query.where("size(workOrder.reviewEvents) = :round", round)
    }
    query
  }

  def review(): View = {
    val id = getLongId("workOrder")
    val order = entityDao.get(classOf[WorkOrder], id)
    if (order.assessStatus == AssessStatus.Review) {
      put("workOrder", order)
      order.materialAssess foreach { ma =>
        if (ma.ready) {
          put("materialReadyOn", LocalDate.now().plusDays(1))
        } else {
          put("materialReadyOn", ma.readyOn)
        }
      }
      put("factories", entityDao.getAll(classOf[Factory]))
      put("ems", Ems)
      forward()
    } else {
      redirect("search", "只能操作复审中的工单")
    }

  }

  def saveReview(): View = {
    val order = entityDao.get(classOf[WorkOrder], getLongId("workOrder"))
    val me = entityDao.findBy(classOf[User], "code", List(Securities.user)).headOption
    getDate("materialAssess.readyOn") foreach { readyOn =>
      order.materialAssess foreach { ma =>
        if (!ma.ready && ma.readyOn.get != readyOn) {
          ma.readyOn = Some(readyOn)
          ma.assessedBy = me
          ma.updatedAt = Instant.now
          entityDao.saveOrUpdate(ma)
        }
      }
    }

    order.technics foreach { wt =>
      val days = getInt(s"${wt.id}.days")
      if (days.isDefined) {
        wt.passed = Some(true)
        val nf = entityDao.get(classOf[Factory], getInt(s"${wt.id}.factory.id", 0))
        if (wt.days.getOrElse(0) != days.get || nf != wt.factory) {
          wt.updatedAt = Instant.now
          wt.days = days
          wt.factory = nf
          wt.assessedBy = me
          entityDao.saveOrUpdate(wt)
        }
      }
    }
    order.updateAssessBeginAt(Instant.now())
    entityDao.saveOrUpdate(order)
    orderService.recalcState(order, me.get, RequestUtils.getIpAddr(ActionContext.current.request))
    if (order.assessStatus == AssessStatus.Passed) {
      redirect("search", "复审通过")
    } else {
      redirect("search", "复审后工单状态：" + order.assessStatus.name)
    }
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
