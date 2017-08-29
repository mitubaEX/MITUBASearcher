package controllers

import java.io.{File, FileWriter}
import javax.inject.Inject

import dispatch.Future
import models.{Birthmark, ExtractFile, Result}
import play.api.mvc.{Action, Controller}
import services.{BirthmarkSearcher, EachBirthmarkComparator}
import spray.json._

case class ResultJSON(postedClassFile: String, resultClassFile: String, sim: String,
                      jar: String, groupId: String, artifactId: String, version: String, birthmarkData: String)

object MyJsonProtocol extends DefaultJsonProtocol{
  implicit val resultFormat = jsonFormat8(ResultJSON)
}

/**
  * Created by mituba on 2017/07/23.
  */
class FileUploader @Inject() extends Controller {
  def mainPage = Action{ request =>
    Ok(views.html.main("MITUBA")("10"))
  }

  def download = Action(parse.anyContent){ request =>
    val randomString = scala.util.Random.alphanumeric.take(20).mkString("")
    val tmpFile: File = new File("files", randomString)
    val tmpFileWriter: FileWriter = new FileWriter(tmpFile)
    tmpFileWriter.write(request.body.asFormUrlEncoded.get.get("searchResult").get(0))
    tmpFileWriter.close()

    Ok.sendFile(content = tmpFile, fileName = _ => randomString)
  }

  def upload = Action(parse.multipartFormData){ request =>
    import MyJsonProtocol._

    val kindOfBirthmark = request.body.dataParts.get("birthmark").get(0)
    val threshold = request.body.dataParts.get("threshold").get(0)
    val extractFile = new ExtractFile(request, new Birthmark(kindOfBirthmark))
    val searchResult: List[List[Result]] = new BirthmarkSearcher(threshold).postBirthmark(extractFile)

    val resultMap = searchResult.flatMap(n => n)
      .filterNot(n => n.postedClassFile == "")
      .map(m => ResultJSON(m.postedClassFile.replace(".csv", ""), m.resultClassFile, m.sim,
        m.jar, m.groupId, m.artifactId, m.version.replace("_", "."), m.birthmarkData).toJson.toString())

    Ok("[" + resultMap.mkString(",") + "]")
  }
}
