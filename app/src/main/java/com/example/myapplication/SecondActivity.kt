package com.example.myapplication

import SignupFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.example.myapplication.Fragments.TableFragment
import com.google.android.material.navigation.NavigationView
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SecondActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val access_token = intent.getStringExtra("access")?:"null"
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        val toggleButton = findViewById<View>(R.id.drawer_icon)
        toggleButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Handle item selection from the drawer menu
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_register -> {
                    // Handle the "Register Employee" action
                    println("employee a register")
                    openSignupFragment(access_token)
                }
                R.id.nav_view_all -> {
                    // Handle the "View All Employees" action
                    println("view employee a")
                    openTableFragment(access_token)
                }
                R.id.nav_logout -> {
                    // Handle the "Log Out" action
                    println("logout")
                    logout(access_token)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START) // Close the drawer after selection
            true
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun openSignupFragment(access_token:String) {
        val fragment = SignupFragment()
        val bundle = Bundle()
        bundle.putString("access_token", access_token)
        fragment.arguments = bundle
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.linearLayout, fragment)
        fragmentTransaction.addToBackStack(null) // Optional: Add to back stack
        fragmentTransaction.commit()

    }

    private fun openTableFragment(access_token: String) {
        val fragment = TableFragment()
        val bundle = Bundle()
        bundle.putString("access_token", access_token)
        fragment.arguments = bundle
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.linearLayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    private fun logout(access_token: String) {
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

        val retrofitData = retrofitBuilder.logout()

        // Make the API call
        retrofitData.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                val responseBody = response.body()
                Log.i("Response", "${responseBody?.msg}")
                println(response.code())
                if (response.code() == 200) {
                    Log.i("Response","$responseBody")
                    val intent = Intent(this@SecondActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@SecondActivity, "Error: ${response.code()} - ${response.message()}", Toast.LENGTH_LONG).show()
                    Log.e("Error", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                Log.e("Failure", "API call failed: ${t.message}")
            }
        })
    }

}
