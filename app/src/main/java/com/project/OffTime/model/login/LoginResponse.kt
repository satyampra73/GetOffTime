package com.project.OffTime.model.login

data class LoginResponse(
    val `data`: Data,
    val msg: String,
    val status: Boolean,
    val token: String
)