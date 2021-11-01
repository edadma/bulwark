package io.github.edadma.bulwark

object App {

  def run(args: Args): Unit = {
    args match {
      case Args(config, verbose, test, log, Some(BackupCommand())) =>
        if (!(config.isFile && config.canRead)) problem(s"config file '${config.getCanonicalPath}")

        val c = yaml

        Backup()
    }
  }

}
