package com.mnkdev.uashealing23

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request.Method
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mnkdev.uashealing23.databinding.ActivityNewLocationBinding
import org.json.JSONObject

class NewLocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewLocationBinding
    private val categoryList: MutableList<Category> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchCategoriesAndSetupSpinner()

        binding.btnAddLoc.setOnClickListener {
            val name = binding.inputLocName.text.toString().trim()
            val category = binding.spinnerCategory.selectedItem as Category
            val short_description = binding.inputShortDesc.text.toString().trim()
            val description = binding.inputDesc.text.toString().trim()
            val image_url = binding.inputURLLocation.text.toString().trim()
            if (name.isEmpty() || category.name.isEmpty() || short_description.isEmpty() || description.isEmpty() || image_url.isEmpty() ) {
                binding.inputLocName.error = "Please fill in all fields"
                return@setOnClickListener
            }

            val destination = Explore(id = 0, name, category.id.toString(), short_description, description, image_url)
            addNewDestination(destination)
        }
    }

    private fun fetchCategoriesAndSetupSpinner() {
        val url = "https://ubaya.xyz/native/160422018/uas/get_categories.php"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(
            Method.GET, url,
            { response ->
                Log.d("api_categories_success", response)
                val jsonArray = JSONObject(response).getJSONArray("categories")
                for (i in 0 until jsonArray.length()) {
                    val categoryObj = jsonArray.getJSONObject(i)
                    categoryList.add(Category(categoryObj.getInt("id"), categoryObj.getString("name")))
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCategory.adapter = adapter
            },
            { error ->
                Log.e("api_categories_error", "Volley error: ${error.message}")
                Toast.makeText(this, "Failed to load categories", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(stringRequest)
    }

    private fun addNewDestination(destination: Explore) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://ubaya.xyz/native/160422018/uas/add_new_exploration.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                Log.d("api_add_success", response)
                try {
                    val obj = JSONObject(response)
                    val result = obj.getBoolean("success")
                    val message = obj.getString("message")

                    if (result) {
                        Toast.makeText(this, "${destination.name} added successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("navigateTo", "explore")
                        startActivity(intent)
                        finish()
                    }
                    else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e: Exception) {
                    Log.e("api_add_error", "Error parsing response: ${e.message}")
                    Toast.makeText(this, "Response Error", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.e("api_add_error", "Volley error: ${error.message}")
                Toast.makeText(this, "Network Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["name"] = destination.name
                params["category"] = destination.category
                params["short_description"] = destination.short_description
                params["description"] = destination.description
                params["image_url"] = destination.image_url
                return params
            }
        }

        Log.d("api_add_request", "Sending request to add new destination: $destination")
        queue.add(stringRequest)
    }
}