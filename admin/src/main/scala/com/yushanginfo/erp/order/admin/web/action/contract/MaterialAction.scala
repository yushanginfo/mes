package com.yushanginfo.erp.order.admin.web.action.contract

import com.yushanginfo.erp.order.base.model.OrderStatus
import com.yushanginfo.erp.order.model.{Product, SalesOrder}
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction

class MaterialAction extends RestfulAction[SalesOrder] {

	override protected def indexSetting(): Unit = {
		put("products", entityDao.getAll(classOf[Product]))
	}

	override def saveAndRedirect(entity: SalesOrder): View = {
		entity.status = OrderStatus.Submited
		super.saveAndRedirect(entity)
	}

}
