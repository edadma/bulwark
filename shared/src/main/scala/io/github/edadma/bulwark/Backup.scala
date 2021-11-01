package io.github.edadma.bulwark

import io.github.edadma.cross_platform.readFile
import io.github.edadma.yaml
import io.github.edadma.yaml.{MapYamlNode, SeqYamlNode, StringYamlNode}

import java.io.File
import java.nio.file.Path

object Backup {

  def apply(config: File): Unit = {
    if (!(config.isFile && config.canRead)) problem(s"config file '$config")

    yaml.readFromString(readFile(config.toString)) match {
      case MapYamlNode(entries) =>
        entries map {
          case (StringYamlNode("period"), StringYamlNode(period)) =>
          case (StringYamlNode("groups"), MapYamlNode(gs)) =>
            gs map {
              case (StringYamlNode(group), MapYamlNode(specs)) =>
                specs foreach {
                  case (StringYamlNode("paths"), SeqYamlNode(paths))      =>
                  case (StringYamlNode("exclude"), SeqYamlNode(excludes)) =>
                  case spec                                               => problem(s"invalid spec ($spec) in config file: $config")
                }
              case g => problem(s"invalid group ($g) in config file: $config")
            }
          case s => problem(s"invalid section ($s) in config file: $config")
        }
      case _ => problem(s"expected map of sections in config file: $config")
    }
  }

}

case class Config(period: Long, groups: List[ConfigGroup])
case class ConfigGroup(name: String, paths: List[Path], exclude: List[Path])
