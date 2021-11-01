package io.github.edadma

package object bulwark {

  def problem(msg: String): Nothing = {
    Console.err.println(msg)
    sys.exit(1)
  }

}
