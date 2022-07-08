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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get Intent
        val id: UUID = intent.getParcelableExtra("userId")!!

        // Pager Binding
        val adapter = CalendarStateAdapter(this, id)
        pager = binding.pagerCalendar
        pager.adapter = adapter
        pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        pager.setCurrentItem(
            adapter.firstFragmentPosition, false
        )


    }
}

class CalendarStateAdapter(fragmentActivity: FragmentActivity, id: UUID): FragmentStateAdapter(fragmentActivity) {
    val firstFragmentPosition = Int.MAX_VALUE / 2
    val id = id

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        return CalendarFragment(position, id)
    }
}