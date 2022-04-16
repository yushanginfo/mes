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

package com.yushanginfo.erp.mes.base.action

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import com.yushanginfo.erp.mes.model.{Machine, Technic}
import com.yushanginfo.erp.mes.base.helper.MachineImportHelper
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.excel.schema.ExcelSchema
import org.beangle.data.transfer.importer.ImportSetting
import org.beangle.web.action.annotation.{mapping, response}
import org.beangle.web.action.view.{Stream, View}
import org.beangle.webmvc.support.action.RestfulAction

class MachineAction extends RestfulAction[Machine] {

  @response
  def downloadTemplate(): Any = {
    val schema = new ExcelSchema()
    val sheet = schema.createScheet("数据模板")
    sheet.title("加工中心信息模板")
    sheet.remark("特别说明：\n1、不可改变本表格的行列结构以及批注，否则将会导入失败！\n2、必须按照规格说明的格式填写。\n3、可以多次导入，重复的信息会被新数据更新覆盖。\n4、保存的excel文件名称可以自定。")
    sheet.add("编码", "machine.code").length(15).required().remark("≤15位")
    sheet.add("名称", "machine.name").length(100).required().remark("≤100位")
    val os = new ByteArrayOutputStream()
    schema.generate(os)
    Stream(new ByteArrayInputStream(os.toByteArray), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "加工中心模板.xlsx")
  }

  protected override def configImport(setting: ImportSetting): Unit = {
    setting.listeners = List(new MachineImportHelper(entityDao))
  }

  @mapping(value = "{id}")
  override def info(id: String): View = {
    val builder = OqlBuilder.from(classOf[Technic], "t").where("t.machine.id=:machineid", id.toInt)
    builder.orderBy("t.code")
    val technics = entityDao.search(builder)
    put("technics", technics)
    super.info(id)
  }
}
