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

package com.yushanginfo.erp.mes.base.helper

import java.time.Instant

import com.yushanginfo.erp.mes.model.TechnicScheme
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.data.transfer.importer.{ImportListener, ImportResult}

class TechnicSchemeImportHelper(entityDao: EntityDao) extends ImportListener {

  override def onItemFinish(tr: ImportResult): Unit = {
    val p = transfer.current.asInstanceOf[TechnicScheme]
    p.updatedAt = Instant.now
    if (Strings.isEmpty(p.name)) {
      p.name = p.indexno
    }
    if (null != p.product) {
      entityDao.saveOrUpdate(p)
    }
  }

  override def onStart(tr: ImportResult): Unit = {

  }

  override def onFinish(tr: ImportResult): Unit = {
  }

  override def onItemStart(tr: ImportResult): Unit = {
    for (pcode <- transfer.curData.get("technicScheme.product.code"); indexno <- transfer.curData.get("technicScheme.indexno")) {
      val builder = OqlBuilder.from(classOf[TechnicScheme], "s")
      builder.where("s.product.code=:code", pcode)
      builder.where("s.indexno=:indexno", indexno)
      entityDao.search(builder) foreach { p =>
        transfer.current = p
      }
    }
  }
}
