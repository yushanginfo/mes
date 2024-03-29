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

import net.yushanginfo.erp.base.model.Factory
import net.yushanginfo.erp.mes.model.Reviewer
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.RestfulAction

class ReviewerAction extends RestfulAction[Reviewer] {

  override protected def editSetting(entity: Reviewer): Unit = {
    put("factories", entityDao.getAll(classOf[Factory]))
    put("rounds", List(1, 2, 3))
    super.editSetting(entity)
  }

  override protected def saveAndRedirect(entity: Reviewer): View = {
    val factoryIds = intIds("factory")
    entity.factories.clear()
    entity.factories ++= entityDao.find(classOf[Factory], factoryIds.toSeq)
    entity.rounds.clear()
    entity.rounds ++= getAll("round", classOf[Int])
    super.saveAndRedirect(entity)
  }
}
