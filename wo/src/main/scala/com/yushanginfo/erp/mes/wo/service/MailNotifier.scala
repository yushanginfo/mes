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

import java.io.{IOException, StringWriter}
import java.time.{LocalDate, LocalTime}
import java.{util => ju}

import com.yushanginfo.erp.mes.model.{AssessMember, AssessStatus, WorkOrder}
import freemarker.cache.StrongCacheStorage
import freemarker.core.ParseException
import freemarker.template.{Configuration, SimpleHash, Template}
import org.beangle.commons.bean.Initializing
import org.beangle.commons.lang.Throwables
import org.beangle.commons.logging.Logging
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.data.hibernate.spring.SessionUtils
import org.beangle.ems.app.Ems
import org.beangle.notify.SendingObserver
import org.beangle.notify.mail.{DefaultMailNotifier, JavaMailSender, MailMessage}
import org.beangle.template.freemarker.{BeangleClassTemplateLoader, BeangleObjectWrapper, IncludeIfExistsModel}
import org.hibernate.SessionFactory

import scala.collection.mutable

class MailNotifier extends Initializing with Logging {

  var host: String = _
  var username: String = _
  var password: String = _
  var port: Int = _

  var mailNotifier: DefaultMailNotifier = _
  var entityDao: EntityDao = _
  var sessionFactory: SessionFactory = _

  override def init(): Unit = {
    if (null != host && null != username && null != password) {
      val mailSender = JavaMailSender.smtp(host, username, password, port)
      mailNotifier = new DefaultMailNotifier(mailSender, username)
      // 08:00,12:00
      val times = Set(LocalTime.of(7, 0), LocalTime.of(11, 0))
      MailDaemon.start("ERP Mail Notifier", this, times)
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
    val config = new Configuration(Configuration.VERSION_2_3_24)
    config.setEncoding(config.getLocale, "UTF-8")
    val wrapper = new BeangleObjectWrapper()
    wrapper.setUseCache(false)
    config.setObjectWrapper(wrapper)
    config.setTemplateLoader(new BeangleClassTemplateLoader())
    config.setCacheStorage(new StrongCacheStorage())
    config.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX)
    config.setSharedVariable("include_if_exists", new IncludeIfExistsModel)
    // Disable auto imports and includes
    config.setAutoImports(new ju.HashMap(0))
    config.setAutoIncludes(new ju.ArrayList(0))

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
        val template = getTemplate(config, "/com/yushanginfo/erp/mes/wo/mail/body.ftl")
        val sw = new StringWriter()
        for (am <- kv._2) {
          val model = new SimpleHash(wrapper)
          model.put("workOrders", orders)
          model.put("ems", Ems)
          model.put("assessGroup", gf._1)
          model.put("factory", gf._2)
          model.put("user", am.user)
          template.process(model, sw)
          val body = sw.toString
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

  /**
   * Load template in hierarchical path
   */
  private def getTemplate(config: Configuration, templateName: String): Template = {
    try {
      config.getTemplate(templateName, "UTF-8")
    } catch {
      case e: ParseException => throw e
      case e: IOException =>
        logger.error(s"Couldn't load template '$templateName',loader is ${config.getTemplateLoader.getClass}")
        throw Throwables.propagate(e)
    }
  }
}
