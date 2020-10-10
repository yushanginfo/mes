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

import com.yushanginfo.erp.order.base.model.{Customer, Technic}
import org.beangle.commons.collection.Collections
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.{Coded, Named, Remark, Updated}

import scala.collection.mutable

/**
 * 产品信息
 */
class Product extends LongId with Coded with Named with Updated with Remark{

	/** 客户信息 */
	var customer: Customer = _

	/** 规格 */
	var standard: String = _

	/** 工序列表 */
	var technics: mutable.Buffer[Technic] = Collections.newBuffer[Technic]

}
