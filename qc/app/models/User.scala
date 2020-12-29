package models

import cn.playscala.mongo.annotations.Entity

import java.time.Instant


@Entity("common-user")
case class User(_id: String, name: String, password: String, addTime: String)