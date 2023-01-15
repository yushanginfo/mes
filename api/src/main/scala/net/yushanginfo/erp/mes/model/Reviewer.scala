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

import net.yushanginfo.erp.base.model.{Factory, User}
import org.beangle.commons.collection.Collections
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.TemporalOn

import scala.collection.mutable

/** 复审员
 *
 */
class Reviewer extends LongId with TemporalOn {
  /** 用户 */
  var user: User = _
  /** 工厂 */
  var factories: mutable.Set[Factory] = Collections.newSet[Factory]
  /** 复审轮次 */
  var rounds: mutable.Set[Int] = Collections.newSet[Int]
}
