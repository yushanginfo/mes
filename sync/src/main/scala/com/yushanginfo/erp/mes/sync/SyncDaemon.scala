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
package com.yushanginfo.erp.mes.sync

import org.beangle.commons.logging.Logging

import java.util.{Calendar, Timer, TimerTask}

object SyncDaemon extends Logging {
  def start(name: String, intervalHours: Int, syncService: SyncService): Unit = {
    logger.info(s"Starting $name Daemon,Running within every $intervalHours hours.")
    val daemon = new SyncDaemon(syncService)
    new Timer(s"$name Daemon", true).schedule(daemon,
      new java.util.Date(System.currentTimeMillis + 5000), intervalHours * 3600 * 1000)
  }
}

class SyncDaemon(syncService: SyncService) extends TimerTask with Logging {
  override def run(): Unit = {
    try {
      syncService.sync()
    } catch {
      case e: Throwable => e.printStackTrace()
    }
  }
}
