/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
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
package com.yushanginfo.erp.order.model

import com.yushanginfo.erp.order.base.model.{Factory, Technic}
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Updated


/**
 * 评估信息
 */
class DepartAssess extends LongId with Updated {

	var salesOrder: SalesOrder = _

	/** 工序 */
	var technic: Technic = _

	/** 需要天数 */
	var days: Int = _

	/** 生产厂区 */
	var factory: Factory = _

	/** 是否通过 */
	var passed: Boolean = _

}
