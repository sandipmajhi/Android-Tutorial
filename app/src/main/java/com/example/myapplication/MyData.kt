package com.example.myapplication

data class LoginResponse(
    val email: String,
    val username: String,
    val role: String,
    val access: String,
    val refresh: String,
    val status: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class SignupRequest(
    val email: String,
    val username: String,
    val role: String,
    val password: String
)

data class SignupResponse(
    val email: String,
    val username: String,
    val role: String,
    val access: String,
    val refresh: String,
    val status: String
)

data class LogoutResponse(
    val msg: String
)

data class EmpaView(
    val email: String,
    val username: String,
    val role: String
)