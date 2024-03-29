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

import net.yushanginfo.erp.base.model.User
import org.beangle.commons.collection.Collections
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Updated

import java.time.{Instant, LocalDate}
import scala.collection.mutable

/** 到料信息 */
class MaterialAssess extends LongId with Updated {

  /** 工单 */
  var order: WorkOrder = _

  /** 是否有料 */
  var ready: Boolean = _

  /** 到料日期 */
  var readyOn: Option[LocalDate] = None

  /** 评估人 */
  var assessedBy: Option[User] = None

  /** 创建时间 */
  var createdAt: Instant = Instant.now

  /** bom到料日期 */
  var items: mutable.Buffer[MaterialItemAssess] = Collections.newBuffer[MaterialItemAssess]

  def getItemAssess(item: ProductMaterialItem): Option[MaterialItemAssess] = {
    items.find(_.item == item)
  }

  def assessComplete: Boolean = {
    ready || readyOn.nonEmpty
  }
}
