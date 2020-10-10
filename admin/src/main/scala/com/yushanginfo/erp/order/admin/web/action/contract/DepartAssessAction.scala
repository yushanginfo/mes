package com.yushanginfo.erp.order.admin.web.action.contract

import com.yushanginfo.erp.order.base.model.{Factory, OrderStatus, OrderType, Technic}
import com.yushanginfo.erp.order.model.{DepartAssess, Product, SalesOrder}
import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.web.ProjectSupport

class DepartAssessAction extends RestfulAction[DepartAssess] with ProjectSupport {

	override protected def indexSetting(): Unit = {
		put("products", entityDao.getAll(classOf[Product]))
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
		put("departAssessMap", getDepartAssessMap)
		forward()
	}

	def getDepartAssessMap: Map[SalesOrder, Map[Technic, DepartAssess]] = {
		val builder = OqlBuilder.from(classOf[DepartAssess], "departAssess")
		//部门权限
		addDepart(builder, "departAssess.technic.department")
		val departAssesses = entityDao.search(builder)
		departAssesses.groupBy(a => a.salesOrder).map(b => (b._1, b._2.map(c => (c.technic, c)).toMap))
	}

	override def editSetting(entity: DepartAssess): Unit = {
		put("factories", entityDao.getAll(classOf[Factory]))
		get("technicId").foreach(technicId => {
			put("technic", entityDao.get(classOf[Technic], technicId.toInt))
		})
		get("salesOrderId").foreach(salesOrderId => {
			put("salesOrder", entityDao.get(classOf[SalesOrder], salesOrderId.toLong))
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
		var i = 1
		entity.salesOrder.product.technics.foreach(technic => {
			val builder = OqlBuilder.from(classOf[DepartAssess], "departAssess")
			builder.where("departAssess.technic=:technic", technic)
			builder.where("departAssess.salesOrder=:salesOrder", entity.salesOrder)
			builder.where("departAssess.passed=true")
			val departAssesses = entityDao.search(builder)
			if (departAssesses.isEmpty) {
				i &= 0
			}
		})
		//计算计划完工时间
		if (entity.salesOrder.materialDate.nonEmpty && i == 1 && entity.salesOrder.scheduledOn.isEmpty) {
			entity.salesOrder.scheduledOn = entity.salesOrder.materialDate
			entity.salesOrder.product.technics.foreach(technic => {
				entity.salesOrder.scheduledOn = Option(entity.salesOrder.scheduledOn.get.plusDays(getDepartAssessMap.get(entity.salesOrder).get.get(technic).get.days.toLong))
			})
			val a = entity.salesOrder.scheduledOn.get.compareTo(entity.salesOrder.requireOn)
			if (entity.salesOrder.scheduledOn.get.compareTo(entity.salesOrder.requireOn) > 0) {
				entity.salesOrder.status = OrderStatus.Unpassed
			} else {
				entity.salesOrder.status = OrderStatus.Passed
			}
		}
		super.saveAndRedirect(entity)
	}

}
