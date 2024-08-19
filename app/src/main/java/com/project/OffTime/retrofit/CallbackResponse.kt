package com.db.md.retrofit

interface CallbackResponse {
    fun success(from: String?, message: String?,responseCode:Int)
    fun fail(from: String?)
}