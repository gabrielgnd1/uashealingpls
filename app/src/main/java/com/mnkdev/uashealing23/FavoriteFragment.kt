package com.mnkdev.uashealing23

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mnkdev.uashealing23.databinding.FragmentFavoriteBinding
import org.json.JSONObject
import androidx.appcompat.app.AppCompatActivity

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: ExploreAdapter
    private var favoriteList: ArrayList<Explore> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding.favoriteRecView.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getFavoriteList()
    }

    private fun getFavoriteList() {
        val prefs = requireContext().getSharedPreferences("USER_SESSION", AppCompatActivity.MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)

        if (userId == -1) {
            Toast.makeText(requireContext(), "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "https://ubaya.xyz/native/160422018/uas/get_favorites.php?user_id=$userId"
        val q = Volley.newRequestQueue(requireContext())

        val stringRequest = @SuppressLint("NotifyDataSetChanged")
        object : StringRequest(Method.GET, url,
            { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getBoolean("success")) {
                        val data = obj.getJSONArray("favorites")
                        val sType = object : TypeToken<ArrayList<Explore>>() {}.type
                        favoriteList = Gson().fromJson(data.toString(), sType)

                        adapter = ExploreAdapter(favoriteList, isFavorite = true) { explore ->
                            val intent = Intent(requireContext(), HealingDetailActivity::class.java)
                            intent.putExtra("destination_id", explore.id)
                            intent.putExtra("name", explore.name)
                            intent.putExtra("category", explore.category)
                            intent.putExtra("short_description", explore.short_description)
                            intent.putExtra("description", explore.description)
                            intent.putExtra("image_url", explore.image_url)
                            intent.putExtra("from_favorite", true)
                            startActivity(intent)
                        }

                        binding.favoriteRecView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "Tidak ada favorit ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("api_fav_parse", "Parsing error: ${e.message}")
                }
            },
            { error ->
                Log.e("api_fav_error", "Volley error: ${error.message}")
            }
        ) {}
        q.add(stringRequest)
    }
}
