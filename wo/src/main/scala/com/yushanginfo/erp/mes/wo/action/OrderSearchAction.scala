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
package com.yushanginfo.erp.mes.wo.action

import com.yushanginfo.erp.mes.model.{AssessLog, AssessRecord, AssessStatus, WorkOrder, WorkOrderType}
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.annotation.{mapping, param}
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.EntityAction

class OrderSearchAction extends EntityAction[WorkOrder] {

  def index(): View = {
    val dQuery = OqlBuilder.from(classOf[WorkOrder].getName, "o")
    dQuery.groupBy("o.status.id,o.status.name").select("o.status.id,o.status.name,count(*)")
    dQuery.orderBy("o.status.id")
    put("statusStat", entityDao.search(dQuery))

    val sQuery = OqlBuilder.from(classOf[WorkOrder].getName, "o")
    sQuery.groupBy("o.assessStatus").select("o.assessStatus,count(*)")
    sQuery.orderBy("o.assessStatus")
    put("assessStatusStat", entityDao.search(sQuery))

    val mQuery = OqlBuilder.from(classOf[WorkOrder].getName, "o")
    mQuery.groupBy("o.product.materialType.id,o.product.materialType.name")
    mQuery.select("o.product.materialType.id,o.product.materialType.name,count(*)")
    mQuery.orderBy("o.product.materialType.id")
    put("materialTypeStat", entityDao.search(mQuery))

    put("assessStatuses", AssessStatus.values)
    put("statuses", entityDao.getAll(classOf[WorkOrderType]))
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

    val recQuery= OqlBuilder.from(classOf[AssessRecord],"r")
    recQuery.where("r.order=:order",order)
    recQuery.orderBy("r.updatedAt")
    put("assessRecords",entityDao.search(recQuery))
    forward()
  }
}
