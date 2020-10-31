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

import java.time.LocalDate

import com.yushanginfo.erp.base.model.{Customer, Product, TechnicScheme}
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.{Coded, Remark, Updated}

/** 订单
 *
 */
class SalesOrder extends LongId with Coded with Updated with Remark {

  /** 生产批号 */
  var batchNum: String = _

  /** 客户信息 */
  var customer: Customer = _

  /** 产品信息 */
  var product: Product = _

  /** 订单工艺 */
  var technicScheme: TechnicScheme = _

  /** 订单类型 */
  var orderType: OrderType = _

  /** 数量 */
  var count: Int = _

  /** 计划交付日期 */
  var requireOn: LocalDate = _

  /** 计划完工日期 */
  var scheduledOn: Option[LocalDate] = None

  /** 订单状态 */
  var status: OrderStatus.Status = OrderStatus.Original

  /** 到料日期 */
  var materialDate: Option[LocalDate] = None
}