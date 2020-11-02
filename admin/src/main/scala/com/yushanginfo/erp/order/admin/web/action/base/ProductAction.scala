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

import com.yushanginfo.erp.base.model.{MaterialType, MeasurementUnit, Product}
import com.yushanginfo.erp.order.admin.web.helper.ProductImportHelper
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.transfer.excel.ExcelSchema
import org.beangle.data.transfer.importer.ImportSetting
import org.beangle.data.transfer.importer.listener.ForeignerListener
import org.beangle.webmvc.api.annotation.response
import org.beangle.webmvc.api.view.{Stream, View}
import org.beangle.webmvc.entity.action.RestfulAction

class ProductAction extends RestfulAction[Product] {

  override protected def indexSetting(): Unit = {
    put("materialTypes", entityDao.getAll(classOf[MaterialType]))
  }

  override protected def editSetting(entity: Product): Unit = {
    put("materialTypes", entityDao.getAll(classOf[MaterialType]))
    put("units", entityDao.getAll(classOf[MeasurementUnit]))
  }

  protected override def getQueryBuilder: OqlBuilder[Product] = {
    val query = super.getQueryBuilder
    getInt("technicSchemeNum") foreach { technicSchemeNum =>
      technicSchemeNum match {
        case 0 | 1 => query.where("size(product.technicSchemes)=" + technicSchemeNum)
        case 2 => query.where("size(product.technicSchemes)>=2")
      }
    }
    query
  }

  override def saveAndRedirect(entity: Product): View = {
    //		entity.technics.clear()
    //		val technicIds = getAll("technicIds", classOf[Int])
    //		val newTechnics = entityDao.find(classOf[Technic], technicIds)
    //		val removed = entity.technics.filter(
    //			x => !newTechnics.contains(x)
    //		)
    //		entity.technics.subtractAll(removed)
    //		//保证保存顺序
    //		technicIds.foreach(id => {
    //			entity.technics ++= entityDao.find(classOf[Technic], id)
    //		})
    super.saveAndRedirect(entity)
  }

  @response
  def downloadTemplate(): Any = {
    val mus = entityDao.search(OqlBuilder.from(classOf[MeasurementUnit], "p").orderBy("p.code")).map(_.code)
    val materialTypes = entityDao.search(OqlBuilder.from(classOf[MaterialType], "p").orderBy("p.code")).map(_.code)

    val schema = new ExcelSchema()
    val sheet = schema.createScheet("数据模板")
    sheet.title("产品信息模板")
    sheet.remark("特别说明：\n1、不可改变本表格的行列结构以及批注，否则将会导入失败！\n2、必须按照规格说明的格式填写。\n3、可以多次导入，重复的信息会被新数据更新覆盖。\n4、保存的excel文件名称可以自定。")
    sheet.add("品号", "product.code").length(15).required().remark("≤10位")
    sheet.add("品名", "product.name").length(100).required().remark("≤100位")
    sheet.add("规格", "product.specification").length(100).remark("≤100位")
    sheet.add("品号类别", "product.materialType.code").required().ref(materialTypes)
    sheet.add("单位", "product.unit.code").required().ref(mus)

    val code = schema.createScheet("数据字典")
    code.add("计量单位").data(mus)
    code.add("品号类别").data(materialTypes)

    val os = new ByteArrayOutputStream()
    schema.generate(os)
    Stream(new ByteArrayInputStream(os.toByteArray), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "产品模板.xlsx")
  }

  protected override def configImport(setting: ImportSetting): Unit = {
    val fl = new ForeignerListener(entityDao)
    fl.addForeigerKey("code")
    setting.listeners = List(fl, new ProductImportHelper(entityDao))
  }
}
