package com.example.cs496_pj2_front.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.cs496_pj2_front.databinding.ActivityProfileCalendarBinding
import java.util.*

class ProfileCalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileCalendarBinding
    private lateinit var pager: ViewPager2
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get Intent
        id = intent.getStringExtra("id")!!

        // Pager Binding
        val adapter = CalendarStateAdapter(this)
        pager = binding.pagerCalendar
        pager.adapter = adapter
        pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        pager.setCurrentItem(
            adapter.firstFragmentPosition, false
        )


    }

    inner class CalendarStateAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
        val firstFragmentPosition = Int.MAX_VALUE / 2

        override fun getItemCount(): Int = Int.MAX_VALUE

        override fun createFragment(position: Int): Fragment {
            return CalendarFragment(position, id)
        }
    }
}

