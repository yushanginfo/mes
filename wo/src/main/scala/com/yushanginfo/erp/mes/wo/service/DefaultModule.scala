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

import java.io.FileInputStream

import org.beangle.cdi.PropertySource
import org.beangle.cdi.bind.BindModule
import org.beangle.commons.collection.Collections
import org.beangle.ems.app.EmsApp

class DefaultModule extends BindModule  with PropertySource{

  override protected def binding(): Unit = {
    bind(classOf[MailNotifier])
      .property("host", $("smtp.host"))
      .property("username", $("smtp.username"))
      .property("password", $("smtp.password"))
      .property("port", $("smtp.port")).lazyInit(false)
  }

  override def properties: collection.Map[String, String] = {
    val datas = Collections.newMap[String, String]
    EmsApp.getAppFile foreach { file =>
      val is = new FileInputStream(file)
      val app = scala.xml.XML.load(is)
      (app \\ "smtp") foreach { e =>
        datas += ("smtp.host" -> (e \\ "host").text.trim)
        datas += ("smtp.username" -> (e \\ "username").text.trim)
        datas += ("smtp.password" -> (e \\ "password").text.trim)
        datas += ("smtp.port" -> (e \\ "port").text.trim)
      }
      is.close()
    }
    datas.toMap
  }
}
