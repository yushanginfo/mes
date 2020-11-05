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

import com.yushanginfo.erp.base.model.{Factory, Technic}
import com.yushanginfo.erp.order.model.{DepartAssess, OrderStatus, OrderType, SalesOrder}
import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction

class DepartAssessAction extends RestfulAction[DepartAssess] {

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
    //put("departAssessMap", getDepartAssessMap)
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

  override def saveAndRedirect(entity: DepartAssess): View = {
    get("technicId").foreach(technicId => {
      val technic = entityDao.get(classOf[Technic], technicId.toInt)
      entity.technic = technic
    })
    get("salesOrderId").foreach(salesOrderId => {
      val salesOrder = entityDao.get(classOf[SalesOrder], salesOrderId.toLong)
      entity.salesOrder = salesOrder
    })
    entity.passed = true
    entityDao.saveOrUpdate(entity)

    //判断是否所有的工艺都已经评审
    entityDao.refresh(entity)

    val order = entity.salesOrder
    val notComplete =
      order.technicScheme.technics.exists { technic =>
        val passed = order.assesses.exists { assess =>
          assess.technic == technic && assess.passed
        }
        !passed
      }

    //计算计划完工时间
    if (entity.salesOrder.materialDate.nonEmpty && !notComplete && entity.salesOrder.scheduledOn.isEmpty) {
      entity.salesOrder.scheduledOn = entity.salesOrder.materialDate
      var scheduledOn = entity.salesOrder.materialDate.get
      entity.salesOrder.assesses.foreach(assess =>
        scheduledOn = scheduledOn.plusDays(assess.days.toLong)
      )
      entity.salesOrder.scheduledOn = Some(scheduledOn)
      if (entity.salesOrder.scheduledOn.get.compareTo(entity.salesOrder.requireOn) > 0) {
        entity.salesOrder.status = OrderStatus.Unpassed
      } else {
        entity.salesOrder.status = OrderStatus.Passed
      }
    }
    super.saveAndRedirect(entity)
  }

}
