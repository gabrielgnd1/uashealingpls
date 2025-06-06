package com.mnkdev.uashealing23

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnkdev.uashealing23.databinding.ActivitySignInBinding

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
            // Handle sign in button click
            // You can perform authentication by checking the email and password here
            val email = binding.txtInputEmail.text.toString()
            val password = binding.txtInputPassword.text.toString()
            // Add your authentication logic here
            // For example, check if the email and password are correct
            if (email == "your_email" && password == "your_password") {
                // Authentication successful, navigate to the main activity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(EMAILKEY, email)
                intent.putExtra(PASSWORDKEY, password)
                startActivity(intent)
                finish()
            }
            else {
                // Authentication failed, show an error message
                binding.txtInputEmail.error = "Invalid email"
                binding.txtInputPassword.error = "Invalid password"
            }
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