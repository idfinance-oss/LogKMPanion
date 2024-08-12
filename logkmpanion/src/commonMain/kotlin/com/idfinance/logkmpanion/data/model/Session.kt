package com.idfinance.logkmpanion.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

internal class Session() : RealmObject {
    @PrimaryKey
    var _id: String = ""
    var timeStart: Long = 0
}