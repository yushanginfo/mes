/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
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
package com.yushanginfo.erp.order.admin.web.action.contract

import java.time.Instant

import com.yushanginfo.erp.base.model.{Department, Factory, Technic, User}
import com.yushanginfo.erp.order.model.{DepartAssess, OrderStatus, OrderType, SalesOrder}
import com.yushanginfo.erp.order.service.OrderService
import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.security.Securities
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction


class DepartAssessAction extends RestfulAction[DepartAssess] {

  var orderService: OrderService = _

  override protected def indexSetting(): Unit = {
    put("orderTypes", entityDao.getAll(classOf[OrderType]))
  }

  override def search(): View = {
    val builder = OqlBuilder.from(classOf[SalesOrder], "salesOrder")
    populateConditions(builder)
    get(Order.OrderStr) foreach { orderClause =>
      builder.orderBy(orderClause)
    }
    builder.tailOrder("salesOrder.id")
    builder.limit(getPageLimit)
    val salesOrders = entityDao.search(builder)
    put("salesOrders", salesOrders)
    forward()
  }

  override def editSetting(entity: DepartAssess): Unit = {
    put("factories", entityDao.getAll(classOf[Factory]))
    get("technicId").foreach(technicId => {
      put("technic", entityDao.get(classOf[Technic], technicId.toInt))
    })
    get("salesOrderId").foreach(salesOrderId => {
      val order = entityDao.get(classOf[SalesOrder], salesOrderId.toLong)
      put("salesOrder", order)
      if (null == entity.factory) {
        entity.factory = order.factory
      }
    })
    super.editSetting(entity)
  }

  def assess(): View = {
    put("factories", entityDao.getAll(classOf[Factory]))
    put("depart", entityDao.get(classOf[Department], intId("depart")))
    val order = entityDao.get(classOf[SalesOrder], longId("salesOrder"))
    if (order.status == OrderStatus.Passed) {
      forward(to(classOf[SalesOrderAction], "info", "id=" + order.id))
    } else {
      put("salesOrder", order)
      forward()
    }
  }

  def saveAssess(): View = {
    val depart = entityDao.get(classOf[Department], intId("depart"))
    val order = entityDao.get(classOf[SalesOrder], longId("salesOrder"))
    val technicSet = order.technicScheme.technics.map(_.technic).toSet
    val removedAssess = order.assesses.filter { x => !technicSet.contains(x.technic) }
    order.assesses.subtractAll(removedAssess)
    val assessMap = order.assesses.map(x => (x.technic, x)).toMap
    val users = entityDao.findBy(classOf[User], "code", List(Securities.user))
    order.technicScheme.technics foreach { pt =>
      if (pt.technic.depart == depart) {
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
    get("salesOrderId").foreach(salesOrderId => {
      val salesOrder = entityDao.get(classOf[SalesOrder], salesOrderId.toLong)
      entity.salesOrder = salesOrder
    })
    val users = entityDao.findBy(classOf[User], "code", List(Securities.user))
    entity.passed = true
    entity.assessedBy = users.headOption
    entityDao.saveOrUpdate(entity)

    entityDao.refresh(entity)
    orderService.recalcState(entity.salesOrder)
    super.saveAndRedirect(entity)
  }

}
