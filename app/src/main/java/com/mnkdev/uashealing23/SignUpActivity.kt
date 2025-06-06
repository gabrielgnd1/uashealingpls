package com.mnkdev.uashealing23

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnkdev.uashealing23.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            // Handle sign up button click
            // You can perform registration by saving the user data here
            val name = binding.txtInputName.text.toString()
            val email = binding.txtInputEmail.text.toString()
            val password = binding.txtInputPassword.text.toString()
            val repeatPassword = binding.txtInputRepeatPassword.text.toString()
            // Add your registration logic here
            // For example, check if the password and repeat password match
            if (password == repeatPassword) {
                // Registration successful, navigate to the main activity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("name", name)
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                startActivity(intent)
                finish()
            }
            else {
                // Passwords do not match, show an error message
                binding.txtInputPassword.error = "Passwords do not match"
                binding.txtInputRepeatPassword.error = "Passwords do not match"
            }
        }
    }
}