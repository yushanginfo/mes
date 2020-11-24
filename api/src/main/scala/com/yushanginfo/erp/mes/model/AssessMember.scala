package com.yushanginfo.erp.mes.model

import com.yushanginfo.erp.base.model.{Factory, User}
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Updated

class AssessMember extends LongId with Updated {

  var group: AssessGroup = _

  var user: User = _

  var factory: Factory = _

}
