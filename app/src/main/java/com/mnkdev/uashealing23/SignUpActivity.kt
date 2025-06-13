package com.mnkdev.uashealing23

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mnkdev.uashealing23.databinding.ActivitySignUpBinding
import com.google.gson.Gson
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            val name = binding.txtInputName.text.toString().trim()
            val email = binding.txtInputEmail.text.toString().trim()
            val password = binding.txtInputPassword.text.toString().trim()
            val repeatPassword = binding.txtInputRepeatPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != repeatPassword) {
                binding.txtInputPassword.error = "Passwords do not match"
                binding.txtInputRepeatPassword.error = "Passwords do not match"
                return@setOnClickListener
            }

            val user = User(name, email, password)
            registerUser(user)
        }
    }

    private fun registerUser(user: User) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://ubaya.xyz/native/160422018/uas/register.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Log.d("api_register_success", response)
                try {
                    val obj = JSONObject(response)
                    val result = obj.getBoolean("success")
                    val message = obj.getString("message")

                    if (result) {
                        Toast.makeText(this, "Register Success!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SignInActivity::class.java)
                        intent.putExtra("name", user.name)
                        intent.putExtra("email", user.email)
                        intent.putExtra("password", user.password)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Register Failed: $message", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Log.e("api_register_error", "Parsing error: ${e.message}")
                    Toast.makeText(this, "Response Error", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.e("api_register_failed", "Volley error: ${error.message}")
                Toast.makeText(this, "Network error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["name"] = user.name
                params["email"] = user.email
                params["password"] = user.password
                params["confirm_password"] = user.password
                return params
            }
        }

        Log.d("debug_params", "Sending: name=${user.name}, email=${user.email}, password=${user.password}")
        queue.add(stringRequest)
    }
}