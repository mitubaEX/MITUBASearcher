package controllers

import java.io.File
import javax.inject.Inject

import dispatch.Future
import models.{Birthmark, ExtractFile, Result}
import play.api.mvc.{Action, Controller}
import services.{BirthmarkSearcher, EachBirthmarkComparator}


/**
  * Created by mituba on 2017/07/23.
  */
class FileUploader @Inject() extends Controller {
  def upload = Action(parse.multipartFormData){ request =>
    val extractFile = new ExtractFile(request, new Birthmark("2-gram"))


    val searchResult: List[List[Result]] = new BirthmarkSearcher().postBirthmark(extractFile)

    val resultMap = searchResult.flatMap(n => n).sortWith(_.sim.toDouble > _.sim.toDouble)
      .map(m => m.postedClassFile + "," + m.resultClassFile + "," + m.sim + "," + m.jar
        + "," + m.groupId + "," + m.artifactId + "," + m.version)

    val compareResultMap = new EachBirthmarkComparator(extractFile, searchResult, new Birthmark("2-gram"))
      .compare().flatMap(n => n).sortWith(_.sim.toDouble > _.sim.toDouble)
      .map(m => m.postedClassFile + "," + m.resultClassFile + "," + m.sim + "," + m.jar
        + "," + m.groupId + "," + m.artifactId + "," + m.version)

    Ok(resultMap.mkString("\n"))
  }
}
