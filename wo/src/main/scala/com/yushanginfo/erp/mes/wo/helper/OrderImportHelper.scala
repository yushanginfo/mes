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
package com.yushanginfo.erp.mes.wo.helper

import java.time.Instant

import com.yushanginfo.erp.mes.model.{AssessStatus, WorkOrder}
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.data.transfer.importer.{ImportListener, ImportResult}

class OrderImportHelper(entityDao: EntityDao) extends ImportListener {

  override def onItemFinish(tr: ImportResult): Unit = {
    val order = transfer.current.asInstanceOf[WorkOrder]
    order.updatedAt = Instant.now
    if (null == order.createdAt) {
      order.createdAt = Instant.now()
    }
    if (null != order.product && null != order.factory) {
      entityDao.saveOrUpdate(order)
    }
  }

  override def onStart(tr: ImportResult): Unit = {

  }

  override def onFinish(tr: ImportResult): Unit = {
  }

  override def onItemStart(tr: ImportResult): Unit = {
    //通过单别和工单单号联合唯一
    for (workOrderTypeName <- transfer.curData.get("workOrder.orderType.name"); batchNum <- transfer.curData.get("workOrder.batchNum")) {
      val builder = OqlBuilder.from(classOf[WorkOrder], "wo")
      builder.where("wo.workOrderType.name=:workOrderTypeName", workOrderTypeName)
      builder.where("wo.batchNum=:batchNum", batchNum)
      entityDao.search(builder) foreach { p =>
        transfer.current = p
        if (p.assessStatus == AssessStatus.Passed) {
          tr.addFailure("工单已经评审通过，无需导入", batchNum)
        }
      }
    }
  }
}
