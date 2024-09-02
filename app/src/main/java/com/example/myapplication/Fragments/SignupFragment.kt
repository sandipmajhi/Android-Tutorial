
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.ApiInterface
import com.example.myapplication.BASE_URL
import com.example.myapplication.R
import com.example.myapplication.SignupRequest
import com.example.myapplication.SignupResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        val access_token = arguments?.getString("access_token")?:"null"
        val signupButton = view.findViewById<Button>(R.id.signupBtn)
        val usernameEditText = view.findViewById<EditText>(R.id.et_username)
        val passwordEditText = view.findViewById<EditText>(R.id.et_pass)
        val roleSpinner = view.findViewById<Spinner>(R.id.spinner_role)
        val emailEditText = view.findViewById<EditText>(R.id.et_email)

        val roles = arrayOf("Level A", "Level S") // Your roles
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roles)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        roleSpinner.adapter = adapter

        signupButton.setOnClickListener {
            val username = usernameEditText?.text.toString()
            val password = passwordEditText?.text.toString()
            val email = emailEditText?.text.toString()
            val role = roleSpinner.selectedItem.toString()
            println(access_token)
            if (role == "Level A"){
                registerEmpA(username, email, role, password, access_token)
            }else if(role == "Level S"){
                registerEmpS(username, email, role, password, access_token)
            }

        }

        return view
    }

    private fun registerEmpA(username: String, email: String, role: String, password: String, access_token: String) {
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
        val signupRequest = SignupRequest(email = email, username = username, role = role, password = password)
        val retrofitData = retrofitBuilder.emparegister(signupRequest)

        // Make the API call
        retrofitData.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                val responseBody = response.body()
                Log.i("Response", "${responseBody?.email}")
                println(response.code())
                if (response.code() == 201) {
                    showCustomDialog(responseBody?.status)
                    Log.i("Response","$responseBody")
                } else {
                    showCustomDialog("Error: ${response.code()} - ${response.message()}")
                    Toast.makeText(context, "Error: ${response.code()} - ${response.message()}", Toast.LENGTH_LONG).show()
                    Log.e("Error", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Log.e("Failure", "API call failed: ${t.message}")
            }
        })
    }

    private fun showCustomDialog(message: String?) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.signup_popup)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val okBtn = dialog.findViewById<Button>(R.id.okBtn)
        val messageTextView = dialog.findViewById<TextView>(R.id.messageId)

        messageTextView.text = message

        okBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()


    }

    private fun registerEmpS(username: String, email: String, role: String, password: String, access_token: String) {
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
        val signupRequest = SignupRequest(email = email, username = username, role = role, password = password)
        val retrofitData = retrofitBuilder.empsregister(signupRequest)

        // Make the API call
        retrofitData.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                val responseBody = response.body()
                Log.i("Response", "${responseBody?.email}")
                println(response.code())
                if (response.code() == 201) {
                    showCustomDialog(responseBody?.status)
                    Log.i("Response","$responseBody")
                } else {
                    showCustomDialog("Error: ${response.code()} - ${response.message()}")
                    Toast.makeText(context, "Error: ${response.code()} - ${response.message()}", Toast.LENGTH_LONG).show()
                    Log.e("Error", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show()
                Log.e("Failure", "API call failed: ${t.message}")
            }
        })
    }
}
