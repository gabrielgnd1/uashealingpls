package com.mnkdev.uashealing23

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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
        val destinationId = intent.getIntExtra("destination_id", -1)
        val fromFavorite = intent.getBooleanExtra("from_favorite", false)

        binding.textName.text = name
        binding.textCategory.text = category
        binding.textTagline.text = shortDescription
        binding.textDescription.text = description
        Picasso.get().load(imageUrl).into(binding.imageDetail)

        if (fromFavorite) {
            binding.btnAddToFavourite.text = "REMOVE FAVOURITE"
            binding.btnAddToFavourite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_delete_24, 0, 0, 0)
            binding.btnAddToFavourite.setOnClickListener {
                val userId = getSharedPreferences("USER_SESSION", MODE_PRIVATE).getInt("user_id", -1)
                if (userId != -1 && destinationId != -1) {
                    val url = "https://ubaya.xyz/native/160422018/uas/delete_favorite.php"
                    val request = object : StringRequest(Method.POST, url,
                        {
                            Toast.makeText(this, "Berhasil dihapus dari favorit", Toast.LENGTH_SHORT).show()
                            finish()
                        },
                        {
                            Toast.makeText(this, "Gagal menghapus", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        override fun getParams(): MutableMap<String, String> {
                            return hashMapOf(
                                "user_id" to userId.toString(),
                                "destination_id" to destinationId.toString()
                            )
                        }
                    }
                    Volley.newRequestQueue(this).add(request)
                }
            }
        } else {
            binding.btnAddToFavourite.text = "ADD TO FAVOURITE"
            binding.btnAddToFavourite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_24, 0, 0, 0)
            binding.btnAddToFavourite.setOnClickListener {
                val userId = getSharedPreferences("USER_SESSION", MODE_PRIVATE).getInt("user_id", -1)
                if (userId != -1 && destinationId != -1) {
                    val url = "https://ubaya.xyz/native/160422018/uas/add_favorite.php"
                    val request = object : StringRequest(Method.POST, url,
                        {
                            Toast.makeText(this, "Berhasil ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
                            finish()
                        },
                        {
                            Toast.makeText(this, "Gagal menambahkan ke favorit", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        override fun getParams(): MutableMap<String, String> {
                            return hashMapOf(
                                "user_id" to userId.toString(),
                                "destination_id" to destinationId.toString()
                            )
                        }
                    }
                    Volley.newRequestQueue(this).add(request)
                }
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

}