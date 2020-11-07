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
package com.yushanginfo.erp.order.admin.web.helper

import java.time.Instant

import com.yushanginfo.erp.order.model.{OrderStatus, SalesOrder}
import org.beangle.data.dao.EntityDao
import org.beangle.data.transfer.importer.{ImportListener, ImportResult}

class OrderImportHelper(entityDao: EntityDao) extends ImportListener {

  override def onItemFinish(tr: ImportResult): Unit = {
    val order = transfer.current.asInstanceOf[SalesOrder]
    order.updatedAt = Instant.now
    if (null != order.product) {
      transfer.curData.get("technicScheme.indexno") foreach { indexno =>
        order.product.technicSchemes.find(x => x.indexno == indexno) foreach { scheme =>
          order.technicScheme = scheme
        }
      }
      if (null == order.technicScheme && order.product.technicSchemes.nonEmpty) {
        order.technicScheme = order.product.technicSchemes.head
      }
    }
    if (order.technicScheme == null) {
      tr.addFailure("缺少工艺路线", transfer.curData.get("salesOrder.product.code"))
    }
    if (null != order.product && null != order.customer && null != order.factory) {
      entityDao.saveOrUpdate(order)
    }
  }

  override def onStart(tr: ImportResult): Unit = {

  }

  override def onFinish(tr: ImportResult): Unit = {
  }

  override def onItemStart(tr: ImportResult): Unit = {
    transfer.curData.get("salesOrder.batchNum") foreach { batchNum =>
      entityDao.findBy(classOf[SalesOrder], "batchNum", List(batchNum)) foreach { p =>
        transfer.current = p
        if (p.status == OrderStatus.Passed) {
          tr.addFailure("订单已经评审通过，无需导入", batchNum)
        }
      }
    }
  }
}