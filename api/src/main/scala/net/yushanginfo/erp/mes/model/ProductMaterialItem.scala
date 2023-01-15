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

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Updated

class ProductMaterialItem extends LongId with Updated {
  var product: Product = _
  /** 排序 */
  var indexno: String = _

  /** 材料 */
  var material: Material = _

  /** 数量 */
  var amount: Float = _

  /** 单别 */
  var cb002: String = _
  /** 单号 */
  var cb003: String = _
}
