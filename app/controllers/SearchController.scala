package controllers

import javax.inject.Inject

import models.Result
import play.api.mvc.{Action, Controller}
import play.api._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import services.{BirthmarkPoster, BirthmarkSearcher}
import spray.json.DefaultJsonProtocol
import spray.json._

case class ResultJSONPost(postedClassFile: String, resultClassFile: String, sim: String,
                      jar: String, groupId: String, artifactId: String, version: String, birthmarkData: String)

object MyJsonProtocolPost extends DefaultJsonProtocol{
  implicit val resultFormat = jsonFormat8(ResultJSON)
}

class SearchController  @Inject() extends Controller {
  implicit val rds = (
      (__ \ 'data).read[String] and
      (__ \ 'birthmark).read[String] and
      (__ \ 'threshold).read[String]
    ) tupled

  // data, birthmark, thresholdを受け取り，それに基づいて検索する
  def search = Action(parse.json) { request =>
    import MyJsonProtocolPost._
    println(request.body.toString)
    request.body.validate[(String, String, String)].map{
      case (data, birthmark, threshold) => {
        val searchResult: List[Result] =
          new BirthmarkPoster(data = data, birthmark = birthmark, threshold = threshold).post()
        val resultMap = searchResult.filterNot(n => n.postedClassFile == "")
          .map(m => ResultJSON(m.postedClassFile.replace(".csv", ""), m.resultClassFile, m.sim,
            m.jar, m.groupId, m.artifactId, m.version.replace("_", "."), m.birthmarkData).toJson.toString())
        println("[" + resultMap.mkString(",") + "]")
        Ok("[" + resultMap.mkString(",") + "]")
//        Ok(s"${data}, ${birthmark}, ${threshold}")
      }
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
