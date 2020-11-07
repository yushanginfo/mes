/*
 * Agile Enterprice Resource Planning Solution.
 *
 * Copyright © 2020, The YushangInfo Software.
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

import com.yushanginfo.erp.mes.model.Product
import org.beangle.commons.collection.Properties
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{mapping, response}

class ProductWs extends ActionSupport {
  var entityDao: EntityDao = _

  @response
  @mapping("")
  def index: Seq[Properties] = {
    val query = OqlBuilder.from(classOf[Product], "p")
    get("q") foreach { q =>
      if (Strings.isNotEmpty(q)) {
        query.where("p.code like :q or p.name like :q", "%" + q + "%")
      }
    }

    getBoolean("hasTechnicScheme") foreach { hasTechnicScheme =>
      if (hasTechnicScheme) {
        query.where("size(p.technicSchemes)>0")
      } else {
        query.where("size(p.technicSchemes)=0")
      }
    }
    query.limit(1, 10)
    entityDao.search(query).map { pd =>
      val p = new Properties()
      p.put("id", pd.id.toString)
      p.put("title", pd.title)
      p
    }
  }
}
