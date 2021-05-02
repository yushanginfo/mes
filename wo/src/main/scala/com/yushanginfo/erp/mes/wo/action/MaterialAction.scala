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

import com.yushanginfo.erp.base.model.User
import com.yushanginfo.erp.mes.model._
import com.yushanginfo.erp.mes.service.OrderService
import org.beangle.commons.web.util.RequestUtils
import org.beangle.data.dao.OqlBuilder
import org.beangle.security.Securities
import org.beangle.webmvc.api.context.ActionContext
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction

import java.time.Instant

class MaterialAction extends RestfulAction[WorkOrder] {
  var orderService: OrderService = _

  override protected def indexSetting(): Unit = {
    put("orderTypes", entityDao.getAll(classOf[WorkOrderType]))
    put("orderStatuses", entityDao.getAll(classOf[WorkOrderStatus]))
  }

  override def getQueryBuilder: OqlBuilder[WorkOrder] = {
    val builder = super.getQueryBuilder
    val users = entityDao.findBy(classOf[User], "code", List(Securities.user))
    users.map(_.factory).headOption foreach { u =>
      u foreach { f =>
        builder.where("workOrder.factory=:factory", f)
      }
    }
    builder
  }

  override def editSetting(entity: WorkOrder): Unit = {
    val ma = entity.materialAssess.getOrElse(new MaterialAssess)
    if (!ma.persisted) {
      ma.ready = false
    }
    put("materialAssess", ma)
    super.editSetting(entity)
  }

  override def saveAndRedirect(order: WorkOrder): View = {
    val materialAssess = populateEntity(classOf[MaterialAssess], "materialAssess")
    if (order.assessStatus == AssessStatus.Original) {
      order.assessStatus = AssessStatus.Submited
      order.updateAssessBeginAt(Instant.now())
    }
    materialAssess.order = order
    materialAssess.updatedAt = Instant.now
    val users = entityDao.findBy(classOf[User], "code", List(Securities.user))
    materialAssess.assessedBy = users.headOption
    entityDao.saveOrUpdate(materialAssess)
    order.materialAssess = Some(materialAssess)
    orderService.recalcState(order, users.head, RequestUtils.getIpAddr(ActionContext.current.request))
    super.saveAndRedirect(order)
  }

}
