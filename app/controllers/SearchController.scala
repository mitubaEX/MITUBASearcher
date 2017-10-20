package controllers

import javax.inject.Inject

import play.api.mvc.{Action, Controller}

import play.api._
import play.api.libs.json._
import play.api.libs.functional.syntax._

class SearchController  @Inject() extends Controller {
  implicit val rds = (
      (__ \ 'data).read[String] and
      (__ \ 'birthmark).read[String] and
      (__ \ 'threshold).read[String]
    ) tupled

  // data, birthmark, thresholdを受け取り，それに基づいて検索する
  def search = Action(parse.json) { request =>
    println(request.body.toString)
    request.body.validate[(String, String, String)].map{
      case (data, birthmark, threshold) => Ok(s"${data}, ${birthmark}, ${threshold}")
    }.recoverTotal{
      e => BadRequest("error")
    }
//    (request.body \ "data").asOpt[String].map{ data =>
//      Ok("hello" + data)
//    }.getOrElse{
//      BadRequest("Missing parameter [data]")
//    }
  }
}
