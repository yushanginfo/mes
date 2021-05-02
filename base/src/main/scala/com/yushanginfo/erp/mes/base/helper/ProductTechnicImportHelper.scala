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
package com.yushanginfo.erp.mes.base.helper

import com.yushanginfo.erp.mes.model.{ProductTechnic, TechnicScheme}
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.data.transfer.importer.{ImportListener, ImportResult}

class ProductTechnicImportHelper(entityDao: EntityDao) extends ImportListener {

  override def onItemFinish(tr: ImportResult): Unit = {
    val p = transfer.current.asInstanceOf[ProductTechnic]
    if ("2" == transfer.curData.get("productTechnic.internal")) {
      p.internal = false
    }
    entityDao.saveOrUpdate(p)
  }

  override def onStart(tr: ImportResult): Unit = {
  }

  override def onFinish(tr: ImportResult): Unit = {
  }

  override def onItemStart(tr: ImportResult): Unit = {
    if ("1" == transfer.curData.get("productTechnic.internal")) {
      transfer.curData.remove("productTechnic.machine.code")
    }
    for (pcode <- transfer.curData.get("scheme.product.code"); indexno <- transfer.curData.get("scheme.indexno");
         technicCode <- transfer.curData.get("productTechnic.technic.code")) {

      val ptQuery = OqlBuilder.from(classOf[ProductTechnic], "pt")
      ptQuery.where("pt.scheme.product.code=:code", pcode)
      ptQuery.where("pt.scheme.indexno=:indexno", indexno)
      ptQuery.where("pt.technic.code=:technicCode", technicCode)

      entityDao.search(ptQuery).headOption match {
        case Some(pt) => transfer.current = pt
        case None =>
          val builder = OqlBuilder.from(classOf[TechnicScheme], "s")
          builder.where("s.product.code=:code", pcode)
          builder.where("s.indexno=:indexno", indexno)

          entityDao.search(builder).headOption match {
            case Some(scheme) => transfer.current.asInstanceOf[ProductTechnic].scheme = scheme
            case None => tr.addFailure(s"找不到品号${pcode} 编号为${indexno}工艺路线", indexno)
          }
      }
    }
  }
}
