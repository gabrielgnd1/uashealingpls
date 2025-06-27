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
import com.mnkdev.uashealing23.databinding.FragmentProfileBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserData(view)
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
                    binding.inputProfileName.setText(user.getString("name"))
                    binding.inputProfileEmail.setText(user.getString("email"))
                    val createdAtString = user.optString("created_at", "-")
                    if (createdAtString != "-") {
                        try {
                            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val date = inputFormat.parse(createdAtString)
                            binding.inputProfileJoined.setText(date?.let { outputFormat.format(it) } ?: "-")
                        } catch (e: Exception) {
                            binding.inputProfileJoined.setText("-")
                        }
                    } else {
                        binding.inputProfileJoined.setText("-")
                    }
                    binding.inputProfileFav.setText(user.optString("total_favourites", "0"))
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
