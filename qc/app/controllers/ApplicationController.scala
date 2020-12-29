package controllers

import akka.stream.ThrottleMode
import cn.playscala.mongo.Mongo
import cn.playscala.mongo.client.FindBuilder
import models.User
import play.api.Play.materializer
import play.api.data.Form
import play.api.data.Forms.{text, tuple}
import play.api.libs.json.JsObject

import javax.inject._
import play.api.mvc._
import play.api.libs.json.Json._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global


/*========================================================================
MessagesAbstractController是因为里边加了actionBuilder这东西就是为了解决CSRF问题才有的
这里mongo的注入类似于angular
import play.api.libs.json.Json._导入， 所以 Json.obj() 可以被简写为 obj()
======================================================================= */


@Singleton
class ApplicationController @Inject()(cc: MessagesControllerComponents,mongo: Mongo) extends  MessagesAbstractController(cc)  {
  import  UserForm._


  /*******************************************************
  *初始页面绑定view
  ********************************************************/
  def login = Action{
    implicit  request=>{

      //传入多个参数的方法，括号内用逗号隔开
      Ok(views.html.login(form))
    }
  }


  /*******************************************************
   *登录操作，get方法
   ********************************************************/

  def doLogin(userName:String,password:String) = Action{
    implicit request: Request[AnyContent] =>{
      var   mess =userName + "&" + password
      Ok(mess)

    }
  }

  /*******************************************************
   *登录操作，post方法1
   * 非浏览器js操作并设置请求头,如果是浏览器的话设置routes + nocsrf
   ********************************************************/

  val userForm = Form(
    tuple(
      "userName" -> text,
      "password" -> text
    )
  )

  def doPostLogin1 = Action{
    implicit  request =>{
      val (userName, password) = userForm.bindFromRequest.get
      Ok(obj("name"->userName,"value"->"hello,world"))
    }
  }





  /*******************************************************
   *登录操作，post方法2
   * 浏览器和非浏览器兼容，不过需要用play那一套，麻烦但是兼容
   ********************************************************/
  def doPostLogin2 = Action{
    implicit  request =>{

      //直接用这套也可以
      form.bindFromRequest.fold(
        err => {
          println("=====",err);
          Ok("has error");
        },
        success => {
          Ok(obj("name"->success.userName,"value"->"hello,world"))
        }
      )
    }
  }

  /*********************************************************
   *常规的增删改查的使用
   ********************************************************/

  //增
  def addData() = Action{


    /*

      //(1)插入model,参数是整个model
      //mongo.insertOne[User](User("0", "joymufeng", "123456", Instant.now))


      //（2）插入json
      val jsObj = obj("_id" -> "1", "name" -> "joymufeng", "password" -> "123456", "addTime" -> Instant.now)
      // a.通过[model]识别插入
      def add =  mongo.collection[User].insertOne(jsObj);
      // b.通过表名插入，自动识别
      //mongo.collection("common-user").insertOne(jsObj)//这种方法会自动找到对应的model的

   */


    //查询所有数据
    def getLastUser =
      mongo.find[User]().list().map{
        users =>{
          println("---",users);
          var index = users.length -1;
          val data = users(index);
          data
        }
      }
    var data = Await.result(getLastUser,3 seconds);

    var id = data._id.toInt + 1; //这里加成了一个字符串
    var _id = id + "";


    val jsObj = obj("_id" -> _id , "name" -> "qc", "password" -> "123456", "addTime" -> Instant.now)
    // a.通过[model]识别插入
    def add =  mongo.collection[User].insertOne(jsObj);

    var success = Await.result(add,3 seconds);

    println(success);

    var  up = Await.result(getLastUser,3 seconds);
    println(up);
    if(up._id.toInt == id){
      Ok("success");
    }else{
      //正常应该到错误页面，我们这里简单处理
      Ok("fail");
    }


  }

  //删除
  def deleteData() = Action{

    //在这里进行删除操作
    /*
    mongo.deleteById[User]("0")
    mongo.deleteOne[User](obj("_id" -> "0"))

    mongo.collection[User].deleteById("0")
    mongo.collection[User].deleteOne(obj("_id" -> "0"))

    mongo.collection("common-user").deleteOne(obj("_id" -> "0"))

     */


    //简单操作，世界上可以先查这条数据，如果存在就删除，删完再查
    mongo.collection[User].deleteById("5");

    Ok("delete");
  }

  //改
  def updateData() = Action{
    /*
      mongo.updateById[User]("0", obj("$set" -> obj("password" -> "123321")))
      mongo.updateOne[User](obj("_id" -> "0"), obj("$set" -> obj("password" -> "123321")))

      mongo.collection[User].updateById("0", obj("$set" -> obj("password" -> "123321")))
      mongo.collection[User].updateOne(obj("_id" -> "0"), obj("$set" -> obj("password" -> "123321")))

      mongo.collection("common-user").updateById("0", obj("$set" -> obj("password" -> "123321")))
      mongo.collection("common-user").updateOne(obj("_id" -> "0"), obj("$set" -> obj("password" -> "123321")))

     */



    mongo.collection[User].updateOne(obj("_id" -> "0"), obj("$set" -> obj("password" -> "123321")))
    Ok("update");
  }



  //查
  def findData() = Action{
    /*
      mongo.findById[User]("0") // Future[Option[User]]
      mongo.find[User](obj("_id" -> "0")).first // Future[Option[User]]

      mongo.collection[User].findById[User]("0") // Future[Option[User]]
      mongo.collection[User].find[User](obj("_id" -> "0")).first // Future[Option[User]]

      mongo.collection[User].findById[JsObject]("0") // Future[Option[JsObject]]
      mongo.collection[User].find[JsObject](obj("_id" -> "0")).first // Future[Option[JsObject]]

      mongo.collection("common-user").findById[User]("0") // Future[Option[User]]
      mongo.collection("common-user").find[User](obj("_id" -> "0")).first // Future[Option[User]]

      mongo.collection("common-user").findById[JsObject]("0") // Future[Option[JsObject]]
      mongo.collection("common-user").find[JsObject](obj("_id" -> "0")).first // Future[Option[JsObject]]
    */


    //操作函数
    def func = mongo.find[User](obj("_id" -> "0")).first
    var data = Await.result(func,3 seconds); //阻塞式展开，第二个参数表示最长阻塞时间
    //接开返回
    data match{
      case Some(s) =>{
        Ok(s.name);
      }
    }
  }
}
