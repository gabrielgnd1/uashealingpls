package com.mnkdev.uashealing23

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mnkdev.uashealing23.databinding.ActivitySignInBinding
import org.json.JSONObject

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    companion object {
        const val EMAILKEY = "emailkey"
        const val PASSWORDKEY = "passwordkey"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            val email = binding.txtInputEmail.text.toString().trim()
            val password = binding.txtInputPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val q = Volley.newRequestQueue(this)
            val url = "https://ubaya.xyz/native/160422018/uas/login.php"

            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                { response ->
                    Log.d("api_login_success", response)
                    try {
                        val obj = JSONObject(response)
                        val result = obj.getBoolean("success")
                        val message = obj.getString("message")

                        if (result) {
                            val user = obj.getJSONObject("user")
                            val name = user.getString("name")

                            Toast.makeText(this, "Welcome, $name!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra(EMAILKEY, email)
                            intent.putExtra(PASSWORDKEY, password)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Login Failed: $message", Toast.LENGTH_LONG).show()
                        }
                    }
                    catch (e: Exception) {
                        Log.e("api_login_error", "Parsing error: ${e.message}")
                        Toast.makeText(this, "Login response error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                },
                { error ->
                    Log.e("api_login_failed", "Volley error: ${error.message}")
                    Toast.makeText(this, "Login failed: ${error.message}", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["email"] = email
                    params["password"] = password
                    return params
                }
            }

            Log.d("debug_params", "Sending: email=$email, password=$password")
            q.add(stringRequest)
        }

        binding.btnSignUp.setOnClickListener {
            // Handle sign up button click
            // You can navigate to the sign up activity or perform any other action here
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}