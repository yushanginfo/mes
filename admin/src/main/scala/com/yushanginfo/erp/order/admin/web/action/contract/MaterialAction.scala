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
import com.yushanginfo.erp.order.service.OrderService
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction

class MaterialAction extends RestfulAction[SalesOrder] {
  var orderService: OrderService = _

  override protected def indexSetting(): Unit = {
  }

  override def saveAndRedirect(entity: SalesOrder): View = {
    entity.status = OrderStatus.Submited
    orderService.recalcState(entity)
    super.saveAndRedirect(entity)
  }

}
