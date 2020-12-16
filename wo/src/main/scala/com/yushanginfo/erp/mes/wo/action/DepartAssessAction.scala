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

import java.time.Instant

import com.yushanginfo.erp.base.model.{Factory, User}
import com.yushanginfo.erp.mes.model._
import com.yushanginfo.erp.order.service.OrderService
import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.security.Securities
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction

class DepartAssessAction extends RestfulAction[WorkOrderTechnic] {

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
    val workOrders = entityDao.search(builder)
    put("workOrders", workOrders)
    forward()
  }

  override def editSetting(entity: WorkOrderTechnic): Unit = {
    put("factories", entityDao.getAll(classOf[Factory]))
    get("technicId").foreach(technicId => {
      put("technic", entityDao.get(classOf[Technic], technicId.toInt))
    })
    get("workOrderId").foreach(workOrderId => {
      val order = entityDao.get(classOf[WorkOrder], workOrderId.toLong)
      put("workOrder", order)
      if (null == entity.factory) {
        entity.factory = order.factory
      }
    })
    super.editSetting(entity)
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
    val assessMap = order.technics.map(x => (x.technic, x)).toMap
    val users = entityDao.findBy(classOf[User], "code", List(Securities.user))
    order.technics foreach { wt =>
      if (wt.technic.assessGroup.contains(group)) {
        val assess = assessMap.get(wt.technic).orNull
        assess.updatedAt = Instant.now
        assess.passed = Some(true)
        assess.days = getInt(wt.id + ".days")
        assess.factory = entityDao.get(classOf[Factory], getInt(wt.id + ".factory.id", 0))
        assess.assessedBy = users.headOption
        entityDao.saveOrUpdate(assess)
      }
    }
    entityDao.saveOrUpdate(order)
    orderService.recalcState(order)
    redirect("search", "info.save.success")
  }

  override def saveAndRedirect(entity: WorkOrderTechnic): View = {
    get("technicId").foreach(technicId => {
      val technic = entityDao.get(classOf[Technic], technicId.toInt)
      entity.technic = technic
    })
    get("workOrderId").foreach(workOrderId => {
      val workOrder = entityDao.get(classOf[WorkOrder], workOrderId.toLong)
      entity.workOrder = workOrder
    })
    val users = entityDao.findBy(classOf[User], "code", List(Securities.user))
    entity.passed = Some(true)
    entity.assessedBy = users.headOption
    entityDao.saveOrUpdate(entity)

    entityDao.refresh(entity)
    orderService.recalcState(entity.workOrder)
    super.saveAndRedirect(entity)
  }

}
