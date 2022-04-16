/*
 * Copyright (C) 2020, The YushangInfo Software.
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

package com.yushanginfo.erp.mes.base.ws

import com.yushanginfo.erp.base.model.User
import org.beangle.commons.collection.page.PageLimit
import org.beangle.commons.collection.{Order, Properties}
import org.beangle.data.dao.OqlBuilder
import org.beangle.web.action.support.ActionSupport
import org.beangle.web.action.annotation.response
import org.beangle.webmvc.support.action.EntityAction
import org.beangle.webmvc.support.helper.QueryHelper.{PageParam, PageSizeParam}

class UserWs extends ActionSupport with EntityAction[User] {
  @response
  def index(): Seq[Properties] = {
    val query = OqlBuilder.from(classOf[User], "user")
    populateConditions(query)
    query.limit(PageLimit(getInt(PageParam, 1), getInt(PageSizeParam, 100)))
    get("q") foreach { q =>
      val c = s"%$q%"
      query.where("user.name like :c or user.code like :c", c)
    }
    val orderStr = get(Order.OrderStr).getOrElse("user.name")
    query.orderBy(orderStr)
    entityDao.search(query).map { t =>
      new Properties(t, "id", "code", "name", "description")
    }
  }
}
