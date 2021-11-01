package io.github.edadma.bulwark

import io.github.edadma.cross_platform.readFile
import io.github.edadma.yaml
import io.github.edadma.yaml.{MapYamlNode, SeqYamlNode, StringYamlNode}

import java.io.File
import java.nio.file.{Files, Path, Paths}
import scala.collection.mutable.ListBuffer

object ReadConfig {

  def apply(config: File): Config = {
    if (!(config.isFile && config.canRead)) problem(s"config file '$config")

    val groupsbuf = new ListBuffer[ConfigGroup]
    val periodbuf = new ListBuffer[String]

    yaml.readFromString(readFile(config.toString)) match {
      case MapYamlNode(entries) =>
        entries foreach {
          case (StringYamlNode("period"), StringYamlNode(period)) =>
            if (periodbuf.nonEmpty) problem(s"period has already been given as '${periodbuf.head}'")

            periodbuf += period
          case (StringYamlNode("groups"), MapYamlNode(gs)) =>
            gs foreach {
              case (StringYamlNode(group), MapYamlNode(specs)) =>
                val pathsBuf = new ListBuffer[Path]
                val excludesBuf = new ListBuffer[Path]

                specs foreach {
                  case (StringYamlNode("paths"), SeqYamlNode(paths)) =>
                    paths foreach {
                      case StringYamlNode(path) =>
                        val p = Paths.get(path).normalize

                        if (Files.exists(p) && Files.isReadable(p)) problem(s"can't read path '$p'")
                        if (!p.isAbsolute) problem(s"path '$p' is relative")
                        if (pathsBuf contains p) problem(s"duplicate path '$p'")

                        pathsBuf += p
                    }
                  case (StringYamlNode("exclude"), SeqYamlNode(excludes)) =>
                    excludes foreach {
                      case StringYamlNode(path) =>
                        val p = Paths.get(path).normalize

                        if (Files.exists(p) && Files.isReadable(p)) problem(s"can't read path '$p'")
                        if (p.isAbsolute) problem(s"path '$p' is absolute")
                        if (excludesBuf contains p) problem(s"duplicate path '$p'")

                        excludesBuf += p
                    }
                  case spec => problem(s"invalid spec ($spec) in config file: $config")
                }

                if (groupsbuf.exists(_.name == group)) problem(s"duplicate group '$group'")
                if (pathsBuf.isEmpty) problem(s"no paths were given for group '$group'")

                groupsbuf += ConfigGroup(group, pathsBuf.toList, excludesBuf.toList)
              case g => problem(s"invalid group ($g) in config file: $config")
            }
          case s => problem(s"invalid section ($s) in config file: $config")
        }
      case _ => problem(s"expected map of sections in config file: $config")
    }

    if (groupsbuf.isEmpty) problem("no groups were given")
    if (periodbuf.isEmpty) problem("no period was given")

    val hoursRegex = """(\d+(?:.\d+)?)\s+hours""".r
    val daysRegex = """(\d+(?:.\d+)?)\s+days""".r
    val weeksRegex = """(\d+(?:.\d+)?)\s+weeks""".r
    val SECOND = 1000L
    val MINUTE = 60 * SECOND
    val HOUR = 60 * MINUTE
    val DAY = 24 * HOUR
    val WEEK = 7 * DAY

    def num(s: String, mult: Long): Double =
      (s.toDoubleOption getOrElse problem(s"")) * mult match {
        case 0                    => problem(s"a period of zero doesn't make sense (30 minutes at least)")
        case p if p < 30 * MINUTE => problem(s"you'll be doing backups constantly (30 minutes at least)")
        case p if p > 2 * WEEK    => problem(s"you should backup your system more often than that (2 weeks at most)")
        case p                    => p
      }

    val period =
      periodbuf.head.trim match {
        case "hourly"      => HOUR
        case "daily"       => DAY
        case "weekly"      => WEEK
        case hoursRegex(h) => num(h, HOUR)
        case daysRegex(d)  => num(d, DAY)
        case weeksRegex(w) => num(w, WEEK)
        case p             => problem(s"unparsable period '$p'")
      }

    Config(period.toLong, groupsbuf.toList)
  }

}
