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
import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.{Coded, Named, Remark, Updated}

/**
 * 工艺
 */
class Technic extends IntId with Coded with Named with Updated with Remark {

  /** 工艺说明 */
  var description: Option[String] = _

  /** 场内生产/委托外部 */
  var internal: Boolean = _

  /** 加工中心 */
  var machine: Option[Machine] = None

  /** 供应商 */
  var supplier: Option[Supplier] = None

  /** 评审小组 */
  var assessGroup: Option[AssessGroup] = None

  def title: String = {
    s"${this.code}${this.name} ${this.machine.map(_.name).orNull}"
  }

  /** 评审预计需要的天数，默认为0 */
  var duration: Int = _

}
