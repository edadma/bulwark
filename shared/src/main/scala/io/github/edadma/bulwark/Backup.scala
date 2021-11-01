package io.github.edadma.bulwark

import io.github.edadma.cross_platform.readFile
import io.github.edadma.yaml
import io.github.edadma.yaml.{MapYamlNode}

import java.io.File

object Backup {

  def apply(config: File): Unit = {
    if (!(config.isFile && config.canRead)) problem(s"config file '${config.getCanonicalPath}")

    yaml.readFromString(readFile(config.toString)) match {
      case MapYamlNode(entries) =>
    }
  }

}
