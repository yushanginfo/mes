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
package com.yushanginfo.erp.mes.sync

import javax.sql.DataSource
import org.beangle.data.jdbc.query.JdbcExecutor

class SyncServiceImpl extends SyncService {

  var dataSource: DataSource = _

  def sync(): String = {
    val executor = new JdbcExecutor(dataSource)

    executor.update("insert into erp_mes.machines(id,code,name,updated_at) select next_id('erp_mes.machines'),md.md001,md.md002,now() from shtz.cmsmd md \n\twhere not exists(select * from erp_mes.machines a where md.md001 = a.code);")
    executor.update("update erp_mes.machines m set name=(select md.md002 from shtz.cmsmd md where md.md001=m.code)\n\twhere exists(select * from shtz.cmsmd md where md.md001 = m.code and md.md002<>m.name);")

    ""
  }

  private def deleteData(): String = {
    //remove data
    ""
  }
}
