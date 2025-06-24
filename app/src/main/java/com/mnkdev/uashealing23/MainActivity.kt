package com.mnkdev.uashealing23

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.mnkdev.uashealing23.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val fragments: ArrayList<Fragment> = ArrayList()
    private lateinit var drawerLayout: DrawerLayout //init drawer
    private lateinit var navView: NavigationView //init view



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //bagian drawer
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_change_password -> {
                    Toast.makeText(this, "Change Password clicked", Toast.LENGTH_SHORT).show()
                    // TODO: Ganti ke fragment atau activity change password
                }
                R.id.nav_logout -> {
                    Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
                    // TODO: Tambahkan logic logout, misal hapus session dan navigate ke login
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        fragments.add(ExploreFragment())
        fragments.add(FavoriteFragment())
        fragments.add(ProfileFragment())
        binding.viewPager.adapter = ViewPagerAdapter(this, fragments)

        val navigateTo = intent.getStringExtra("navigateTo")
        if (navigateTo == "explore") {
            binding.viewPager.currentItem = 0
        }

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNav.selectedItemId = binding.bottomNav.menu.getItem(position).itemId
            }
        })

        binding.bottomNav.setOnItemSelectedListener {
            binding.viewPager.currentItem = when(it.itemId){
                R.id.itemExplore -> 0
                R.id.itemFav -> 1
                R.id.itemProfile -> 2
                else -> 0
            }
            true
        }
    }
}