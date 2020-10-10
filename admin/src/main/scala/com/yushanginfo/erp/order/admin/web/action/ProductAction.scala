package com.yushanginfo.erp.order.admin.web.action

import com.yushanginfo.erp.order.base.model.{Customer, Technic}
import org.beangle.webmvc.entity.action.RestfulAction
import com.yushanginfo.erp.order.model.Product
import org.beangle.webmvc.api.view.View

class ProductAction extends RestfulAction[Product] {

	override protected def editSetting(entity: Product): Unit = {
		put("customers", entityDao.getAll(classOf[Customer]))
		put("technics", entityDao.getAll(classOf[Technic]))
	}


	override def saveAndRedirect(entity: Product): View = {
		entity.technics.clear()
		val technicIds = getAll("technicId2nd", classOf[Int])
		val newTechnics = entityDao.find(classOf[Technic], technicIds)
		val removed = entity.technics.filter(
			x => !newTechnics.contains(x)
		)
		entity.technics.subtractAll(removed)
		//保证保存顺序
		technicIds.foreach(id => {
			entity.technics ++= entityDao.find(classOf[Technic], id)
		})
		super.saveAndRedirect(entity)
	}

}
