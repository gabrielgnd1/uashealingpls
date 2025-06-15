package com.mnkdev.uashealing23

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
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

    fun updateExploreList() {
        binding.exploreRecView.adapter = ExploreAdapter(exploreList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val q = Volley.newRequestQueue(requireContext())
        val url = "https://ubaya.xyz/native/160422018/uas/get_explore_list.php"
        val stringRequest = StringRequest(Request.Method.POST, url,
            {
                // Success response
                Log.d("api_explore_success", it)
                val obj = JSONObject(it)

                if (obj.getString("result") == "success") {
                    val data = obj.getJSONArray("exploreList")

                    val stype = object: TypeToken<List<Explore>>(){}.type
                    exploreList = Gson().fromJson<List<Explore>>(data.toString(), stype) as ArrayList<Explore>
                    Log.d("hasil", exploreList.toString())
                    updateExploreList()
                }
            },
            {
                // Failed response
                Log.e("api_explore_error", it.toString())
            }
        )

        q.add(stringRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater, container, false)

        binding.exploreRecView.layoutManager = LinearLayoutManager(requireContext())
        binding.exploreRecView.adapter = ExploreAdapter(exploreList)

        return binding.root
    }

    fun getExploreList() {
        val q = Volley.newRequestQueue(requireContext())
        val url = "https://ubaya.xyz/native/160422018/uas/get_explore_list.php"

        val stringRequest = object: StringRequest(Method.GET, url,
            { response ->
                try {
                    val obj = JSONObject(response)
                    val data = obj.getJSONArray("exploreList")
                    exploreList.clear()

                    for (i in 0 until data.length()) {
                        val item = data.getJSONObject(i)
                        val explore = Explore(
                            item.getInt("id"),
                            item.getString("title"),
                            item.getString("category"),
                            item.getString("summary"),
                            item.getString("image_url"),
                            item.getBoolean("is_favorite")
                        )
                        exploreList.add(explore)
                    }
                    adapter.notifyDataSetChanged()
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