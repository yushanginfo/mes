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
package com.yushanginfo.erp.order.admin.web.action.contract

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import com.yushanginfo.erp.base.model.{Department, Factory, Machine, Product, Supplier}
import com.yushanginfo.erp.order.admin.web.helper.{OrderImportHelper, TechnicImportHelper}
import com.yushanginfo.erp.order.model.{OrderType, SalesOrder}
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.transfer.excel.ExcelSchema
import org.beangle.data.transfer.importer.ImportSetting
import org.beangle.data.transfer.importer.listener.ForeignerListener
import org.beangle.webmvc.api.annotation.response
import org.beangle.webmvc.api.view.Stream
import org.beangle.webmvc.entity.action.RestfulAction

class SalesOrderAction extends RestfulAction[SalesOrder] {

  override protected def indexSetting(): Unit = {
    put("orderTypes", entityDao.getAll(classOf[OrderType]))
    put("factories", entityDao.getAll(classOf[Factory]))
  }

  override def editSetting(entity: SalesOrder): Unit = {
    put("orderTypes", entityDao.getAll(classOf[OrderType]))
    put("factories", entityDao.getAll(classOf[Factory]))
    super.editSetting(entity)
  }

  @response
  def downloadTemplate(): Any = {
    val orderTypes = entityDao.search(OqlBuilder.from(classOf[OrderType], "p").orderBy("p.code")).map(_.code)
    val factories = entityDao.search(OqlBuilder.from(classOf[Factory], "p").orderBy("p.code")).map(_.code)

    val schema = new ExcelSchema()
    val sheet = schema.createScheet("数据模板")
    sheet.title("订单信息模板")
    sheet.remark("特别说明：\n1、不可改变本表格的行列结构以及批注，否则将会导入失败！\n2、必须按照规格说明的格式填写。\n3、可以多次导入，重复的信息会被新数据更新覆盖。\n4、保存的excel文件名称可以自定。")
    sheet.add("客户编号", "salesOrder.customer.code").length(5).required().remark("≤5位")
    sheet.add("订单编号", "salesOrder.code").length(30).required().remark("≤30位")
    sheet.add("品号", "salesOrder.product.code").length(100).remark("≤100位")
    sheet.add("订单类型", "salesOrder.orderType.code").ref(orderTypes)
    sheet.add("生产批号", "salesOrder.batchNum").required()
    sheet.add("计划数量", "salesOrder.amount").required()
    sheet.add("客户交期", "salesOrder.deadline").date().required()
    sheet.add("计划交期", "salesOrder.plannedEndOn").date().required()
    sheet.add("工艺路线编号", "technicScheme.indexno").remark("默认为编号中的第一个")
    sheet.add("厂区", "salesOrder.factory.code").required().ref(factories)

    val code = schema.createScheet("数据字典")
    code.add("订单类型").data(orderTypes)
    code.add("厂区信息").data(factories)
    val os = new ByteArrayOutputStream()
    schema.generate(os)
    Stream(new ByteArrayInputStream(os.toByteArray), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "订单模板.xlsx")
  }

  protected override def configImport(setting: ImportSetting): Unit = {
    val fl = new ForeignerListener(entityDao)
    fl.addForeigerKey("code")
    setting.listeners = List(fl, new OrderImportHelper(entityDao))
  }
}
