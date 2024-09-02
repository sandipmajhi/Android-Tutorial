package com.example.myapplication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @POST("userlogin/")
    fun login(@Body loginRequest:LoginRequest): Call<LoginResponse>

    @POST("empa_register/")
    fun emparegister(@Body signupRequest: SignupRequest): Call<SignupResponse>

    @POST("emps_register/")
    fun empsregister(@Body signupRequest: SignupRequest): Call<SignupResponse>

    @GET("empa_alluser/")
    fun viewallempausers(): Call<List<EmpaView>>

    @GET("userlogout/")
    fun logout(): Call<LogoutResponse>
}