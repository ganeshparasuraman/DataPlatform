package com.coniferhealth.dataplatform.parser


import org.json4s.{DefaultFormats, TypeHints}

import scala.collection.JavaConverters._

case class CustomTypeHints(hintsMap: Map[String, Class[_]]) extends TypeHints {

  override val hints: List[Class[_]] = hintsMap.values.toList

  override def hintFor(classVal: Class[_]): String =
    hintsMap.map(_.swap).get(classVal) match {
      case None => throw new IllegalArgumentException(s"Unsupported type $classVal in json spec")
      case Some(x) => x
    }

  override def classFor(hint: String): Option[Class[_]] = hintsMap.get(hint) match {
    case None => throw new IllegalArgumentException(s"Unsupported type $hint in json spec")
    case x => x
  }
}

class CustomTypeFormats(customHintMap: Map[String, Class[_]]) extends DefaultFormats {
  override val typeHintFieldName: String = "type"
  override val typeHints: TypeHints = CustomTypeHints(customHintMap)
}