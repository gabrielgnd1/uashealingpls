package com.mnkdev.uashealing23

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserData(view)
    }

    private fun loadUserData(view: View) {
        val url = "http://ubaya.xyz/get_profile.php"
        val queue = Volley.newRequestQueue(requireContext())

        val request = object : StringRequest(Method.POST, url,
            { response ->
                val jsonObject = JSONObject(response)
                if (jsonObject.getBoolean("success")) {
                    val user = jsonObject.getJSONObject("user")
                    view.findViewById<TextInputEditText>(R.id.profileName)?.setText(user.getString("name"))
                    view.findViewById<TextInputEditText>(R.id.profileEmail)?.setText(user.getString("email"))
                    view.findViewById<TextInputEditText>(R.id.profileJoined)?.setText(user.getString("joined"))
                    view.findViewById<TextInputEditText>(R.id.profileFav)?.setText(user.getString("total_favourites"))
                } else {
                    Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Network error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["userId"] = "1" //
                return params
            }
        }

        queue.add(request)
    }
}
