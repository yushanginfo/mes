/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
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
package com.yushanginfo.erp.order.admin.web.helper

import java.time.Instant

import com.yushanginfo.erp.base.model.ProductMaterialItem
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.data.transfer.importer.{ImportListener, ImportResult}

class ProductMaterialItemImportHelper(entityDao: EntityDao) extends ImportListener {

  override def onItemFinish(tr: ImportResult): Unit = {
    val item = transfer.current.asInstanceOf[ProductMaterialItem]
    item.updatedAt = Instant.now
    if (null != item.product && null != item.material) {
      entityDao.saveOrUpdate(item)
    }
  }

  override def onStart(tr: ImportResult): Unit = {
  }

  override def onFinish(tr: ImportResult): Unit = {
  }

  override def onItemStart(tr: ImportResult): Unit = {
    for (pcode <- transfer.curData.get("productMaterialItem.product.code");
         indexno <- transfer.curData.get("productMaterialItem.indexno")) {

      val ptQuery = OqlBuilder.from(classOf[ProductMaterialItem], "pmi")
      ptQuery.where("pmi.product.code=:code", pcode)
      ptQuery.where("pmi.indexno=:indexno", indexno)

      entityDao.search(ptQuery).foreach { pmi =>
        transfer.current = pmi
      }
    }
  }
}
