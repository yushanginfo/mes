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

package net.yushanginfo.erp.mes.service

import net.yushanginfo.erp.base.model.User
import net.yushanginfo.erp.mes.model.{ReviewEvent, WorkOrder}

trait OrderService {
  def recalcState(order: WorkOrder, user: User, ip: String): Unit

  /** 发起复审 */
  def issueReview(order: WorkOrder, reviewEvent: ReviewEvent, operator: User, ip: String): Unit

  /** 通知复审需要反馈的人员 */
  def notifyReviewers(order: WorkOrder, reviewEvent: ReviewEvent): Unit

  /** 通知业务（销售代表） */
  def notifySaler(order: WorkOrder): Unit
}
