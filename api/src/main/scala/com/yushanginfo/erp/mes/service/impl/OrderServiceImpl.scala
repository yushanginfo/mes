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
package com.yushanginfo.erp.mes.service.impl

import com.yushanginfo.erp.base.model.{Customer, User}
import com.yushanginfo.erp.mes.model.{AssessLog, AssessStatus, ReviewEvent, WorkOrder}
import com.yushanginfo.erp.mes.service.{MailContentGenerator, MailNotifierBuilder, OrderService}
import org.beangle.commons.bean.Initializing
import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.Strings
import org.beangle.commons.logging.Logging
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.ems.app.Ems
import org.beangle.notify.SendingObserver
import org.beangle.notify.mail.{DefaultMailNotifier, JavaMailSender, MailMessage}

import java.time.{LocalDate, ZoneId}
import scala.collection.mutable

class OrderServiceImpl extends OrderService with Logging with Initializing {

  var entityDao: EntityDao = _
  var mailNotifier: DefaultMailNotifier = _
  var mailNotifierBuilder: MailNotifierBuilder = _
  var mailGenerator = MailContentGenerator().forTemplate("/com/yushanginfo/erp/mes/wo/mail/order.ftl")
  var reviewGenerator = MailContentGenerator().forTemplate("/com/yushanginfo/erp/mes/wo/mail/review.ftl")

  override def init(): Unit = {
    mailNotifierBuilder.builder().foreach { nf =>
      mailNotifier = nf
    }
  }

  override def recalcState(order: WorkOrder, operator: User, ip: String): Unit = {
    val originStatus = order.assessStatus
    val allComplete = order.technics.nonEmpty && !order.technics.exists(!_.passed.getOrElse(false))

    //计算计划完工时间
    order.materialAssess foreach { materialAssess =>
      if (allComplete && order.assessStatus != AssessStatus.Passed) {
        val processDays = order.technics.foldLeft(0)(_ + _.days.get)
        val startOn =
          if (materialAssess.ready) {
            LocalDate.ofInstant(order.technics.map(_.updatedAt).max, ZoneId.systemDefault()).plusDays(1)
          } else {
            materialAssess.readyOn.get
          }

        order.scheduledOn = Some(startOn.plusDays(processDays))
        order.deadline foreach { deadline =>
          if (order.scheduledOn.get.compareTo(deadline) > 0) {
            //此处注意复审轮次
            order.assessStatus = AssessStatus.Unpassed
          } else {
            order.assessStatus = AssessStatus.Passed
          }
        }
      }
    }
    entityDao.saveOrUpdate(order)
    if (originStatus != order.assessStatus) {
      val log = new AssessLog(originStatus, order, operator, ip)
      entityDao.saveOrUpdate(log)
      notifySaler(order)
    }
  }

  override def issueReview(order: WorkOrder, reviewEvent: ReviewEvent, operator: User, ip: String): Unit = {
    val originStatus = order.assessStatus
    order.scheduledOn = null
    order.assessStatus = AssessStatus.Review
    order.technics.foreach(departAssess => {
      departAssess.passed = Some(false)
    })
    if (originStatus != order.assessStatus) {
      val log = new AssessLog(originStatus, order, operator, ip)
      entityDao.saveOrUpdate(log)
    }
    order.reviewEvents += reviewEvent
    entityDao.saveOrUpdate(order)
    notifyReviewers(order, reviewEvent)
  }

  override def notifyReviewers(order: WorkOrder, reviewEvent: ReviewEvent): Unit = {
    val mails = new mutable.ArrayBuffer[MailMessage]
    if (reviewEvent.watchers.nonEmpty) {
      reviewEvent.watchers foreach { saler =>
        val model = Collections.newMap[String, Any]
        model.put("workOrder", order)
        model.put("reviewEvent", reviewEvent)
        model.put("ems", Ems)
        val body = reviewGenerator.generate(model)
        val subject = s"${order.product.name}(${order.batchNum})  ${reviewEvent.issueBy.name}发起${order.reviewEvents.size}轮复审"
        mails += new MailMessage(subject, body, saler.email.get)
      }
      mailNotifier.deliver(mails, SendingObserver.Log)
    }
  }

  /** 通知业务（销售代表） */
  override def notifySaler(order: WorkOrder): Unit = {
    if (order.assessStatus == AssessStatus.Passed || order.assessStatus == AssessStatus.Unpassed || order.assessStatus == AssessStatus.Review) {
      val quickCode = Strings.substringBefore(order.product.specification.getOrElse("."), "-")
      val cquery = OqlBuilder.from[User](classOf[Customer].getName, "c")
      cquery.where("c.quickCode=:quickCode", quickCode)
      cquery.where("c.saler is not null and c.saler.email is not null")
      cquery.select("distinct c.saler")
      val salers = entityDao.search(cquery)
      val mails = new mutable.ArrayBuffer[MailMessage]
      salers foreach { saler =>
        val model = Collections.newMap[String, Any]
        model.put("workOrder", order)
        model.put("ems", Ems)
        val body = mailGenerator.generate(model)
        val subject = s"${order.product.name}(${order.batchNum})评审状态变更为${order.assessStatus.name}"
        mails += new MailMessage(subject, body, saler.email.get)
      }
      mailNotifier.deliver(mails, SendingObserver.Log)
    }
  }
}
