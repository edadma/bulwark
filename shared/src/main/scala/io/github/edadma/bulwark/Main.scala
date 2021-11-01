package io.github.edadma.bulwark

import java.io.File
import scopt.OParser

case class Args(config: File = new File("bulwark.yaml").getCanonicalFile,
                verbose: Boolean = false,
                test: Boolean = false,
                log: Option[File] = None,
                command: Option[Command] = None)

trait Command
case class BackupCommand() extends Command

object Main extends App {

  val builder = OParser.builder[Args]
  val parser = {
    import builder._

    val BOLD = Console.BOLD
    var firstSection = true

    def section(name: String) = {
      val res =
        s"${if (!firstSection) "\n" else ""}$BOLD\u2501\u2501\u2501\u2501\u2501 $name ${"\u2501" * (20 - name.length)}${Console.RESET}"

      firstSection = false
      res
    }

    OParser.sequence(
      programName("bulwark"),
      head("Bulwark backup/restore utility", "v0.1.0"),
      note(section("General Options")),
      opt[File]('c', "config")
        .valueName("<path>")
        .action((y, c) => c.copy(config = y.getCanonicalFile))
        .text("YAML configuration"),
      help('h', "help").text("prints this usage text"),
      opt[File]('l', "log")
        .valueName("<path>")
        .action((l, c) => c.copy(log = Some(l.getCanonicalFile)))
        .text("log file"),
      opt[Unit]('t', "test")
        .action((_, c) => c.copy(test = true))
        .text("test run (no modifications)"),
      opt[Unit]('v', "verbose")
        .action((_, c) => c.copy(verbose = true))
        .text("verbose output"),
      version("version").text("prints the version"),
      note(section("Commands")),
      cmd("backup")
        .action((_, c) => c.copy(command = Some(BackupCommand())))
        .text("  Backup the system")
        .children(
          opt[Unit]('s', "sync")
            .action((_, c) => c.copy(verbose = true))
            .text("sync backup (destructive)")
        ),
    )
  }

  OParser.parse(parser, args, Args()) match {
    case Some(args: Args) if args.command.nonEmpty => App run args
    case Some(_)                                   => println(OParser.usage(parser))
    case _                                         =>
  }

}
