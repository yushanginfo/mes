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
package com.yushanginfo.erp.base.model

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.{Coded, Named, Remark, Updated}

/**
 * 客户信息
 */
class Customer extends LongId with Coded with Named with Updated with Remark {

  /** 客户简称 */
  var shortName: String = _

  /** 业务人员 */
  var saler: Option[User] = None

  /** 快捷码 */
  var quickCode: Option[String] = None

  /** 总公司 */
  var parent: Option[Customer] = None

  def title: String = {
    s"${this.quickCode.orNull} ${this.name} "
  }

}
