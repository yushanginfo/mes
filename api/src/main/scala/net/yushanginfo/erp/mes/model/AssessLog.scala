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

package net.yushanginfo.erp.mes.model

import net.yushanginfo.erp.base.model.User
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.{Remark, Updated}

import java.time.Instant

class AssessLog extends LongId with Updated with Remark {

  def this(fromStatus: AssessStatus, order: WorkOrder, user: User, ip: String)= {
    this()
    this.orderId = order.id
    this.updatedAt = Instant.now
    this.user = user
    this.ip = ip
    this.fromStatus = fromStatus
    this.toStatus = order.assessStatus
  }

  var orderId: Long = _

  var user: User = _

  var fromStatus: AssessStatus = _

  var toStatus: AssessStatus = _

  var ip: String = _

}
