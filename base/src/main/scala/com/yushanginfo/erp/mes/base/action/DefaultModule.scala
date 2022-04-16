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

import org.beangle.cdi.bind.BindModule

class DefaultModule extends BindModule {
  override protected def binding(): Unit = {
    bind(classOf[UserAction], classOf[DepartmentAction], classOf[FactoryAction], classOf[TechnicAction], classOf[CustomerAction])
    bind(classOf[ProductAction], classOf[MaterialAction])
    bind(classOf[MachineAction], classOf[SupplierAction])
    bind(classOf[TechnicSchemeAction])
    bind(classOf[ProductBomAction])
    bind(classOf[ProductTechnicAction])
    bind(classOf[AssessGroupAction])
    bind(classOf[AssessMemberAction])

    bind(classOf[ReviewerAction])
  }
}
