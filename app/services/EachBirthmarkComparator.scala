package services

import java.io.{File, FileWriter}

import scala.io.Source
import com.github.pochi.runner.scripts.{ScriptRunner, ScriptRunnerBuilder}
import models.{Birthmark, ExtractFile, Result}

/**
  * Created by mituba on 2017/07/24.
  */
class EachBirthmarkComparator(extractFile: ExtractFile, searchResult: List[List[Result]], kindOfBirthmark: Birthmark) {

  // filename -> groupid.artifact
  def createBirthmarkFile(fileName: String, birthmarkData: String): Unit ={
    val File: File = new File("files", fileName)
    val FileWriter: FileWriter = new FileWriter(File)
    FileWriter.write(fileName + ",,," + birthmarkData)
    FileWriter.close()
  }

  def getCompareResultFiles(postedClassFileName: String, resultClassFileName: String): String ={
    val builder: ScriptRunnerBuilder = new ScriptRunnerBuilder
    val runner: ScriptRunner = builder.build
    val arg: Array[String] = Array("./compare_input_csv_test.js", kindOfBirthmark.birthmark, "files/" + postedClassFileName, "files/" + resultClassFileName)
    runner.runsScript(arg)
    "files/files." + postedClassFileName + "-files." + resultClassFileName + ".csv"
  }

  def compare(): List[List[Result]] = {
    def getCompareList(result: Result): List[Result] ={
      createBirthmarkFile(result.resultClassFile + ".csv", result.birthmarkData)
      getCompareResultFiles(result.postedClassFile, result.resultClassFile + ".csv")

      Source.fromFile(getCompareResultFiles(result.postedClassFile, result.resultClassFile + ".csv"))
        .getLines().map(_.split(",")).toList.sortBy(n => n(2) > n(2))
        .map(n => new Result(n(0), n(1).replace(".csv", ""), n(2),
          result.jar, result.groupId, result.artifactId, result.version.replace("_", "."), result.birthmarkData)).toList
    }
    searchResult.flatMap(n => n).map(n => getCompareList(n)).toList
  }
}
