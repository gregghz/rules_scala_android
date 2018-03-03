package com.lucidchart.android.resources

import java.io.File
import scopt._

case class CliOptions(
  outfile: File = new File("."),
  files: Seq[File] = Nil,
  sourcePackage: String = ""
)

object CliOptions {

  val parser = new OptionParser[CliOptions]("cli-options") {
    head("Scala Android Resource Compiler", "a billion")

    opt[File]('o', "outfile").required().action { (value, config) =>
      config.copy(outfile = value)
    }.text("Source file to write compiled resources to")

    opt[String]('p', "package").required().action { (value, config) =>
      config.copy(sourcePackage = value)
    }.text("Package for codes and things")

    arg[File]("<file>...").unbounded().required().action { (value, config) =>
      config.copy(files = config.files :+ value)
    }
  }

}