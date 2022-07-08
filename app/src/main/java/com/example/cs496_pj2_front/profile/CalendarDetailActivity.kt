package com.example.cs496_pj2_front.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs496_pj2_front.databinding.ActivityCalendarDetailBinding
import java.util.*

class CalendarDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id: UUID = intent.getParcelableExtra("userId")!!
    }

    override fun onResume() {
        super.onResume()

        // Fetch Calendar Detail


        // Recycler View
        val rvProfile = binding.rvCalendarDetail

        rvProfile.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //rvProfile.adapter = CalendarDetailAdapter()

    }
}