package com.idfinance.kmm.debug.view.data.model

import com.idfinance.kmm.debug.view.domain.LogType
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

internal class Log() : RealmObject {
    @PrimaryKey
    var _id = ObjectId()
    var tag: String = ""
    var message: String = ""
    var isError: Boolean = false
    var time: Long = 0

    constructor(tag: String = "", message: String = "", type: LogType = LogType.DEFAULT, time: Long = 0) : this() {
        this.tag = tag
        this.message = message
        this.isError = type == LogType.ERROR
        this.time = time
    }
}
