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

import com.yushanginfo.erp.mes.model.{Product, ProductMaterialItem}
import com.yushanginfo.erp.mes.base.helper.ProductMaterialItemImportHelper
import org.beangle.data.transfer.excel.ExcelSchema
import org.beangle.data.transfer.importer.ImportSetting
import org.beangle.data.transfer.importer.listener.ForeignerListener
import org.beangle.webmvc.api.annotation.{ignore, response}
import org.beangle.webmvc.api.view.Stream
import org.beangle.webmvc.entity.action.RestfulAction

class ProductBomAction extends RestfulAction[ProductMaterialItem] {

  @ignore
  protected override def simpleEntityName: String = {
    "item"
  }

  override protected def editSetting(entity: ProductMaterialItem): Unit = {
    put("product", entityDao.find(classOf[Product], entity.product.id))
    super.editSetting(entity)
  }

  @response
  def downloadTemplate(): Any = {
    val schema = new ExcelSchema()
    val sheet = schema.createScheet("数据模板")
    sheet.title("产品材料清单模板")
    sheet.remark("特别说明：\n1、不可改变本表格的行列结构以及批注，否则将会导入失败！\n2、必须按照规格说明的格式填写。\n3、可以多次导入，重复的信息会被新数据更新覆盖。\n4、保存的excel文件名称可以自定。")
    sheet.add("主件品号", "productMaterialItem.product.code").required().length(15).required()
    sheet.add("序号", "productMaterialItem.indexno").required().length(5).required()
    sheet.add("元件品号", "productMaterialItem.material.code").required().length(15).required()
    sheet.add("组成用量", "productMaterialItem.amount").required()

    val os = new ByteArrayOutputStream()
    schema.generate(os)
    Stream(new ByteArrayInputStream(os.toByteArray), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "产品材料清单模板.xlsx")
  }

  protected override def configImport(setting: ImportSetting): Unit = {
    val fl = new ForeignerListener(entityDao)
    fl.addForeigerKey("code")
    setting.listeners = List(fl, new ProductMaterialItemImportHelper(entityDao))
  }
}
