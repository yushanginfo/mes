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

package net.yushanginfo.erp.mes.base.helper

import java.time.Instant

import net.yushanginfo.erp.base.model.Customer
import org.beangle.data.dao.EntityDao
import org.beangle.data.transfer.importer.{ImportListener, ImportResult}

class CustomerImportHelper(entityDao: EntityDao) extends ImportListener {

  override def onItemFinish(tr: ImportResult): Unit = {
    val co = transfer.current.asInstanceOf[Customer]
    co.updatedAt = Instant.now
    entityDao.saveOrUpdate(co)
  }

  override def onStart(tr: ImportResult): Unit = {
  }

  override def onFinish(tr: ImportResult): Unit = {
  }

  override def onItemStart(tr: ImportResult): Unit = {
    transfer.curData.get("customer.code") foreach{code=>
      entityDao.findBy(classOf[Customer],"code",List(code)) foreach{ p=>
        transfer.current=p
      }
    }
  }
}
