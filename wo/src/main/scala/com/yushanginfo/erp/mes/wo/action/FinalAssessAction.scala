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

import com.yushanginfo.erp.mes.model._
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction

class FinalAssessAction extends RestfulAction[WorkOrder] {
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
          query.param(s"customerCode${i}", b+"%")
          s"workOrder.product.specification like :customerCode${i}"
        }
        println(codeQueryStr.mkString(" or "))
        query.where(codeQueryStr.mkString(" or "))
      }
    }
    query
  }

  def review(): View = {
    val ids = longIds("workOrder")
    val workOrders = entityDao.find(classOf[WorkOrder], ids)
    workOrders.foreach(workOrder => {
      workOrder.scheduledOn = null
      workOrder.assessStatus = AssessStatus.Review
    })
    val wts = entityDao.findBy(classOf[WorkOrderTechnic], "workOrder", workOrders)
    wts.foreach(departAssess => {
      departAssess.passed = Some(false)
    })
    entityDao.saveOrUpdate(wts)
    redirect("search", "info.save.success")
  }

  def cancel(): View = {
    val ids = longIds("workOrder")
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

}
