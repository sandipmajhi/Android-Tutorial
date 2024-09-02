package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

const val BASE_URL = "http:/192.168.29.28:8000//api/users/"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



//        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
//        // Find the EditText and Button views
//        var loginButton:EditText?=null
//
//        fragment?.let {
//            email = it.getEmail()
//            password = it.getPassword()
//        }
//        // Set an OnClickListener for the button
//        loginButton.setOnClickListener {
//            var email:EditText? = null
//            var password:EditText? = null
//            // Retrieve the text from the EditText when the button is clicked
//            fragment?.let {
//                email = it.getEmail()
//                password = it.getPassword()
//            }
//
//
////            Log.i("MainActivity", "username: $username password: $password")
//            getMyData(email?.text.toString(), password?.text.toString())
//        }


    }
//    private fun getMyData(username:String, password:String){
//        val retrofitBuilder = Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(BASE_URL)
//            .build()
//            .create(ApiInterface::class.java)
//
//        val loginRequest = LoginRequest(email=username, password=password)
//        val retrofitData = retrofitBuilder.login(loginRequest)
//        retrofitData.enqueue(object : Callback<LoginResponse> {
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                val responseBody = response.body()
//                Log.i("Response","${responseBody?.email}")
//
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                println(t.message)
//                println("hello")
//            }
//        })
//    }
}
