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

import java.time.{LocalDate, LocalTime}
import java.util.{Timer, TimerTask}

import org.beangle.commons.logging.Logging

import scala.collection.mutable

object MailDaemon extends Logging {
  def start(name: String, mailNotifier: MailNotifier,times: Set[LocalTime]): Unit = {
    logger.info(s"Starting $name Daemon")
    val daemon = new MailDaemon(mailNotifier,times)
    new Timer(s"$name Daemon", true).schedule(daemon,
      new java.util.Date(System.currentTimeMillis + 5000), 60 * 1000)
  }
}

class MailDaemon(mailNotifier: MailNotifier, times: Set[LocalTime]) extends TimerTask with Logging {
  private val sended = new mutable.HashMap[LocalDate, List[LocalTime]]

  override def run(): Unit = {
    try {
      val today = LocalDate.now()
      val now = LocalTime.now()
      sended.remove(today.minusDays(1))
      sended.get(today) match {
        case Some(l) =>
          if (times.exists(_.getHour==now.getHour) && !l.exists(_.getHour == now.getHour)) {
            logger.info(mailNotifier.sendMail())
            sended.put(today, now :: l)
          }
        case None =>
          if(times.exists(_.getHour==now.getHour) ){
            logger.info(mailNotifier.sendMail())
            sended.put(today, List(now))
          }
      }
    } catch {
      case e: Throwable => e.printStackTrace()
    }
  }
}