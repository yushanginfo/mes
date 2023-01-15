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

import net.yushanginfo.erp.base.model.Supplier
import org.beangle.data.model.LongId

/** 产品工艺 */
class ProductTechnic extends LongId {
  /** 工艺路线 */
  var scheme: TechnicScheme = _
  /** 工艺 */
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

  def this(indexno: String, scheme: TechnicScheme, technic: Technic) = {
    this()
    this.indexno = indexno
    this.scheme = scheme
    this.technic = technic
  }

}
