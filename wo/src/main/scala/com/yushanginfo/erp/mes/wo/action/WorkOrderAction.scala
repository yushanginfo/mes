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
package com.yushanginfo.erp.mes.wo.action

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.time.Instant

import com.yushanginfo.erp.base.model.Factory
import com.yushanginfo.erp.mes.model.{SalesOrderType, WorkOrder, WorkOrderType}
import com.yushanginfo.erp.mes.sync.SyncServiceImpl
import com.yushanginfo.erp.mes.wo.helper.OrderImportHelper
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.transfer.excel.ExcelSchema
import org.beangle.data.transfer.importer.ImportSetting
import org.beangle.data.transfer.importer.listener.ForeignerListener
import org.beangle.ems.app.Ems
import org.beangle.webmvc.api.annotation.response
import org.beangle.webmvc.api.view.{Stream, View}
import org.beangle.webmvc.entity.action.RestfulAction

class WorkOrderAction extends RestfulAction[WorkOrder] {

  var syncService: SyncServiceImpl = _

  override protected def indexSetting(): Unit = {
    put("workOrderTypes", entityDao.getAll(classOf[WorkOrderType]))
    put("factories", entityDao.getAll(classOf[Factory]))
  }

  override def editSetting(entity: WorkOrder): Unit = {
    put("salesOrderTypes", entityDao.getAll(classOf[SalesOrderType]))
    put("workOrderTypes", entityDao.getAll(classOf[WorkOrderType]))
    put("factories", entityDao.getAll(classOf[Factory]))
    put("ems", Ems)
    super.editSetting(entity)
  }

  override def saveAndRedirect(entity: WorkOrder): View = {
    if (null == entity.createdAt) {
      entity.createdAt = Instant.now
    }
    super.saveAndRedirect(entity)
  }

  def sync(): View = {
    val rs = syncService.sync()
    put("rs", rs)
    forward()
  }

  @response
  def downloadTemplate(): Any = {
    val salesOrderTypes = entityDao.search(OqlBuilder.from(classOf[SalesOrderType], "p").orderBy("p.code")).map(_.name)
    val workOrderTypes = entityDao.search(OqlBuilder.from(classOf[WorkOrderType], "p").orderBy("p.code")).map(_.name)
    val factories = entityDao.search(OqlBuilder.from(classOf[Factory], "p").orderBy("p.code")).map(_.name)

    val schema = new ExcelSchema()
    val sheet = schema.createScheet("数据模板")
    sheet.title("工单信息模板")
    sheet.remark("特别说明：\n1、不可改变本表格的行列结构以及批注，否则将会导入失败！\n2、必须按照规格说明的格式填写。\n3、可以多次导入，重复的信息会被新数据更新覆盖。\n4、保存的excel文件名称可以自定。")
    sheet.add("顾客代码", "workOrder.customer.code").length(5).required().remark("≤5位")
    sheet.add("订单编号", "workOrder.salesOrderNo").length(30).required().remark("≤30位")
    sheet.add("公司图号", "workOrder.product.specification").length(100).remark("≤100位")
    sheet.add("订单类型", "workOrder.salesOrderType.code").ref(salesOrderTypes)

    sheet.add("客户交期", "workOrder.deadline").date().required()
    sheet.add("计划交期", "workOrder.plannedEndOn").date().required()
    sheet.add("生产批号", "workOrder.batchNum").required()
    sheet.add("计划数量", "workOrder.amount").required()

    sheet.add("工单单别", "workOrder.workOrderType.name").required().ref(workOrderTypes)
    sheet.add("厂区", "workOrder.factory.name").required().ref(factories)
    sheet.add("工艺路线编号", "technicScheme.indexno").remark("默认为编号中的第一个")

    val code = schema.createScheet("数据字典")
    code.add("订单类型").data(salesOrderTypes)
    code.add("工单单别").data(workOrderTypes)
    code.add("厂区信息").data(factories)
    val os = new ByteArrayOutputStream()
    schema.generate(os)
    Stream(new ByteArrayInputStream(os.toByteArray), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "工单模板.xlsx")
  }

  protected override def configImport(setting: ImportSetting): Unit = {
    val fl = new ForeignerListener(entityDao)
    fl.addForeigerKey("code")
    fl.addForeigerKey("name")
    fl.addForeigerKey("specification")
    setting.listeners = List(fl, new OrderImportHelper(entityDao))
  }
}
