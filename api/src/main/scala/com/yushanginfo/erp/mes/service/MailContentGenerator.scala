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
package com.yushanginfo.erp.mes.service

import freemarker.cache.StrongCacheStorage
import freemarker.core.ParseException
import freemarker.template.{Configuration, SimpleHash, Template}
import org.beangle.commons.lang.Throwables
import org.beangle.commons.logging.Logging
import org.beangle.template.freemarker.{BeangleClassTemplateLoader, BeangleObjectWrapper, IncludeIfExistsModel}

import java.io.{IOException, StringWriter}
import java.{util => ju}

object MailContentGenerator extends Logging {
  class Generator(configuration: Configuration, template: Template) {
    def generate(model: collection.Map[String, Any]): String = {
      val sw = new StringWriter()
      val datas = new SimpleHash(configuration.getObjectWrapper)
      model foreach { case (k, v) =>
        datas.put(k, v)
      }
      template.process(datas, sw)
      sw.toString
    }
  }

  def apply(): MailContentGenerator = {
    val config = new Configuration(Configuration.VERSION_2_3_24)
    config.setEncoding(config.getLocale, "UTF-8")
    val wrapper = new BeangleObjectWrapper()
    wrapper.setUseCache(false)
    config.setObjectWrapper(wrapper)
    config.setTemplateLoader(new BeangleClassTemplateLoader())
    config.setCacheStorage(new StrongCacheStorage())
    config.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX)
    config.setSharedVariable("include_if_exists", new IncludeIfExistsModel)
    // Disable auto imports and includes
    config.setAutoImports(new ju.HashMap(0))
    config.setAutoIncludes(new ju.ArrayList(0))
    new MailContentGenerator(config)
  }
}

class MailContentGenerator(config: Configuration) extends Logging {

  def forTemplate(templateUrl: String): MailContentGenerator.Generator = {
    val template = getTemplate(config, templateUrl)
    new MailContentGenerator.Generator(config, template)
  }

  def generate(templateUrl: String, model: collection.Map[String, Any]): String = {
    forTemplate(templateUrl).generate(model)
  }

  /**
   * Load template in hierarchical path
   */
  private def getTemplate(config: Configuration, templateName: String): Template = {
    try {
      config.getTemplate(templateName, "UTF-8")
    } catch {
      case e: ParseException => throw e
      case e: IOException =>
        logger.error(s"Couldn't load template '$templateName',loader is ${config.getTemplateLoader.getClass}")
        throw Throwables.propagate(e)
    }
  }
}
