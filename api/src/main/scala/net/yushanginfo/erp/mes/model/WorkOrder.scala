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
import org.beangle.data.model.pojo.{Remark, Updated}

import java.time.{Instant, LocalDate}
import scala.collection.mutable

/** 工单
 */
class WorkOrder extends LongId with Updated with Remark {

  /** 工单单别 */
  var orderType: WorkOrderType = _

  /** 工单单号 */
  var batchNum: String = _

  /** 产品信息 */
  var product: Product = _

  /** 数量 */
  var amount: Int = _

  /** 客户交付日期 */
  var deadline: Option[LocalDate] = None

  /** 计划完工日期 */
  var plannedEndOn: Option[LocalDate] = None

  /** 评审交付日期 */
  var scheduledOn: Option[LocalDate] = None

  /** 工单状态 */
  var status: WorkOrderStatus = _

  /** 工单评审状态 */
  var assessStatus: AssessStatus = AssessStatus.Original

  /** 到料日期 */
  var materialAssess: Option[MaterialAssess] = None

  /** 工单工艺 */
  var technics: mutable.Buffer[WorkOrderTechnic] = Collections.newBuffer[WorkOrderTechnic]

  /** 复审事件 */
  var reviewEvents: mutable.Buffer[ReviewEvent] = Collections.newBuffer[ReviewEvent]

  /** 生产工厂 */
  var factory: Factory = _

  /** 开单时间 */
  var createdAt: Instant = _

  /** 评审开始于 */
  var assessBeginAt: Option[Instant] = None

  /** 复审开始于 */
  var reviewAssessBeginAt: Option[Instant] = None

  /** 业务员 */
  var saler: Option[User] = None

  def canAssess: Boolean = {
    assessStatus != AssessStatus.Unpassed && assessStatus != AssessStatus.Passed
  }

  def inReview: Boolean = {
    assessStatus == AssessStatus.Review || assessStatus == AssessStatus.Unpassed
  }

  def updateAssessBeginAt(now: Instant): Unit = {
    assessBeginAt match {
      case Some(beginAt) => if (now.isBefore(beginAt)) assessBeginAt = Some(now)
      case None => assessBeginAt = Some(now)
    }
  }

  def updateReviewAssessBeginAt(now: Instant): Unit = {
    reviewAssessBeginAt match {
      case Some(beginAt) => if (now.isBefore(beginAt)) reviewAssessBeginAt = Some(now)
      case None => reviewAssessBeginAt = Some(now)
    }
  }

  def calcFinishedOn(lastFinishedOn:java.sql.Date,remindDays:Number):LocalDate={
    lastFinishedOn.toLocalDate.minusDays(remindDays.longValue())
  }
}
