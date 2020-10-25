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
package com.yushanginfo.erp.order.admin.web.action.base

import com.yushanginfo.erp.base.model.{Customer, Product, Technic}
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction

class ProductAction extends RestfulAction[Product] {

	override protected def editSetting(entity: Product): Unit = {
		put("customers", entityDao.getAll(classOf[Customer]))
		put("technics", entityDao.getAll(classOf[Technic]))
	}


	override def saveAndRedirect(entity: Product): View = {
		entity.technics.clear()
		val technicIds = getAll("technicIds", classOf[Int])
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
