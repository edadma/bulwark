package io.github.edadma.bulwark

import java.io.ByteArrayInputStream
import java.security.{DigestInputStream, MessageDigest}
import java.util.zip.CRC32
import scala.language.postfixOps

object Main extends App {

  val s = "this is a test string"
  val digest = MessageDigest.getInstance("SHA-1")
  val input = new ByteArrayInputStream(s.getBytes)
  val digestStream = new DigestInputStream(input, digest)

  while (digestStream.read() != -1) {
    // read file stream without buffer
  }

  val msgDigest = digestStream.getMessageDigest

  println((msgDigest.digest map (b => "%02x".format(b))) mkString)

  //  val crc = new CRC32
  //
  //  crc.update(s.getBytes)
  //  println(java.lang.Long.toHexString(crc.getValue))
}
