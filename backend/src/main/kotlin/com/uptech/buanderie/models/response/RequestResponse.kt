package com.uptech.buanderie.models.response

class RequestResponse<T> {
    var code = 0
    var msg = ""
    var result: T? = null

    constructor(code: Int, msg: String, result: T?) {
        this.code = code
        this.msg = msg
        this.result = result
    }
}