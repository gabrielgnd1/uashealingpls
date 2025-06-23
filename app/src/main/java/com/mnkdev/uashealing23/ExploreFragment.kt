package com.mnkdev.uashealing23

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Request.Method
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mnkdev.uashealing23.databinding.FragmentExploreBinding
import org.json.JSONObject

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private lateinit var adapter: ExploreAdapter
    var exploreList: ArrayList<Explore> = ArrayList()
    var categoryMap: Map<String, String> = mapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        fetchCategoriesThenExplore()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater, container, false)

        categoryMap = mapOf()

        binding.exploreRecView.layoutManager = LinearLayoutManager(requireContext())

        binding.fabExplore.setOnClickListener {
            val intent = Intent(requireContext(), NewLocationActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    fun fetchCategoriesThenExplore() {
        val q = Volley.newRequestQueue(requireContext())
        val categoryUrl = "https://ubaya.xyz/native/160422018/uas/get_categories.php"

        val categoryRequest = StringRequest(Method.GET, categoryUrl,
            { response ->
                Log.d("api_categories_success", response)
                val obj = JSONObject(response)
                val data = obj.getJSONArray("categories")
                val tempCategoryMap = mutableMapOf<String, String>()

                for (i in 0 until data.length()) {
                    val item = data.getJSONObject(i)
                    val id = item.getInt("id").toString()
                    val name = item.getString("name")
                    tempCategoryMap[id] = name
                }

                categoryMap = tempCategoryMap
                getExploreList()
            },
            { error ->
                Log.e("api_categories_error", error.message ?: "")
            }
        )

        q.add(categoryRequest)
    }

    fun getExploreList() {
        val q = Volley.newRequestQueue(requireContext())
        val url = "https://ubaya.xyz/native/160422018/uas/get_explore_list.php"

        val stringRequest = @SuppressLint("NotifyDataSetChanged")
        object: StringRequest(Method.GET, url,
            { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getBoolean("success")) {
                        Log.d("api_explore_success", response)
                        val data = obj.getJSONArray("exploreList")
                        val stype = object : TypeToken<ArrayList<Explore>>() {}.type

                        exploreList = Gson().fromJson(data.toString(), stype)

                        adapter = ExploreAdapter(exploreList)

                        binding.exploreRecView.adapter = adapter

                        adapter.notifyDataSetChanged()
                    }
                }
                catch (e: Exception) {
                    Log.e("api_explore_error", "Parse error: ${e.message}")
                }
            },
            { error ->
                Log.e("api_explore_error", "Volley error: ${error.message}")
            }
        ) {}

        q.add(stringRequest)
    }
}