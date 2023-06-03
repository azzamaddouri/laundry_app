package com.uptech.buanderie



object Log {

    fun i(value: String) {
        println(value)
    }

    fun i(tag: String, value: String) {
        println("$tag:::$value")
    }

    fun e(value: String) {
        println("Error:$value")
    }

    fun e(tag: String, value: String) {
        println("Error:$tag:::$value")
    }
}