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

import java.time.{Instant, LocalDate}

import com.yushanginfo.erp.mes.model.AssessGroup
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.data.transfer.importer.{ImportListener, ImportResult}

/**
 * @author chaostone
 */
class AssessGroupImportListener(entityDao: EntityDao) extends ImportListener {
  override def onItemFinish(tr: ImportResult): Unit = {
    val tg = tr.transfer.current.asInstanceOf[AssessGroup]
    tg.updatedAt = Instant.now
    entityDao.saveOrUpdate(tg)
  }

  /**
   * 开始转换单个项目
   */
  override def onItemStart(tr: ImportResult): Unit = {
    val query = OqlBuilder.from(classOf[AssessGroup], "tg")
    query.where("tg.code=:code", tr.transfer.curData.getOrElse("assessGroup.code", ""))
    val groups = entityDao.search(query)
    if (groups.size == 1) {
      tr.transfer.current = groups.head
    }
  }

}
