package controllers

import java.io.File
import javax.inject.Inject

import dispatch.Future
import models.{Birthmark, ExtractFile, Result}
import play.api.mvc.{Action, Controller}
import services.{BirthmarkSearcher, EachBirthmarkComparator}
import spray.json._

case class ResultJSON(postedClassFile: String, resultClassFile: String, sim: String,
                      jar: String, groupId: String, artifactId: String, version: String)

object MyJsonProtocol extends DefaultJsonProtocol{
  implicit val resultFormat = jsonFormat7(ResultJSON)
}

/**
  * Created by mituba on 2017/07/23.
  */
class FileUploader @Inject() extends Controller {
  def mainPage = Action{ request =>
    Ok(views.html.main("WebAPI"))
  }

  def upload = Action(parse.multipartFormData){ request =>
    println(request.body.toString())
    request.body.file("a").map{ n => println(n) }
    val extractFile = new ExtractFile(request, new Birthmark("2-gram"))

    val searchResult: List[List[Result]] = new BirthmarkSearcher().postBirthmark(extractFile)

    import MyJsonProtocol._

    val resultMap = searchResult.flatMap(n => n).filterNot(_.sim.contains("lev")).sortWith(_.sim.toDouble > _.sim.toDouble)
      .map(m => ResultJSON(m.postedClassFile.replace(".csv", ""), m.resultClassFile, m.sim,
        m.jar, m.groupId, m.artifactId, m.version.replace("_", ".")).toJson.toString())

    println("[" + resultMap.mkString(",") + "]")
    Ok("[" + resultMap.mkString(",") + "]")
  }
}
