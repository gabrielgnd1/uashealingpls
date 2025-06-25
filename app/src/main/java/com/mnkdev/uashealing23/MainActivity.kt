package com.mnkdev.uashealing23

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.mnkdev.uashealing23.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private val fragments: ArrayList<Fragment> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Healing Yuk!"


        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,  // <-- ini penting agar hamburger muncul
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()


        fragments.add(ExploreFragment())
        fragments.add(FavoriteFragment())
        fragments.add(ProfileFragment())
        binding.viewPager.adapter = ViewPagerAdapter(this, fragments)


        val navigateTo = intent.getStringExtra("navigateTo")
        if (navigateTo == "explore") {
            binding.viewPager.currentItem = 0
        }


        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNav.selectedItemId =
                    binding.bottomNav.menu.getItem(position).itemId
            }
        })


        binding.bottomNav.setOnItemSelectedListener {
            binding.viewPager.currentItem = when (it.itemId) {
                R.id.itemExplore -> 0
                R.id.itemFav -> 1
                R.id.itemProfile -> 2
                else -> 0
            }
            true
        }

        // drawer item click
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itemChangePass -> {
                    val intent = Intent(this, ChangePassword::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.itemLogout -> {
                    val prefs = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.clear()  //delete session
                    editor.apply()
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        val prefs = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val name = prefs.getString("user_name", "User") ?: "User"
        val editor = prefs.edit()
        editor.putString("user_name", "John Doe")  // Ganti dengan nama pengguna yang sebenarnya
        editor.apply()

       //ganti nama di drawer jadi name
        val headerView = binding.navView.getHeaderView(0) // Mengambil tampilan header
        val userNameTextView: TextView = headerView.findViewById(R.id.textViewUserName)
        userNameTextView.text = "Welcome, $name"

    }

    //hamburger
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return if (drawerToggle.onOptionsItemSelected(item)) true
        else super.onOptionsItemSelected(item)
    }
}
