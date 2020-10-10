package com.yushanginfo.erp.order.admin.web.action.contract

import com.yushanginfo.erp.order.base.model.{OrderStatus, Technic}
import com.yushanginfo.erp.order.model.{DepartAssess, SalesOrder}
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
