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
package com.yushanginfo.erp.order.service.impl

import java.time.{LocalDate, ZoneId}

import com.yushanginfo.erp.mes.model.{AssessStatus, WorkOrder}
import com.yushanginfo.erp.order.service.OrderService
import org.beangle.data.dao.EntityDao

class OrderServiceImpl extends OrderService {

  var entityDao: EntityDao = _

  override def recalcState(order: WorkOrder): Unit = {
    val notComplete =
      order.technicScheme.technics.exists { pt =>
        val passed = order.assesses.exists { assess =>
          assess.technic == pt.technic && assess.passed
        }
        !passed
      }

    //计算计划完工时间
    order.materialAssess foreach { materialAssess =>
      if (!notComplete && order.scheduledOn.isEmpty) {
        val processDays = order.assesses.foldLeft(0)(_ + _.days)
        val startOn =
          if (materialAssess.ready) {
            LocalDate.ofInstant(order.assesses.map(_.updatedAt).max,ZoneId.systemDefault()).plusDays(1)
          } else {
            materialAssess.readyOn.get
          }

        order.scheduledOn = Some(startOn.plusDays(processDays))
        if (order.scheduledOn.get.compareTo(order.deadline) > 0) {
          order.status = AssessStatus.Unpassed
        } else {
          order.status = AssessStatus.Passed
        }
      }
    }
    entityDao.saveOrUpdate(order)
  }

}
