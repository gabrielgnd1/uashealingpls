package com.mnkdev.uashealing23

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            val prefs = requireActivity().getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()

            val intent = Intent(requireContext(), SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun loadUserData(view: View) {
        val prefs = requireActivity().getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)

        if (userId == -1) {
            Toast.makeText(requireContext(), "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "https://ubaya.xyz/native/160422018/uas/get_profile.php"
        val queue = Volley.newRequestQueue(requireContext())

        val request = object : StringRequest(Method.POST, url,
            { response ->
                val jsonObject = JSONObject(response)
                if (jsonObject.getBoolean("success")) {
                    val user = jsonObject.getJSONObject("user")
                    view.findViewById<TextInputEditText>(R.id.inputProfileName)?.setText(user.getString("name"))
                    view.findViewById<TextInputEditText>(R.id.inputProfileEmail)?.setText(user.getString("email"))
                    view.findViewById<TextInputEditText>(R.id.inputProfileJoined)?.setText(user.optString("created_at", "-"))
                    view.findViewById<TextInputEditText>(R.id.inputProfileFav)?.setText(user.optString("total_favourites", "0"))
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
                params["userId"] = userId.toString()
                return params
            }
        }

        queue.add(request)
    }

}
