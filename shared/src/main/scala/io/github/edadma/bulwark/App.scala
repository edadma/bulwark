package io.github.edadma.bulwark

import io.github.edadma.cross_platform.readFile

object App {

  def run(args: Args): Unit = {
    args match {
      case Args(config, verbose, test, log, Some(BackupCommand())) => Backup(config)
    }
  }

}
