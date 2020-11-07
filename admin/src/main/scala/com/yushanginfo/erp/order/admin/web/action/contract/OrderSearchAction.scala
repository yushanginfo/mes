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
package com.yushanginfo.erp.order.admin.web.action.contract

import com.yushanginfo.erp.order.model.{OrderStatus, SalesOrder}
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.annotation.{mapping, param}
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.EntityAction

class OrderSearchAction extends EntityAction[SalesOrder] {


  def index(): View = {
    val dQuery = OqlBuilder.from(classOf[SalesOrder].getName, "o")
    dQuery.groupBy("o.status").select("o.status,count(*)")
    dQuery.orderBy("o.status")
    put("stateStat", entityDao.search(dQuery))

    val mQuery = OqlBuilder.from(classOf[SalesOrder].getName, "o")
    mQuery.groupBy("o.product.materialType.id,o.product.materialType.name")
    mQuery.select("o.product.materialType.id,o.product.materialType.name,count(*)")
    mQuery.orderBy("o.product.materialType.id")
    put("materialTypeStat", entityDao.search(mQuery))

    put("statuses", OrderStatus.values)
    forward()
  }

  def search(): View = {
    val query = getQueryBuilder
    get("q") foreach { q =>
      query.where("salesOrder.product.code like :q" +
        " or salesOrder.product.specification like :q" +
        " or salesOrder.batchNum like :q" +
        " or salesOrder.code like :q ", s"%${q.trim}%")
    }
    query.orderBy("salesOrder.updatedAt desc")
    query.limit(getPageLimit)
    put("salesOrders", entityDao.search(query))
    forward()
  }

  @mapping(value = "{id}")
  def info(@param("id") id: String): View = {
    put(simpleEntityName, getModel[SalesOrder](entityName, convertId(id)))
    forward()
  }
}
