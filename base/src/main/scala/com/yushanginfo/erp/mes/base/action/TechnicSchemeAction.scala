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
package com.yushanginfo.erp.mes.base.action

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import com.yushanginfo.erp.mes.model.{ProductTechnic, Technic, TechnicScheme}
import com.yushanginfo.erp.mes.base.helper.TechnicSchemeImportHelper
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

  override def saveAndRedirect(scheme: TechnicScheme): View = {
    scheme.technics.clear()
    val technicIds = getAll("technicIds", classOf[Int])
    val newTechnics = entityDao.find(classOf[Technic], technicIds)
    val removed = scheme.technics.filter(
      x => !newTechnics.contains(x.technic)
    )
    scheme.technics.subtractAll(removed)
    //保证保存顺序
    var i = 0
    technicIds.foreach(id => {
      scheme.technics.find(_.technic.id == id) match {
        case None =>
          val pt = new ProductTechnic(i.toString, scheme, entityDao.get(classOf[Technic], id))
          scheme.technics += pt
        case Some(t) =>
          t.indexno = i.toString
      }
      i += 1
    })
    super.saveAndRedirect(scheme)
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

}
