package controllers

import javax.inject._
import play.api.mvc._
import models.Product
import play.api.libs.json._

import scala.collection.mutable




@Singleton
class ProductController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private val productList = new mutable.ListBuffer[Product]()
  productList += Product(1, "Przyklad")
  productList += Product(2, "Nastepny przyklad")
  productList += Product(3, "Kolejny przyklad")
  case class newProductList(name: String)

  implicit val productListJson = Json.format[Product]
  implicit val newProductList = Json.format[newProductList]

  def getAll(): Action[AnyContent] = Action {
    if (productList.isEmpty) {
      NoContent
    } else {
      Ok(Json.toJson(productList))
    }
  }

  def getById(itemId: Long) = Action {
    val foundItem = productList.find(_.id == itemId)
    foundItem match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound
    }
  }
  def add() = Action { implicit request =>
      val content = request.body
      val jsonObject = content.asJson
      val newProductList: Option[newProductList] =
        jsonObject.flatMap(
          Json.fromJson[newProductList](_).asOpt
        )
    newProductList match {
      case Some(newItem) =>
        val nextId = productList.map(_.id).max + 1
        val toBeAdded = Product(nextId, newItem.name)
        productList += toBeAdded
        Created(Json.toJson(toBeAdded))
      case None =>
        BadRequest
    }
  }




}