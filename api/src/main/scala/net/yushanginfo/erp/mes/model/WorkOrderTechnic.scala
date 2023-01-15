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

package net.yushanginfo.erp.mes.model

import net.yushanginfo.erp.base.model.{Factory, Supplier, User}
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Updated

import java.time.LocalDate

/**
 * 工单工艺信息
 */
class WorkOrderTechnic extends LongId with Updated {

  def this(workOrder: WorkOrder, technic: Technic) = {
    this()
    this.workOrder = workOrder
    this.technic = technic
  }

  /** 工单 */
  var workOrder: WorkOrder = _

  /** 工序 */
  var technic: Technic = _

  /** 加工顺序 */
  var indexno: String = _

  /** 工艺说明 */
  var description: Option[String] = _

  /** 场内生产/委托外部 */
  var internal: Boolean = _

  /** 加工中心 */
  var machine: Option[Machine] = None

  /** 供应商 */
  var supplier: Option[Supplier] = None

  /** 生产厂区 */
  var factory: Factory = _

  /** 需要天数 */
  var days: Option[Int] = None

  /** 是否通过 */
  var passed: Option[Boolean] = None

  /** 评估人 */
  var assessedBy: Option[User] = None

  /**实际开工*/
  var beginOn:Option[LocalDate]=None

  /**实际完工*/
  var endOn:Option[LocalDate] =None

  /**完工数量*/
  var finishedQuantity:Float=_

  def machineOrSupplierName: String = {
    machine match {
      case Some(m) => m.name
      case None =>
        supplier match {
          case Some(s) => s.name
          case None => "--"
        }
    }
  }
}
