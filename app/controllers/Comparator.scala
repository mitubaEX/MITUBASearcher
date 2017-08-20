package controllers

import javax.inject.Inject

import com.github.pochi.runner.birthmarks.BirthmarkComparator
import models.Birthmark
import play.api.mvc.{Action, Controller}

/**
  * Created by mituba on 2017/08/20.
  */
class Comparator @Inject() extends Controller {
  def compare = Action(parse.multipartFormData){ request =>
    println(request.body.dataParts.get("searchResult").get(0))
    println(request.body.dataParts.get("birthmark").get(0))
    val birthmark = request.body.dataParts.get("birthmark").get(0)
    val searchResult = request.body.dataParts.get("searchResult").get(0)
    Ok(services.BirthmarkComparator(new Birthmark(birthmark)).compareBirthmark(searchResult))
  }
}
