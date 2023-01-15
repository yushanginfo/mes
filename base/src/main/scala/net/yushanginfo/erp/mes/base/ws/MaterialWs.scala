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

package net.yushanginfo.erp.mes.base.ws

import net.yushanginfo.erp.mes.model.Material
import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.web.action.support.ActionSupport
import org.beangle.web.action.annotation.{mapping, response}

class MaterialWs extends ActionSupport {
  var entityDao: EntityDao = _

  @response
  @mapping("")
  def index: Seq[Properties] = {
    val query = OqlBuilder.from(classOf[Material], "es")
    query.where("es.code like :q or es.name like :q", "%" + get("q", "") + "%")
    query.limit(1, 10)
    entityDao.search(query).map { es =>
      val p = new Properties()
      p.put("id", es.id.toString)
      p.put("title", es.title)
      p
    }
  }
}
