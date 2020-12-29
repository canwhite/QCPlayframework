package models

import cn.playscala.mongo.annotations.Entity


@Entity("common-person")//Entity用于指定关联的mongodb collection名称，如果未指定，则默认使用Model类名称
case class Person(_id: String, name: String, age: Int)
//作为约定，Model 类使用 _id 字段作为唯一标识， 该字段同时也是 mongodb collection 的默认主键。
