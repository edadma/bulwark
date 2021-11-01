package io.github.edadma

import io.github.edadma.datetime.Datetime
import io.github.edadma.yaml.{
  BooleanYamlNode,
  FloatYamlNode,
  IntYamlNode,
  MapYamlNode,
  NullYamlNode,
  SeqYamlNode,
  StringYamlNode,
  TimestampYamlNode,
  YamlNode,
  readFromString
}

import scala.language.postfixOps

package object bulwark {

  def problem(msg: String): Nothing = {
    Console.err.println(msg)
    sys.exit(1)
  }

//  def yaml(s: String): Any = {
//    def construct(n: YamlNode): Any =
//      n match {
//        case NullYamlNode         => null
//        case TimestampYamlNode(t) => Datetime.fromString(t).timestamp
//        case MapYamlNode(entries) => entries map { case (k, v) => (construct(k), construct(v)) } toMap
//        case IntYamlNode(n)       => BigDecimal(n)
//        case FloatYamlNode(n)     => BigDecimal(n)
//        case StringYamlNode(s)    => s
//        case BooleanYamlNode(b)   => b == "true"
//        case SeqYamlNode(elems)   => elems map construct
//      }
//
//    construct(readFromString(s))
//  }

}
