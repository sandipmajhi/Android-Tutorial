package com.example.myapplication.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.myapplication.ApiInterface
import com.example.myapplication.BASE_URL
import com.example.myapplication.LoginRequest
import com.example.myapplication.LoginResponse
import com.example.myapplication.R
import com.example.myapplication.SecondActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginFragment : Fragment() {


private var emailEditText: EditText? = null
private var passwordEditText: EditText? = null
private var loginButton: Button? = null
private var signupButton: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        emailEditText = view?.findViewById<EditText>(R.id.et_email)
        passwordEditText = view?.findViewById<EditText>(R.id.et_pass)
        loginButton = view?.findViewById<Button>(R.id.loginBtn)


        loginButton?.setOnClickListener {
            var email = emailEditText?.text.toString()
            var password = passwordEditText?.text.toString()
            getMyData(email, password)
        }


        return view
    }
    private fun getMyData(username:String, password:String){
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val loginRequest = LoginRequest(email=username, password=password)
        val retrofitData = retrofitBuilder.login(loginRequest)
        retrofitData.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val responseBody = response.body()
                Log.i("Response","${responseBody?.email}")
                Log.i("Response","${responseBody?.refresh}")

                if(response.code()==200){
                    val intent = Intent(requireContext(), SecondActivity::class.java)
                    intent.putExtra("access",responseBody?.access)
                    startActivity(intent)
                }


            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                println(t.message)
                println("hello")
            }
        })
    }

}