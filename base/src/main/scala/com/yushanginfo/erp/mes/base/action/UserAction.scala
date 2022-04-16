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

import com.yushanginfo.erp.base.model.{Department, Factory, User}
import com.yushanginfo.erp.mes.base.helper.UserImportHelper
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.excel.schema.ExcelSchema
import org.beangle.data.transfer.importer.ImportSetting
import org.beangle.data.transfer.importer.listener.ForeignerListener
import org.beangle.web.action.annotation.response
import org.beangle.web.action.view.Stream
import org.beangle.webmvc.support.action.RestfulAction

class UserAction extends RestfulAction[User] {

  override protected def editSetting(entity: User): Unit = {
    put("departments", entityDao.getAll(classOf[Department]))
    put("factories", entityDao.getAll(classOf[Factory]))
  }

  override protected def indexSetting(): Unit = {
    put("departments", entityDao.getAll(classOf[Department]))
    put("factories", entityDao.getAll(classOf[Factory]))
  }

  @response
  def downloadTemplate(): Any = {
    val departs = entityDao.search(OqlBuilder.from(classOf[Department], "p").orderBy("p.code")).map(_.code)
    val factories = entityDao.search(OqlBuilder.from(classOf[Factory], "p").orderBy("p.code")).map(_.code)

    val schema = new ExcelSchema()
    val sheet = schema.createScheet("数据模板")
    sheet.title("用户信息模板")
    sheet.remark("特别说明：\n1、不可改变本表格的行列结构以及批注，否则将会导入失败！\n2、必须按照规格说明的格式填写。\n3、可以多次导入，重复的信息会被新数据更新覆盖。\n4、保存的excel文件名称可以自定。")
    sheet.add("人员编码", "user.code").length(15).required().remark("≤10位")
    sheet.add("姓名", "user.name").length(100).required().remark("≤100位")
    sheet.add("电子邮件", "user.email").length(100).remark("≤100位")
    sheet.add("移动电话", "user.mobile").length(18).remark("≤18位")
    sheet.add("所在部门", "user.department.code").ref(departs).required()
    sheet.add("所在厂区", "user.factory.code").ref(factories)

    val code = schema.createScheet("数据字典")
    code.add("部门").data(departs)
    code.add("厂区").data(factories)

    val os = new ByteArrayOutputStream()
    schema.generate(os)
    Stream(new ByteArrayInputStream(os.toByteArray), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "用户模板.xlsx")
  }

  protected override def configImport(setting: ImportSetting): Unit = {
    val fl = new ForeignerListener(entityDao)
    fl.addForeigerKey("name")
    setting.listeners = List(fl, new UserImportHelper(entityDao))
  }
}
