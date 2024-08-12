package com.idfinance.logkmpanion.data.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

internal class NetworkRequest : RealmObject {
    @PrimaryKey
    var _id: String = ""
    var sessionId: String = ""
    var request: Request? = null
    var response: Response? = null
    var duration: Long? = null
    var error: String? = null
}

internal class Request : EmbeddedRealmObject {
    var url: String = ""
    var method: String = ""
    var headers: RealmList<String> = realmListOf()
    var cookies: RealmList<String> = realmListOf()
    var body: String = ""
}

internal class Response : EmbeddedRealmObject {
    var statusCode: Int = 0
    var headers: RealmList<String> = realmListOf()
    var body: String = ""
    var time: String = ""
}