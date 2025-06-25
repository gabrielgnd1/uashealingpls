package com.mnkdev.uashealing23

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mnkdev.uashealing23.databinding.ActivityChangePasswordBinding
import org.json.JSONObject

class ChangePassword : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle the Change Password button click
        binding.btnSubmitChange.setOnClickListener {
            val currentPassword = binding.inputCurrentPassword.text.toString().trim()
            val newPassword = binding.inputNewPassword.text.toString().trim()
            val repeatPassword = binding.inputConfirmPassword.text.toString().trim()

            // Validasi input
            if (currentPassword.isEmpty() || newPassword.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != repeatPassword) {
                binding.inputNewPassword.error = "Passwords do not match"
                binding.inputConfirmPassword.error = "Passwords do not match"
                return@setOnClickListener
            }

            // Retrieve user email from SharedPreferences
            val prefs = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
            val email = prefs.getString("user_email", "") ?: ""

            if (email.isEmpty()) {
                Toast.makeText(this, "User session expired. Please log in again.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Send request to change password
            changePassword(email, currentPassword, newPassword)
        }
    }

    private fun changePassword(email: String, currentPassword: String, newPassword: String) {
        val url = "https://ubaya.xyz/native/160422018/uas/auth/changepass.php"
        val queue = Volley.newRequestQueue(this)

        // Create the request to change the password
        val stringRequest = object : StringRequest(Request.Method.POST, url,
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getBoolean("success")
                    val message = jsonObject.getString("message")

                    if (success) {
                        Toast.makeText(this, "Password successfully updated!", Toast.LENGTH_SHORT).show()

                        // Hapus sesi pengguna dari SharedPreferences
                        val prefs = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
                        val editor = prefs.edit()
                        editor.remove("user_email")  // Hapus email pengguna
                        editor.apply()  // Simpan perubahan

                        // Redirect to login screen
                        val intent = Intent(this, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to change password: $message", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error processing the response.", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Network error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email
                params["password"] = currentPassword
                params["new_password"] = newPassword
                params["confirm_password"] = newPassword
                return params
            }
        }

        queue.add(stringRequest)
    }
}
