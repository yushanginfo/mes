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

import com.yushanginfo.erp.mes.model.{AssessStatus, DepartAssess, WorkOrder, WorkOrderStatus, WorkOrderType}
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction

class FinalAssessAction extends RestfulAction[WorkOrder] {
  override protected def indexSetting(): Unit = {
    put("orderTypes", entityDao.getAll(classOf[WorkOrderType]))
    put("orderStatuses", entityDao.getAll(classOf[WorkOrderStatus]))
  }

  override def search(): View = {
    super.search()
  }

  override def info(id: String): View = {
    super.info(id)
  }

  def review(): View = {
    val ids = longIds("workOrder")
    val workOrders = entityDao.find(classOf[WorkOrder], ids)
    workOrders.foreach(workOrder => {
      workOrder.scheduledOn = null
    })
    val departAssesses = entityDao.findBy(classOf[DepartAssess], "workOrder", workOrders)
    departAssesses.foreach(departAssess => {
      departAssess.passed = false
    })
    entityDao.saveOrUpdate(departAssesses)
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
