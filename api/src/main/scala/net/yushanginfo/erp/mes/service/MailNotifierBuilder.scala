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
