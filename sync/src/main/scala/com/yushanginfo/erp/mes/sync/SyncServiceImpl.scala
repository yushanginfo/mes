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
package com.yushanginfo.erp.mes.sync

import javax.sql.DataSource
import org.beangle.commons.bean.Initializing
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.time.Stopwatch
import org.beangle.commons.lang.{ClassLoaders, Strings}
import org.beangle.commons.logging.Logging
import org.beangle.data.jdbc.engine.Engines
import org.beangle.data.jdbc.query.JdbcExecutor
import org.beangle.db.transport.Config.{SeqConfig, TableConfig}
import org.beangle.db.transport.{Config, ConversionModel, Reactor}
import org.beangle.ems.app.datasource.AppDataSourceFactory

class SyncServiceImpl extends SyncService with Initializing with Logging {

  var dataSource: DataSource = _

  var erpDataSource: DataSource = _

  override def init(): Unit = {
    try {
      val dataSourceFactory = new AppDataSourceFactory()
      dataSourceFactory.name = "erp"
      dataSourceFactory.init()
      erpDataSource = dataSourceFactory.result
      SyncDaemon.start("ERP SYNC", 1, this)
    } catch {
      case e: Throwable => e.printStackTrace()
    }

  }

  def sync(): String = {
    transportTables()
    val executor = new JdbcExecutor(dataSource)
    val log = new StringBuilder
    executeFile(executor, "0init.sql", log)
    executeFile(executor, "1remove.sql", log)
    executeFile(executor, "2update.sql", log)
    executeFile(executor, "3insert.sql", log)
    log.toString()
  }

  def transportTables(): Unit = {
    val source = new Config.Source(Engines.SQLServer, erpDataSource)
    val target = new Config.Target(Engines.PostgreSQL, dataSource)
    target.schema = Engines.PostgreSQL.toIdentifier("shtz")
    source.schema = Engines.SQLServer.toIdentifier("dbo")
    source.catalog = Engines.SQLServer.toIdentifier("SHTZ")

    val tableConfig = new TableConfig
    tableConfig.lowercase = true
    tableConfig.withIndex = true
    tableConfig.withConstraint = true
    tableConfig.includes = Strings.split("cmsmd,cmsmw,invmb,bomme,bommf,bomcb,mocta,sfcta").toSeq
    tableConfig.excludes = List.empty
    source.table = tableConfig
    source.sequence = new SeqConfig
    source.sequence.includes = List.empty
    source.sequence.excludes = List.empty

    val config = new Config(source, target, 1, 10000, Tuple2(0, Int.MaxValue), ConversionModel.Recreate)
    config.beforeActions=List.empty
    config.afterActions=List.empty
    new Reactor(config).start()
  }

  def executeFile(executor: JdbcExecutor, fileName: String, log: StringBuilder): String = {
    readSql(fileName) foreach { s =>
      if (s.startsWith("--")) {
        var comment = Strings.substringBefore(s, "\n")
        comment = Strings.replace(comment, "--", "")
        var statement = Strings.substringAfter(s, "\n").trim()
        statement = Strings.replace(statement, "\n", " ")
        log.append(comment)
        val sw = new Stopwatch(true)
        val rs = executor.update(statement)
        log.append(s"${rs}条\n")
        logger.info(comment + s"${rs}条 用时 ${sw}")
      } else {
        executor.update(s)
      }
    }
    log.toString()
  }

  def readSql(name: String): Seq[String] = {
    ClassLoaders.getResourceAsStream(name) match {
      case Some(r) =>
        val content = IOs.readString(r)
        val statements = Strings.split(content, ";")
        statements.map(x => x.replace('\r', '\n').trim).toList
      case None => List.empty
    }
  }

}
