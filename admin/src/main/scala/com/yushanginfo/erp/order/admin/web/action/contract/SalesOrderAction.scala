package com.yushanginfo.erp.order.admin.web.action.contract

import com.yushanginfo.erp.order.base.model.OrderType
import com.yushanginfo.erp.order.model.SalesOrder
import com.yushanginfo.erp.order.model.Product
import org.beangle.webmvc.entity.action.RestfulAction

class SalesOrderAction extends RestfulAction[SalesOrder]{

	override protected def indexSetting(): Unit = {
		put("products", entityDao.getAll(classOf[Product]))
		put("orderTypes",entityDao.getAll(classOf[OrderType]))
	}

	override def editSetting(entity: SalesOrder): Unit = {
		put("products", entityDao.getAll(classOf[Product]))
		put("orderTypes",entityDao.getAll(classOf[OrderType]))
		super.editSetting(entity)
	}

}
