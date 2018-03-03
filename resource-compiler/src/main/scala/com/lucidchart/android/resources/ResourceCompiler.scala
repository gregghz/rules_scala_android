package com.lucidchart.android.resources

import cats.instances.all._
import cats.syntax.all._
import com.lucidchart.open.xtract._
import java.io.PrintWriter
import scala.xml.XML

case class Resources(
  idResources: Set[Resource],
  layoutResources: Set[Resource]
)

object ResourceCompiler {

  def main(args: Array[String]): Unit = {
    CliOptions.parser.parse(args, CliOptions()).map { config =>


      val resources: Resources = config.files.foldLeft(Resources(Set.empty, Set.empty)) { case (resources, filename) =>
        println(s"Processing $filename")
        val file = XML.loadFile(filename)

        val layoutName = parseLayoutName(filename.toString)
        println(layoutName)

        val parseResult = XmlReader.of[Element](Element.reader(Some(layoutName))).read(file)

        parseResult.fold(
          errors => {
            println(s"[WARN] could not parse XML: $errors")
            println(file)
            resources
          }
        )(
          elem => {
            val additionalIdResources = elementsToResources(List(elem), Set.empty)
            val layoutResource = elem.rootName.map { name =>
              val correctedLabel = elem.label match {
                case "merge" => "android.view.View"
                case label => label
              }
              LayoutResource(correctedLabel, name)
            }

            resources.copy(
              idResources = resources.idResources ++ additionalIdResources,
              layoutResources = resources.layoutResources ++ layoutResource
            )
          }
        )
      }

      val finalContent = trTemplate
        .replace("%{package}", config.sourcePackage)
        .replace("%{id_resources}", resources.idResources.map(_.scalaVal).mkString("  ", "\n  ", ""))
        .replace("%{layout_resources}", resources.layoutResources.map(_.scalaVal).mkString("    ", "\n    ", ""))

      println(s"\n$finalContent\n")

      val writer = new PrintWriter(config.outfile)

      try {
        writer.write(finalContent);
      } finally {
        writer.close()
      }
    }
  }

  @scala.annotation.tailrec
  private def elementsToResources(remaining: List[Element], resources: Set[Resource]): Set[Resource] = {
    if (remaining.isEmpty) {
      resources
    } else {
      val elem = remaining.head
      val newResources = elem.id match {
        case Some(rawId) =>
          val id = rawId.replace("@+id/", "").replace("@id/", "")
          resources + IdResource(elem.label, id)
        case None =>
          resources
      }
      elementsToResources(remaining.tail ++ elem.children, newResources)
    }
  }

  private def parseLayoutName(filename: String): String = {
    val withoutExt = filename.dropRight(".xml".length)
    val lastSlashIdx = withoutExt.lastIndexOf('/')
    withoutExt.slice(lastSlashIdx + 1, withoutExt.length)
  }

  val trTemplate = """package %{package}

import android.view._
import android.widget._
import com.lucidchart.android.resources._

object TR {
%{id_resources}

  object layout {
%{layout_resources}
  }
}

"""

}

case class Element(
  label: String,
  id: Option[String],
  children: List[Element],
  rootName: Option[String]
)

object Element {
  import Instances._

  def reader(rootName: Option[String]): XmlReader[Element] = XmlReader[Element] { node =>
    node.headOption.map { node =>
      val id = node.attribute("http://schemas.android.com/apk/res/android", "id").flatMap(_.headOption).map(_.text)

      val children: ParseResult[List[Element]] = node.child.toList.map { child =>
        reader(None).read(child)
      }.sequence

      children.map(Element(node.label, id, _, rootName))
    }.getOrElse(ParseFailure())
  }
}

trait Resource {
  def scalaVal: String
}

case class IdResource(
  label: String,
  id: String
) extends Resource {
  def scalaVal: String = {
    s"final val $id: TypedResource[$label] = TypedResource[$label](R.id.$id)"
  }
}

case class LayoutResource(
  label: String,
  name: String
) extends Resource {
  def scalaVal: String = {
    s"final val $name: TypedLayout[$label] = TypedLayout[$label](R.layout.$name)"
  }
}
