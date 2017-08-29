package models

import java.io.File
import java.nio.file.StandardCopyOption

import com.github.pochi.runner.scripts.{ScriptRunner, ScriptRunnerBuilder}
import play.api.libs.Files
import play.api.mvc.{MultipartFormData, Request}
import services.Extracter
/**
  * Created by mituba on 2017/07/23.
  */
class ExtractFile(request: Request[MultipartFormData[Files.TemporaryFile]], kindOfBirthmark: Birthmark) {
  val result = request.body.file("file").map { file =>
    val filename = file.filename
    val contentType = file.contentType
//    file.ref.moveTo(new File("files", filename))
    java.nio.file.Files.copy(file.ref.file.toPath, new File("files", new File(file.filename).getName).toPath, StandardCopyOption.REPLACE_EXISTING)

    // filename, kindOfBirthmark
//    new Extracter().getExtractFile("files/" + file.filename, kindOfBirthmark.birthmark)
    def getExtractFile: String ={
      val builder: ScriptRunnerBuilder = new ScriptRunnerBuilder
      val runner: ScriptRunner = builder.build
      val arg: Array[String] = Array("./extract.js", "files/" + new File(file.filename).getName, kindOfBirthmark.birthmark)
      runner.runsScript(arg)
      new File(file.filename).getName + ".csv"
    }
    getExtractFile
  }
  val extractFile = result.get
}
