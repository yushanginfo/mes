package com.yushanginfo.erp.mes.service

import org.beangle.notify.SendingObserver
import org.beangle.notify.mail.{DefaultMailNotifier, JavaMailSender, MailMessage}

class MailNotifierBuilder {

  var host: String = _
  var username: String = _
  var password: String = _
  var port: Int = _

  var result: DefaultMailNotifier = _

  def builder(): Option[DefaultMailNotifier] = {
    if (null != result) {
      Some(result)
    } else {
      if (null != host && null != username && null != password) {
        val mailSender = JavaMailSender.smtp(host, username, password, port)
        Some(new DefaultMailNotifier(mailSender, username))
      } else {
        None
      }
    }
  }
}
