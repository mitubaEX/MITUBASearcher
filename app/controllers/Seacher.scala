package controllers

import javax.inject.Inject

import play.api.mvc.{Action, Controller}
import play.api.libs.json._

// ユーザからもらうものは，ファイル名と閾値とバースマークの種類

// caseクラスを定義
case class Param(inputFileName: String, kindOfBirthmark: String, threshold: String)
// コンパニオンオブジェクトを定義
object Param {
  implicit def jsonWrites = Json.writes[Param]
  implicit def jsonReads = Json.reads[Param]
}

/**
  * Created by mituba on 2017/07/23.
  */
class Seacher @Inject() extends Controller {
  def search = Action { request =>
    // getRequestJson
    val params : Option[JsValue] = request.body.asJson
    val json = params.get

    // encode
    val result: JsResult[Param] = json.validate[Param]
    val person: Param = result.get
    println(person.inputFileName + " " + person.kindOfBirthmark + " " + person.threshold)

    // decode
    val decodeJson: JsValue = Json.toJson(person)
    println(decodeJson.toString())

    Ok(decodeJson.toString())
  }
}
