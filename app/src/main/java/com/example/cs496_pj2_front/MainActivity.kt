package com.example.cs496_pj2_front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.cs496_pj2_front.chat.ChatFragment
import com.example.cs496_pj2_front.databinding.ActivityMainBinding
import com.example.cs496_pj2_front.model.User
import com.example.cs496_pj2_front.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val NUM_PAGES = 2

class MainActivity : AppCompatActivity() {

    private lateinit var pager: ViewPager2
    private lateinit var binding: ActivityMainBinding
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id")!!
        val navBar: BottomNavigationView = binding.bottomNavigationView

        //Pager Configuration
        pager = binding.pagerMain
        pager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        pager.registerOnPageChangeCallback(
            object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    navBar.menu.getItem(position).isChecked = true
                }
            }
        )

        //NavBar Configuration
        navBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> pager.setCurrentItem(0)
                //R.id.nav_chat -> pager.setCurrentItem(1)
            }
            true
        }

    }

    private inner class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fm, lifecycle) {
        override fun getItemCount(): Int {
            return NUM_PAGES
        }

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return ProfileFragment(id)
                //1 -> return ChatFragment()
            }
            return ProfileFragment(id)
        }
    }
}