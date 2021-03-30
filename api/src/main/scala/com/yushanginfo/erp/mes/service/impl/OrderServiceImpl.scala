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
package com.yushanginfo.erp.mes.service.impl

import com.yushanginfo.erp.mes.model.{AssessStatus, WorkOrder}
import com.yushanginfo.erp.mes.service.OrderService
import org.beangle.data.dao.EntityDao

import java.time.{LocalDate, ZoneId}

class OrderServiceImpl extends OrderService {

  var entityDao: EntityDao = _

  override def recalcState(order: WorkOrder): Unit = {
    val allComplete = order.technics.nonEmpty && !order.technics.exists(!_.passed.getOrElse(false))

    //计算计划完工时间
    order.materialAssess foreach { materialAssess =>
      if (allComplete && order.assessStatus != AssessStatus.Passed) {
        val processDays = order.technics.foldLeft(0)(_ + _.days.get)
        val startOn =
          if (materialAssess.ready) {
            LocalDate.ofInstant(order.technics.map(_.updatedAt).max, ZoneId.systemDefault()).plusDays(1)
          } else {
            materialAssess.readyOn.get
          }

        order.scheduledOn = Some(startOn.plusDays(processDays))
        order.deadline foreach { deadline =>
          if (order.scheduledOn.get.compareTo(deadline) > 0) {
            //此处注意复审轮次
            order.reviewRound = order.reviewRound + 1
            order.assessStatus = AssessStatus.Unpassed
          } else {
            order.assessStatus = AssessStatus.Passed
          }
        }
      }
    }
    entityDao.saveOrUpdate(order)
  }

}
