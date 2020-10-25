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

import com.yushanginfo.erp.base.model.Technic
import com.yushanginfo.erp.order.model.{DepartAssess, OrderStatus, SalesOrder}
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction

class FinalAssessAction extends RestfulAction[SalesOrder] {


	override def search(): View = {
		put("departAssessMap", getDepartAssessMap)
		super.search()
	}

	override def info(id: String): View = {
		put("departAssessMap", getDepartAssessMap)
		super.info(id)
	}


	def getDepartAssessMap: Map[SalesOrder, Map[Technic, DepartAssess]] = {
		entityDao.getAll(classOf[DepartAssess]).groupBy(a => a.salesOrder).map(b => (b._1, b._2.map(c => (c.technic, c)).toMap))
	}

	def review(): View = {
		val ids = longIds("salesOrder")
		val salesOrders = entityDao.find(classOf[SalesOrder], ids)
		salesOrders.foreach(salesOrder => {
			salesOrder.scheduledOn = null
		})
		val departAssesses = entityDao.findBy(classOf[DepartAssess], "salesOrder", salesOrders)
		departAssesses.foreach(departAssess => {
			departAssess.passed = false
		})
		entityDao.saveOrUpdate(departAssesses)
		redirect("search", "info.save.success")
	}

	def cancel(): View = {
		val ids = longIds("salesOrder")
		val salesOrders = entityDao.find(classOf[SalesOrder], ids)
		salesOrders.foreach(salesOrder => {
			salesOrder.status = OrderStatus.Cancel
			get("remark").foreach(remark => {
				salesOrder.remark = Option(remark)
			})
		})
		entityDao.saveOrUpdate(salesOrders)
		redirect("search", "info.save.success")
	}

}
