package services

import java.net.URLEncoder

import models.Result
import dispatch._
import dispatch.Defaults._

class BirthmarkPoster(data: String, birthmark: String, threshold: String) {
  def post(): List[Result] = {
    val splitedData = data.split(",", 4)
//    val postParam: String =
//      s"""
//           {
//            "params":{
//              "q":"${splitedData(3).replace(" ", "+").replace(".", "-")}",
//              "rows":"${10}",
//              "wt":"csv"
//            }
//           }
//        """
    val postParam: String =
      s"""
           {
            "params":{
              "q":"*",
              "rows":"${10}",
              "wt":"csv"
            }
           }
        """
    val postUrl: String = s"http://localhost:8982/solr/${birthmark.replace("-", "")}/query"
    val encodedBirthmarkData: String = URLEncoder.encode(data, "UTF-8")
    val requestHandler = url(postUrl).POST
      .addQueryParameter("fl", s"""filename,lev:strdist(birthmark,"${splitedData(3).replace(" ", "+").replace(".", "-")}",edit),jar,groupID,artifactID,version,birthmark""")
      .setContentType("application/json", "UTF-8") << postParam

    val http: Either[Throwable, String] = Http(requestHandler OK as.String).either()
    http match {
      case Right(res) => {
        println(res)
        res.split("\n").map(_.split(",", 7)).map(n => new Result(postedClassFile = splitedData(0),
          resultClassFile = n(0), sim = n(1), jar = n(2), groupId = n(3), artifactId = n(4), version = n(5), birthmarkData = n(6)
        )).filterNot(_.sim.contains("lev"))
          .sortWith(_.sim.toDouble > _.sim.toDouble)
          .filter(_.sim.toDouble >= threshold.toDouble)
          .toList
      }
      // 例外はいたら空のリスト投げ返す．
      case _ => List(new Result("", "", "", "", "", "", "", ""))
    }
  }
}
