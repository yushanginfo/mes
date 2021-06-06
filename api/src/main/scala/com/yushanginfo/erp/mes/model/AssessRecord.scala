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
package com.yushanginfo.erp.mes.model

import org.beangle.commons.collection.Collections
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Updated

import java.time.{Instant, LocalDate}
import scala.collection.mutable

/** 完整的评审信息
 *
 */
class AssessRecord extends LongId with Updated {

  var order: WorkOrder = _

  var items: mutable.Buffer[AssessItem] = Collections.newBuffer[AssessItem]

  /** 是否有料 */
  var materialReady: Boolean = _

  /** 到料日期 */
  var materialReadyOn: Option[LocalDate] = None

  /** 评审交付日期 */
  var scheduledOn: LocalDate = _

  /** 状态 */
  var assessStatus: AssessStatus.Status = _

  def getItem(technic: WorkOrderTechnic): Option[AssessItem] = {
    items.find { x =>
      x.indexno == technic.indexno && x.matchine == technic.machineOrSupplierName
    }
  }

  def this(order: WorkOrder) = {
    this()
    this.order = order
    this.updatedAt = Instant.now

    order.technics foreach { technic =>
      val machine = technic.machineOrSupplierName
      val item = new AssessItem(this, technic.indexno, machine, technic.technic, technic.days.get)
      items += item
    }
    this.order.materialAssess.foreach { ma =>
      this.materialReady = ma.ready
      this.materialReadyOn = ma.readyOn
    }
    this.scheduledOn = order.scheduledOn.get
    this.assessStatus = order.assessStatus
  }
}
