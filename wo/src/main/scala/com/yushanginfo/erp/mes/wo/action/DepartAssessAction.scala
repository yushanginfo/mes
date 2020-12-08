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


class DepartAssessAction extends RestfulAction[DepartAssess] {

  var orderService: OrderService = _

  override protected def indexSetting(): Unit = {
    put("orderTypes", entityDao.getAll(classOf[SalesOrderType]))
  }

  override def search(): View = {
    val builder = OqlBuilder.from(classOf[WorkOrder], "workOrder")
    populateConditions(builder)
    get(Order.OrderStr) foreach { orderClause =>
      builder.orderBy(orderClause)
    }
    builder.tailOrder("workOrder.id")
    builder.limit(getPageLimit)
    val workOrders = entityDao.search(builder)
    put("workOrders", workOrders)
    forward()
  }

  override def editSetting(entity: DepartAssess): Unit = {
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
    put("assessGroup", entityDao.get(classOf[AssessGroup], longId("group")))
    val order = entityDao.get(classOf[WorkOrder], longId("workOrder"))
    if (order.status == AssessStatus.Passed) {
      forward(to(classOf[WorkOrderAction], "info", "id=" + order.id))
    } else {
      put("workOrder", order)
      forward()
    }
  }

  def saveAssess(): View = {
    val group = entityDao.get(classOf[AssessGroup], longId("group"))
    val order = entityDao.get(classOf[WorkOrder], longId("workOrder"))
    val technicSet = order.technicScheme.technics.map(_.technic).toSet
    val removedAssess = order.assesses.filter { x => !technicSet.contains(x.technic) }
    order.assesses.subtractAll(removedAssess)
    val assessMap = order.assesses.map(x => (x.technic, x)).toMap
    val users = entityDao.findBy(classOf[User], "code", List(Securities.user))
    order.technicScheme.technics foreach { pt =>
      if (pt.technic.assessGroup.contains(group)) {
        val assess = assessMap.get(pt.technic) match {
          case Some(assess) => assess
          case None => val assess = new DepartAssess(order, pt.technic)
            order.assesses.addOne(assess)
            assess
        }
        assess.updatedAt = Instant.now
        assess.passed = true
        assess.days = getInt("technic_" + pt.technic.id + ".days", 0)
        assess.factory = entityDao.get(classOf[Factory], getInt("technic_" + pt.technic.id + ".factory.id", 0))
        assess.assessedBy = users.headOption
        entityDao.saveOrUpdate(assess)
      }
    }
    entityDao.saveOrUpdate(order)
    orderService.recalcState(order)
    redirect("search", "info.save.success")
  }

  override def saveAndRedirect(entity: DepartAssess): View = {
    get("technicId").foreach(technicId => {
      val technic = entityDao.get(classOf[Technic], technicId.toInt)
      entity.technic = technic
    })
    get("workOrderId").foreach(workOrderId => {
      val workOrder = entityDao.get(classOf[WorkOrder], workOrderId.toLong)
      entity.workOrder = workOrder
    })
    val users = entityDao.findBy(classOf[User], "code", List(Securities.user))
    entity.passed = true
    entity.assessedBy = users.headOption
    entityDao.saveOrUpdate(entity)

    entityDao.refresh(entity)
    orderService.recalcState(entity.workOrder)
    super.saveAndRedirect(entity)
  }

}
