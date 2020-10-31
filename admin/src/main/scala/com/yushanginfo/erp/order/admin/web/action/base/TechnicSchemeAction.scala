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
package com.yushanginfo.erp.order.admin.web.action.base

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import com.yushanginfo.erp.base.model.{Product, Technic, TechnicScheme}
import com.yushanginfo.erp.order.admin.web.helper.TechnicSchemeImportHelper
import org.beangle.commons.collection.Properties
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.transfer.excel.ExcelSchema
import org.beangle.data.transfer.importer.ImportSetting
import org.beangle.data.transfer.importer.listener.ForeignerListener
import org.beangle.webmvc.api.annotation.response
import org.beangle.webmvc.api.view.{Stream, View}
import org.beangle.webmvc.entity.action.RestfulAction

class TechnicSchemeAction extends RestfulAction[TechnicScheme] {

  override protected def editSetting(entity: TechnicScheme): Unit = {
    put("technics", entityDao.getAll(classOf[Technic]))
  }

  override def saveAndRedirect(entity: TechnicScheme): View = {
    entity.technics.clear()
    val technicIds = getAll("technicIds", classOf[Int])
    val newTechnics = entityDao.find(classOf[Technic], technicIds)
    val removed = entity.technics.filter(
      x => !newTechnics.contains(x)
    )
    entity.technics.subtractAll(removed)
    //保证保存顺序
    technicIds.foreach(id => {
      entity.technics ++= entityDao.find(classOf[Technic], id)
    })
    super.saveAndRedirect(entity)
  }

  @response
  def downloadTemplate(): Any = {
    val schema = new ExcelSchema()
    val sheet = schema.createScheet("数据模板")
    sheet.title("工艺路线信息模板")
    sheet.remark("特别说明：\n1、不可改变本表格的行列结构以及批注，否则将会导入失败！\n2、必须按照规格说明的格式填写。\n3、可以多次导入，重复的信息会被新数据更新覆盖。\n4、保存的excel文件名称可以自定。")
    sheet.add("产品代码", "technicScheme.product.code").length(15).required()
    sheet.add("路线编号", "technicScheme.indexno").length(100).required().remark("≤100位")
    sheet.add("路线名称", "technicScheme.name").length(100).required().remark("≤100位")

    val os = new ByteArrayOutputStream()
    schema.generate(os)
    Stream(new ByteArrayInputStream(os.toByteArray), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "工艺路线模板.xlsx")
  }

  protected override def configImport(setting: ImportSetting): Unit = {
    val fl = new ForeignerListener(entityDao)
    fl.addForeigerKey("code")
    setting.listeners = List(fl, new TechnicSchemeImportHelper(entityDao))
  }

  @response
  def loadProduct: Seq[Properties] = {
    val query = OqlBuilder.from(classOf[Product], "es")
    query.where("es.code like :code", "%" + get("q", "") + "%")
    query.limit(1, 10)
    entityDao.search(query).map { es =>
      val p = new Properties()
      p.put("value", es.id.toString)
      p.put("text", s"${es.code} ${es.name}")
      p
    }
  }
}
