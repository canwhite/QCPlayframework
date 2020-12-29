import cn.playscala.mongo.Mongo
import com.google.inject.AbstractModule
import play.api.libs.json.{Format, Json}
import cn.playscala.mongo.codecs.macrocodecs.JsonFormat

class Module extends AbstractModule {
  override def configure() = {
    //setModelsPackage将会查找指定包路径下的所有Case Class,自动生成驱动所需的编解码器
    //需要注意的是，这些编解码器是驱动私有的，外界无法共享。我们仍然需要定义全局共享的隐式 Format 对象：
    Mongo.setModelsPackage("models")
  }
}

//定义全局共享的隐式Format对象
//如果有很多的 Case Class，则需要逐个定义，编写起来还是挺麻烦的。

//package object models {
//  implicit val personFormat = Json.format[Person]
//}


// so，我们可以使用 @JsonFormat 宏注解，通过一行代码为所有 Case Class 生成相应的隐式 Format 对象：
package object models {
  @JsonFormat("models")
  implicit val formats = ???
}