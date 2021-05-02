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
package com.yushanginfo.erp.mes.wo.service

import com.yushanginfo.erp.mes.model.{AssessMember, AssessStatus, WorkOrder}
import com.yushanginfo.erp.mes.service.{MailNotifierBuilder}
import org.beangle.commons.bean.Initializing
import org.beangle.commons.collection.Collections
import org.beangle.commons.logging.Logging
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.data.hibernate.spring.SessionUtils
import org.beangle.ems.app.Ems
import org.beangle.notify.SendingObserver
import org.beangle.notify.mail.{DefaultMailNotifier, MailMessage}
import org.beangle.template.freemarker.DefaultTemplateEngine
import org.hibernate.SessionFactory

import java.time.{LocalDate, LocalTime}
import scala.collection.mutable

class CronMailNotifier extends Initializing with Logging {

  var mailNotifierBuilder:MailNotifierBuilder=_
  var mailNotifier: DefaultMailNotifier = _
  var entityDao: EntityDao = _
  var sessionFactory: SessionFactory = _

  override def init(): Unit = {
    mailNotifierBuilder.builder() foreach{ nf=>
      mailNotifier = nf
      // 08:00,12:00
      val times = Set(LocalTime.of(7, 0), LocalTime.of(11, 0))
      CronMailDaemon.start("ERP Mail Notifier", this, times)
    }
  }

  def sendMail(): String = {
    SessionUtils.enableBinding(sessionFactory)
    try {
      if (null == mailNotifier) {
        "mailSender is not ready"
      } else {
        doSendMail()
      }
    }
    catch {
      case e: Exception => e.printStackTrace()
        "error happed"
    } finally {
      SessionUtils.disableBinding(sessionFactory)
      SessionUtils.closeSession(sessionFactory)
    }
  }

  def doSendMail(): String = {
    val members = entityDao.search(OqlBuilder.from(classOf[AssessMember], "m").where("m.user.email is not null"))
    val groups = members.groupBy(f => (f.group, f.factory))

    val generator = DefaultTemplateEngine().forTemplate("/com/yushanginfo/erp/mes/wo/mail/body.ftl")
    val mails = new mutable.ArrayBuffer[MailMessage]
    groups foreach { kv =>
      val gf = kv._1
      val builder = OqlBuilder.from(classOf[WorkOrder], "workOrder")
      builder.orderBy("workOrder.createdAt desc")
      builder.where("exists(from workOrder.technics wt where wt.technic.assessGroup =:group" +
        " and wt.factory = :factory and (wt.passed is null or wt.passed=false))", gf._1, gf._2)
      builder.where("workOrder.assessStatus in (:statuses)", Array(AssessStatus.Original, AssessStatus.Review, AssessStatus.Submited))
      val orders = entityDao.search(builder)
      if (orders.nonEmpty) {
        for (am <- kv._2) {
          val model = Collections.newMap[String, Any]
          model.put("workOrders", orders)
          model.put("ems", Ems)
          model.put("assessGroup", gf._1)
          model.put("factory", gf._2)
          model.put("user", am.user)
          val body = generator.render(model)
          val subject = s"${LocalDate.now()} ${gf._2.name} ${gf._1.name} ${orders.size}个待评审工单提醒"
          mails += new MailMessage(subject, body, am.user.email.get)
        }
      }
    }
    if (mails.nonEmpty) {
      mailNotifier.deliver(mails, SendingObserver.Log)
      s"send ${mails.size} mails"
    } else {
      "none mail is sended"
    }
  }

}
