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

import com.yushanginfo.erp.base.model.User
import com.yushanginfo.erp.mes.base.helper.AssessGroupImportListener
import com.yushanginfo.erp.mes.model.AssessGroup
import org.beangle.data.transfer.excel.ExcelSchema
import org.beangle.data.transfer.importer.ImportSetting
import org.beangle.data.transfer.importer.listener.ForeignerListener
import org.beangle.ems.app.Ems
import org.beangle.webmvc.api.annotation.response
import org.beangle.webmvc.api.view.{Stream, View}
import org.beangle.webmvc.entity.action.RestfulAction

class AssessGroupAction extends RestfulAction[AssessGroup] {

  protected override def indexSetting(): Unit = {
  }

  protected override def editSetting(g: AssessGroup): Unit = {
  }

  protected override def saveAndRedirect(tg: AssessGroup): View = {
    val newMembers = entityDao.find(classOf[User], longIds("member"))
    val removed = tg.members filter { x => !newMembers.contains(x) }
    tg.members.subtractAll(removed)
    newMembers foreach { l =>
      if (!tg.members.contains(l))
        tg.members += l
    }
    super.saveAndRedirect(tg)
  }

  @response
  def downloadTemplate(): Any = {
    val schema = new ExcelSchema()
    val sheet = schema.createScheet("数据模板")
    sheet.title("评审组信息模板")
    sheet.remark("特别说明：\n1、不可改变本表格的行列结构以及批注，否则将会导入失败！\n2、必须按照规格说明的格式填写。\n3、可以多次导入，重复的信息会被新数据更新覆盖。\n4、保存的excel文件名称可以自定。")
    sheet.add("评审组代码", "assessGroup.code").length(10).required().remark("≤10位")
    sheet.add("评审组名称", "assessGroup.name").length(100).required()
    sheet.add("负责人工号", "assessGroup.director.code")

    val os = new ByteArrayOutputStream()
    schema.generate(os)
    Stream(new ByteArrayInputStream(os.toByteArray), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "评审组模板.xlsx")
  }

  protected override def configImport(setting: ImportSetting): Unit = {
    val fk = new ForeignerListener(entityDao)
    fk.addForeigerKey("code")
    setting.listeners = List(fk, new AssessGroupImportListener(entityDao))
  }

}
