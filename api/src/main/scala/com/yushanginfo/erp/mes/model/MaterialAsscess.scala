package com.yushanginfo.erp.mes.model

import java.time.LocalDate

import com.yushanginfo.erp.base.model.User
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Updated

/** 到料信息 */
class MaterialAsscess extends LongId with Updated {

  /** 工单 */
  var order: WorkOrder = _

  /** 是否有料 */
  var ready: Boolean = _

  /** 到料日期 */
  var readyOn: Option[LocalDate] = None

  /** 评估人 */
  var assessedBy: Option[User] = None
}
