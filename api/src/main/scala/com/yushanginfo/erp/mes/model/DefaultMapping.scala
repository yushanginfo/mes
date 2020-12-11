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

import com.yushanginfo.erp.base.model.Supplier
import org.beangle.data.orm.MappingModule

class DefaultMapping extends MappingModule {

  def binding(): Unit = {
    defaultCache("erp.mes", "read-write")

    bind[MeasurementUnit]
    bind[MaterialType]

    bind[Technic] declare { e =>
      e.code is unique
    }
    bind[Machine] declare { e =>
      e.code is unique
    }

    bind[Supplier]
    bind[Material] declare { e =>
      e.code is unique
    }
    bind[MaterialItem]

    bind[Product].declare { e =>
      e.code is unique
      e.bom.is(depends("product"), orderby("indexno"))
      e.technicSchemes is depends("product")
    }

    bind[ProductMaterialItem]
    bind[TechnicScheme].declare { e =>
      e.technics.is(depends("scheme"), orderby("indexno"))
      index("", false, e.product)
    }
    bind[ProductTechnic].declare { e =>
      e.description is length(500)
    }

    bind[OrderSetting]

    bind[WorkOrder].declare { e =>
      e.assesses is depends("workOrder")
    }

    bind[DepartAssess].declare { e =>
    }

    bind[MaterialAssess].declare { e =>
      index("", true, e.order)
    }

    bind[SalesOrderType]
    bind[WorkOrderType]

    bind[AssessGroup].declare { e =>
      e.members is depends("group")
    }

    bind[AssessMember] declare { e =>
      index("", true, e.group, e.user, e.factory)
    }

    bind[WorkOrderStatus] declare { e =>
      e.code.is (length(10),unique)
    }
  }
}
