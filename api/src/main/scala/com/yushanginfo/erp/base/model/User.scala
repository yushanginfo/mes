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

package com.yushanginfo.erp.base.model

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo._

/**
 * 用户信息
 */
class User extends LongId with Coded with Named with Updated with Remark with TemporalOn {

  var department: Department = _

  var email: Option[String] = None

  var mobile: Option[String] = None

  var factory: Option[Factory] = None

  def description: String = {
    s"${code} ${name} ${department.name}"
  }

}
