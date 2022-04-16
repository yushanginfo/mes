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

import com.yushanginfo.erp.base.model.Supplier
import com.yushanginfo.erp.mes.model.{Machine, ProductTechnic}
import com.yushanginfo.erp.mes.base.helper.ProductTechnicImportHelper
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.excel.schema.ExcelSchema
import org.beangle.data.transfer.importer.ImportSetting
import org.beangle.data.transfer.importer.listener.ForeignerListener
import org.beangle.web.action.annotation.response
import org.beangle.web.action.view.Stream
import org.beangle.webmvc.support.action.RestfulAction

class ProductTechnicAction extends RestfulAction[ProductTechnic] {

  override protected def editSetting(entity: ProductTechnic): Unit = {
    put("machines", entityDao.getAll(classOf[Machine]))
    put("suppliers", entityDao.getAll(classOf[Supplier]))
    if (!entity.persisted) {
      entity.internal = true
    }
  }

  @response
  def downloadTemplate(): Any = {
    val machines = entityDao.search(OqlBuilder.from(classOf[Machine], "p").orderBy("p.code")).map(_.code)
    val suppliers = entityDao.search(OqlBuilder.from(classOf[Supplier], "p").orderBy("p.code")).map(_.code)

    val schema = new ExcelSchema()
    val sheet = schema.createScheet("数据模板")
    sheet.title("产品工艺模板")
    sheet.remark("特别说明：\n1、不可改变本表格的行列结构以及批注，否则将会导入失败！\n2、必须按照规格说明的格式填写。\n3、可以多次导入，重复的信息会被新数据更新覆盖。\n4、保存的excel文件名称可以自定。")
    sheet.add("产品代码", "scheme.product.code").length(15).required()
    sheet.add("工艺路线编号", "scheme.indexno").length(15).required()
    sheet.add("加工顺序", "productTechnic.indexno").length(100).required().remark("≤100位")
    sheet.add("工艺编号", "productTechnic.technic.code").length(100).required().remark("≤100位")
    sheet.add("性质", "productTechnic.internal").required().remark("1厂内,2委外")
    sheet.add("加工中心编号", "productTechnic.machine.code").ref(machines)
    sheet.add("供应商编号", "productTechnic.supplier.code").ref(suppliers)
    sheet.add("工艺说明", "productTechnic.description").length(100).remark("≤100位")

    val code = schema.createScheet("数据字典")
    code.add("加工中心编号").data(machines)
    code.add("供应商").data(suppliers)

    val os = new ByteArrayOutputStream()
    schema.generate(os)
    Stream(new ByteArrayInputStream(os.toByteArray), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "产品工艺模板.xlsx")
  }

  protected override def configImport(setting: ImportSetting): Unit = {
    val fl = new ForeignerListener(entityDao)
    fl.addForeigerKey("code")
    setting.listeners = List(fl, new ProductTechnicImportHelper(entityDao))
  }
}
