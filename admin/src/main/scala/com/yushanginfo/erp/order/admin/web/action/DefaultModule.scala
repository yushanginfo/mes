package com.yushanginfo.erp.order.admin.web.action

import com.yushanginfo.erp.order.admin.web.action.base.{CustomerAction, DepartmentAction, FactoryAction, OrderTypeAction, TechnicAction, UserAction}
import com.yushanginfo.erp.order.admin.web.action.contract.{DepartAssessAction, FinalAssessAction, MaterialAction, SalesOrderAction}
import org.beangle.cdi.bind.BindModule

class DefaultModule  extends BindModule {
	override protected def binding(): Unit = {
		bind(classOf[UserAction],classOf[DepartmentAction],classOf[FactoryAction],classOf[TechnicAction],classOf[CustomerAction],classOf[OrderTypeAction])

		bind(classOf[ProductAction])

		bind(classOf[SalesOrderAction],classOf[MaterialAction],classOf[DepartAssessAction],classOf[FinalAssessAction])
	}
}
