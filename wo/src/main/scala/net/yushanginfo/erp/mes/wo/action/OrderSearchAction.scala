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

package net.yushanginfo.erp.mes.wo.action

import net.yushanginfo.erp.mes.model.*
import org.beangle.data.dao.OqlBuilder
import org.beangle.web.action.annotation.{mapping, param}
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.EntityAction

class OrderSearchAction extends EntityAction[WorkOrder] {

  def index(): View = {
    val factories = entityDao.getAll(classOf[net.yushanginfo.erp.base.model.Factory]).sortBy(_.code)
    val factory = getLong("factory.id").map(fid => factories.find(_.id == fid)).flatten
    val dQuery = OqlBuilder.from(classOf[WorkOrder].getName, "o")
    factory.foreach(f => dQuery.where("o.factory=:factory", f))
    dQuery.groupBy("o.status.id,o.status.name").select("o.status.id,o.status.name,count(*)")
    dQuery.orderBy("o.status.id")
    put("statusStat", entityDao.search(dQuery))

    val sQuery = OqlBuilder.from(classOf[WorkOrder].getName, "o")
    sQuery.groupBy("o.assessStatus").select("o.assessStatus,count(*)")
    sQuery.orderBy("o.assessStatus")
    factory.foreach(f => sQuery.where("o.factory=:factory", f))
    put("assessStatusStat", entityDao.search(sQuery))

    val mQuery = OqlBuilder.from(classOf[WorkOrder].getName, "o")
    mQuery.groupBy("o.product.materialType.id,o.product.materialType.name")
    mQuery.select("o.product.materialType.id,o.product.materialType.name,count(*)")
    mQuery.orderBy("o.product.materialType.id")
    factory.foreach(f => mQuery.where("o.factory=:factory", f))
    put("materialTypeStat", entityDao.search(mQuery))

    put("assessStatuses", AssessStatus.values)
    put("statuses", entityDao.getAll(classOf[WorkOrderType]))
    put("factory", factory)
    put("factories",factories)
    forward()
  }

  def search(): View = {
    val query = getQueryBuilder
    get("q") foreach { q =>
      query.where("workOrder.product.code like :q" +
        " or workOrder.product.specification like :q" +
        " or workOrder.batchNum like :q", s"%${q.trim}%")
    }
    query.orderBy("workOrder.updatedAt desc")
    query.limit(getPageLimit)
    put("workOrders", entityDao.search(query))
    forward()
  }

  @mapping(value = "{id}")
  def info(@param("id") id: String): View = {
    val order = entityDao.get(classOf[WorkOrder], id.toLong)
    val logQuery = OqlBuilder.from(classOf[AssessLog], "al").where("al.orderId=:orderId", order.id)
    logQuery.orderBy("al.updatedAt")
    val logs = entityDao.search(logQuery)
    put("logs", logs)
    put("workOrder", order)

    val recQuery = OqlBuilder.from(classOf[AssessRecord], "r")
    recQuery.where("r.order=:order", order)
    recQuery.orderBy("r.updatedAt")
    put("assessRecords", entityDao.search(recQuery))
    forward()
  }
}
