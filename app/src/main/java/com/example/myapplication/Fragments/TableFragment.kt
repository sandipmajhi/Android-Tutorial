package com.example.myapplication.Fragments

import EmployeeAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ApiInterface
import com.example.myapplication.BASE_URL
import com.example.myapplication.EmpaView
import com.example.myapplication.R
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TableFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var employeeAdapter: EmployeeAdapter
    private var accessToken: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_table, container, false)

        // Get the access token from arguments
        accessToken = arguments?.getString("access_token")

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        employeeAdapter = EmployeeAdapter(listOf())
        recyclerView.adapter = employeeAdapter

        fetchEmployeeDetails(accessToken)

        return view
    }

    private fun fetchEmployeeDetails(access_token: String?) {
        // Ensure the token has the correct Bearer prefix
        val authToken = "Bearer $access_token"

        // Create an Interceptor to add the authorization header
        val authInterceptor = Interceptor { chain ->
            val originalRequest: Request = chain.request()
            val requestWithAuth: Request = originalRequest.newBuilder()
                .header("Authorization", authToken)
                .build()
            chain.proceed(requestWithAuth)
        }

        // Add the Interceptor to OkHttpClient
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        // Build Retrofit with OkHttpClient
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
            .create(ApiInterface::class.java)

        // Prepare the request
        val retrofitData = retrofitBuilder.viewallempausers()

        // Make the API call
        retrofitData.enqueue(object : Callback<List<EmpaView>> {
            override fun onResponse(
                call: Call<List<EmpaView>>,
                response: Response<List<EmpaView>>
            ) {
                val responseBody = response.body()
                Log.i("Response", "$responseBody")
                println(response.code())
                if (response.code() == 200) {
                    employeeAdapter.updateData(responseBody)
                    Log.i("Response", "$responseBody")
                } else {
                    Toast.makeText(
                        context,
                        "Error: ${response.code()} - ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("Error", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<EmpaView>>, t: Throwable) {
                Log.e("Failure", "API call failed: ${t.message}")
            }
        })
    }
}
