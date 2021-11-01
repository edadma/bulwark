package io.github.edadma.bulwark

import io.github.edadma.cross_platform.readFile
import io.github.edadma.yaml
import io.github.edadma.yaml.{MapYamlNode, SeqYamlNode, StringYamlNode}

import java.io.File
import java.nio.file.{Files, Path, Paths}
import scala.collection.mutable.ListBuffer

object Backup {

  def apply(config: File): Unit = {
    val c = ReadConfig(config)

  }

}
