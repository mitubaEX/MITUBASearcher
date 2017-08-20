package services

import java.io.{File, FileWriter}

import models.Birthmark
import org.json4s._
import org.json4s.native.JsonMethods._
import play.api.libs.json.Json
import spray.json.DefaultJsonProtocol
import spray.json._


case class ResultJSON(postedClassFile: String, resultClassFile: String, sim: String,
                      jar: String, groupId: String, artifactId: String, version: String, birthmarkData: String)
object MyJsonProtocol extends DefaultJsonProtocol{
  implicit val resultFormat = jsonFormat8(ResultJSON)
  implicit def jsonWrites = Json.writes[ResultJSON]
  implicit def jsonReads = Json.reads[ResultJSON]
}

//object ResultJSON{
////  implicit val jsonFormat: Format[ResultJSON] = Json.format[ResultJSON]
//  implicit def jsonWrites = Json.writes[ResultJSON]
//  implicit def jsonReads = Json.reads[ResultJSON]
//}


/**
  * Created by mituba on 2017/08/20.
  */
case class BirthmarkComparator(birthmark: Birthmark) {
  def compareBirthmark(searchResult: String): String ={
    import MyJsonProtocol._
    val parseSearchResult = parse(searchResult) // JArray(List(JObject(List(tuple(key, val))))
    val parseSearchResultValues = parseSearchResult.children.map(n => n.values)
    val compareResultList: List[ResultJSON] = parseSearchResultValues.map(n => n.asInstanceOf[Map[String, String]]).map(n => performCompare(birthmark = birthmark, postedClassFile = n.get("postedClassFile").get, resultClassFile = n.get("resultClassFile").get, sim = n.get("sim").get,
      jar = n.get("jar").get, groupId = n.get("groupId").get, artifactId = n.get("artifactId").get, version = n.get("version").get, birthmarkData = n.get("birthmarkData").get)).toList
    val compareResultJson: List[String] = compareResultList.map(n => Json.toJson(n).toString()).toList

    "[" + compareResultJson.mkString(",") + "]"
//    val resultList: List[ResultJSON] = parseSearchResultValues.map(n => ResultJSON.getClass.getMethods.find(_.getName == "apply").
//      get.invoke(ResultJSON, n.asInstanceOf[Map[String, String]].values.toList.map(_.asInstanceOf[AnyRef]):_*).asInstanceOf[ResultJSON]).toList
//    println(resultList)
//    resultList.map(n => performCompare(birthmark = birthmark, postedClassFile = n.postedClassFile, resultClassFile = n.resultClassFile, sim = n.sim,
//      jar = n.jar, groupId = n.groupId, artifactId = n.artifactId, version = n.version, birthmarkData = n.birthmarkData)).toList
  }
  def writeFile(resultClassFile: String, birthmarkData: String): Unit ={
    val fileWriter: FileWriter = new FileWriter(new File("files", resultClassFile + ".csv"))
    fileWriter.write(resultClassFile + ",," + birthmark.birthmark + "," + birthmarkData.replace("\"", ""))
    fileWriter.close()
  }

  def performCompare(birthmark: Birthmark, postedClassFile: String, resultClassFile: String, sim: String,
                    jar: String, groupId: String, artifactId: String, version: String, birthmarkData: String): ResultJSON ={
    import com.github.pochi.runner.scripts.ScriptRunner
    import com.github.pochi.runner.scripts.ScriptRunnerBuilder

    val builder = new ScriptRunnerBuilder
    val runner = builder.build
    println(resultClassFile)
    writeFile(resultClassFile, birthmarkData)

    val arg = Array("./compare_input_csv_test.js", birthmark.birthmark, "files/" + postedClassFile + ".csv", "files/" + resultClassFile + ".csv")
    runner.runsScript(arg)

    val result: List[ResultJSON] = scala.io.Source.fromFile("files/files." + postedClassFile + ".csv-files." + resultClassFile + ".csv.csv").getLines().map(_.split(","))
      .map(n => ResultJSON(postedClassFile = postedClassFile, resultClassFile = resultClassFile, sim = n(2), jar = jar, groupId = groupId,
       artifactId = artifactId, version = version, birthmarkData = birthmarkData)).toList
    new File("files/files." + postedClassFile + ".csv-files." + resultClassFile + ".csv.csv").delete()
    result(0)
  }

}
