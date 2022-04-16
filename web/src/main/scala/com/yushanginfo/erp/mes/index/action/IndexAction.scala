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

package com.yushanginfo.erp.mes.index.action

import org.beangle.ems.app.web.NavContext
import org.beangle.security.realm.cas.{Cas, CasConfig}
import org.beangle.web.action.support.{ActionSupport, ServletSupport}
import org.beangle.web.action.annotation.action
import org.beangle.web.action.context.ActionContext
import org.beangle.web.action.view.View

/**
 * @author duantihua
 */
@action("")
class IndexAction extends ActionSupport with ServletSupport {

  var casConfig: CasConfig = _

  def index(): View = {
    put("nav", NavContext.get(request))
    forward()
  }

  def logout(): View = {
    redirect(to(Cas.cleanup(casConfig, ActionContext.current.request, ActionContext.current.response)), null)
  }
}
