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
package com.yushanginfo.erp.mes.model

import org.beangle.data.model.LongId

class AssessItem extends LongId {

  var record: AssessRecord = _

  var matchine: String = _

  var technic: Technic = _

  var indexno: String = _

  var days: Int = _

  def this(record: AssessRecord, indexno: String, matchine: String, technic: Technic, days: Int) = {
    this()
    this.record = record
    this.indexno = indexno
    this.matchine = matchine
    this.technic = technic
    this.days = days
  }
}
