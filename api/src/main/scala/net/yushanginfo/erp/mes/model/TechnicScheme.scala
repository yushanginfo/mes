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

import org.beangle.commons.collection.Collections
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.{Named, Updated}

import scala.collection.mutable

/** 工艺路线 */
class TechnicScheme extends LongId with Named with Updated {

  /** 工艺路线编号 */
  var indexno: String = _

  /** 产品 */
  var product: Product = _

  /** 工序列表 */
  var technics: mutable.Buffer[ProductTechnic] = Collections.newBuffer[ProductTechnic]

  def title: String = {
    s"${this.indexno} ${this.name}"
  }
}
