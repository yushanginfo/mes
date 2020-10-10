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

import com.yushanginfo.erp.order.base.model.{Customer, User}
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction

class CustomerAction extends RestfulAction[Customer] {

  override protected def editSetting(entity: Customer): Unit = {
    put("users", entityDao.getAll(classOf[User]))
  }

  override def saveAndRedirect(entity: Customer): View ={
    entity.salers.clear()
    val salerIds = getAll("salerId2nd", classOf[Long])
    entity.salers++=entityDao.find(classOf[User],salerIds)
    super.saveAndRedirect(entity)
  }

//  def salerAjax(): View = {
//    val query = OqlBuilder.from(classOf[User], "user")
//    query.orderBy("user.code")
//    populateConditions(query)
//    get("term").foreach(codeOrName => {
//      query.where("(user.name like :name or user.code like :code)", '%' + codeOrName + '%', '%' + codeOrName + '%')
//    })
//    query.limit(getPageLimit)
//    put("users", entityDao.search(query))
//    forward("usersJSON")
//  }
}
