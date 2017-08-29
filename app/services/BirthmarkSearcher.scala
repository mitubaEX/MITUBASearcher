package services

import java.net.URLEncoder

import models.ExtractFile
import models.Result
import play.api.libs.json.{JsValue, Json}
import dispatch._
import dispatch.Defaults._

/**
  * Created by mituba on 2017/07/23.
  */
class BirthmarkSearcher(threshold: String) {
//  def postBirthmark(extractFile: ExtractFile): List[Result] ={
  def postBirthmark(extractFile: ExtractFile): List[List[Result]] ={
    def search(classFile: String,
               jar: String,
               kindOfBirthmark: String,
               birthmarkData: String): List[Result] = {
      val postParam: String =
        s"""
           {
            "params":{
              "q":"${birthmarkData.replace(" ", "+").replace(".", "-")}",
              "rows":"${10}",
              "wt":"csv"
            }
           }
        """
      val postUrl: String = s"http://localhost:8980/solr/${kindOfBirthmark.replace("-","")}/query"
      val encodedBirthmarkData: String = URLEncoder.encode(birthmarkData, "UTF-8")

      val requestHandler = url(postUrl).POST
        .addQueryParameter("fl", s"""filename,lev:strdist(birthmark,"${birthmarkData}",edit),jar,groupID,artifactID,version,birthmark""")
        .setContentType("application/json", "UTF-8")  << postParam

      val http: Either[Throwable, String] = Http(requestHandler OK as.String).either()

      http match {
        case Right(res) => {
          res.split("\n").map(_.split(",", 7)).map(n => new Result(postedClassFile = extractFile.extractFile,
            resultClassFile = n(0), sim = n(1), jar = n(2), groupId = n(3), artifactId = n(4), version = n(5), birthmarkData = n(6)
          )).filterNot(_.sim.contains("lev"))
            .sortWith(_.sim.toDouble > _.sim.toDouble)
            .filter(_.sim.toDouble >= threshold.toDouble)
            .toList
        }
        // 例外はいたら空のリスト投げ返す．
        case _ => List(new Result("","","","","","","",""))
      }
    }

    val source = scala.io.Source.fromFile("files/" + extractFile.extractFile)
    val searchResult: List[List[Result]] = source.getLines().map(_.toString.split(",", 4))
      .map(n => search(n(0), n(1), n(2), n(3)))
      .toList
    source.close()
    searchResult
  }
}
