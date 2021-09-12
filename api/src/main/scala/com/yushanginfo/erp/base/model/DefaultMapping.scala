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
package com.yushanginfo.erp.base.model

import org.beangle.data.orm.MappingModule

class DefaultMapping extends MappingModule {

  def binding(): Unit = {
    defaultCache("erp.base", "read-write")

    bind[Customer] declare { e =>
      e.code is unique
    }

    bind[Department] declare { e =>
      e.code is length(10)
      e.name is length(80)
      e.indexno is length(20)
      e.children is depends("parent")
      index("", true, e.code)
    }

    bind[Factory] declare { e =>
      e.code is unique
    }

    bind[User] declare { e =>
      e.code is unique
    }
    bind[Supplier]
  }
}
