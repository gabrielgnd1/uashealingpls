package com.mnkdev.uashealing23

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnkdev.uashealing23.databinding.ActivityHealingDetailBinding
import com.mnkdev.uashealing23.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso

class HealingDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHealingDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name") ?: "-"
        val category = intent.getStringExtra("category") ?: "-"
        val shortDescription = intent.getStringExtra("short_description") ?: "-"
        val description = intent.getStringExtra("description") ?: "-"
        val imageUrl = intent.getStringExtra("image_url") ?: ""

        binding.textName.text = name
        binding.textCategory.text = category
        binding.textTagline.text = shortDescription
        binding.textDescription.text = description
        Picasso.get().load(imageUrl).into(binding.imageDetail)

        Picasso.get()
            .load(imageUrl)
            .into(binding.imageDetail)

        binding.btnAddToFavourite.setOnClickListener {
            Toast.makeText(this, "$name ditambahkan ke daftar favorit", Toast.LENGTH_SHORT).show()
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}