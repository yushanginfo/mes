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

package net.yushanginfo.erp.mes.base.action

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import net.yushanginfo.erp.base.model.{Department, Supplier}
import net.yushanginfo.erp.mes.base.helper.TechnicImportHelper
import net.yushanginfo.erp.mes.model.{AssessGroup, Machine, Technic}
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.excel.schema.ExcelSchema
import org.beangle.data.transfer.importer.ImportSetting
import org.beangle.data.transfer.importer.listener.ForeignerListener
import org.beangle.web.action.annotation.response
import org.beangle.web.action.view.{Stream, View}
import org.beangle.webmvc.support.action.{ExportSupport, ImportSupport, RestfulAction}

class TechnicAction extends RestfulAction[Technic], ExportSupport[Technic], ImportSupport[Technic] {

  override protected def editSetting(entity: Technic): Unit = {
    put("groups", entityDao.getAll(classOf[AssessGroup]))
    put("machines", entityDao.getAll(classOf[Machine]))
    put("suppliers", entityDao.getAll(classOf[Supplier]))
  }

  override def search(): View = {
    put("groups", entityDao.getAll(classOf[AssessGroup]))
    super.search()
  }

  def batchUpdateGroup(): View = {
    val technics = entityDao.find(classOf[Technic], getIntIds("technic"))
    val group = entityDao.get(classOf[AssessGroup], getLongId("group"))
    technics.foreach { technic =>
      technic.assessGroup = Some(group)
    }
    entityDao.saveOrUpdate(technics)
    redirect("search", "info.save.success")
  }

  @response
  def downloadTemplate(): Any = {
    val machines = entityDao.search(OqlBuilder.from(classOf[Machine], "p").orderBy("p.code")).map(_.code)
    val suppliers = entityDao.search(OqlBuilder.from(classOf[Supplier], "p").orderBy("p.code")).map(_.code)
    val departs = entityDao.search(OqlBuilder.from(classOf[Department], "p").orderBy("p.code")).map(_.code)

    val schema = new ExcelSchema()
    val sheet = schema.createScheet("数据模板")
    sheet.title("工艺信息模板")
    sheet.remark("特别说明：\n1、不可改变本表格的行列结构以及批注，否则将会导入失败！\n2、必须按照规格说明的格式填写。\n3、可以多次导入，重复的信息会被新数据更新覆盖。\n4、保存的excel文件名称可以自定。")
    sheet.add("编码", "technic.code").length(15).required().remark("≤10位")
    sheet.add("名称", "technic.name").length(100).required().remark("≤100位")
    sheet.add("说明", "technic.description").length(100).remark("≤100位")
    sheet.add("性质", "technic.internal").required().bool()
    sheet.add("加工中心编号", "technic.machine.code").ref(machines)
    sheet.add("供应商编号", "technic.supplier.code").ref(suppliers)
    sheet.add("部门编号", "technic.depart.code").ref(departs).required()
    sheet.add("默认需要的天数", "technic.duration").decimal(0,100).remark("当工艺无需人工评审时填写")

    val code = schema.createScheet("数据字典")
    code.add("加工中心编号").data(machines)
    code.add("部门编号").data(departs)
    code.add("供应商").data(suppliers)
    val os = new ByteArrayOutputStream()
    schema.generate(os)
    Stream(new ByteArrayInputStream(os.toByteArray), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "工艺模板.xlsx")
  }

  protected override def configImport(setting: ImportSetting): Unit = {
    val fl = new ForeignerListener(entityDao)
    fl.addForeigerKey("code")
    setting.listeners = List(fl, new TechnicImportHelper(entityDao))
  }
}
