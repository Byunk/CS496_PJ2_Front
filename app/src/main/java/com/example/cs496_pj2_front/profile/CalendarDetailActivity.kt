package com.example.cs496_pj2_front.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs496_pj2_front.databinding.ActivityCalendarDetailBinding
import com.example.cs496_pj2_front.databinding.RowCalendarDetailBinding
import com.example.cs496_pj2_front.model.Schedule
import com.example.cs496_pj2_front.model.User
import com.example.cs496_pj2_front.service.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CalendarDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarDetailBinding
    private lateinit var schedules: ArrayList<Schedule>

    private lateinit var id: String
    private var year = 0
    private var month = 0
    private var date = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id")!!
        year = intent?.getIntExtra("year", 0)!!
        month = intent?.getIntExtra("month", 0)!!
        date = intent?.getIntExtra("day", 0)!!
    }

    override fun onResume() {
        super.onResume()

        // Fetch Calendar Detail
        // Require: Year Month
        // OutPut: Schedule
        val call = APIService.retrofitInterface.getUserDailySchedule(id, year, month, date)
        call.enqueue(object: Callback<ArrayList<Schedule>> {
            override fun onFailure(call: Call<ArrayList<Schedule>>, t: Throwable) {
                Log.i(APIService.TAG, t.message!!)
            }

            override fun onResponse(call: Call<ArrayList<Schedule>>, response: Response<ArrayList<Schedule>>) {
                if (response.body() == null) {
                    // init Schdule
                    schedules = arrayListOf()
                } else {
                    schedules = response.body()!!
                    schedules.sortWith(compareBy( { it.hour }, { it.minute }))
                }
            }
        })

        // Recycler View
        val rvProfile = binding.rvCalendarDetail
        rvProfile.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvProfile.adapter = CalendarDetailAdapter()

    }

    inner class CalendarDetailAdapter: RecyclerView.Adapter<CalendarDetailAdapter.CustomViewHolder>() {

        private lateinit var binding: RowCalendarDetailBinding
        private lateinit var context: Context

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CustomViewHolder {
            context = parent.context
            binding = RowCalendarDetailBinding.inflate(LayoutInflater.from(context))
            return CustomViewHolder(binding.root)
        }

        override fun getItemCount(): Int {
            return schedules.size
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            holder.bind(schedules[position])
        }

        inner class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val time = binding.tvTime
            val todo = binding.tvTodo
            val friends = binding.tvFriends
            val location = binding.tvLocation

            fun bind(schedule: Schedule) {
                val timeText = schedule.hour.toString() + ":" + schedule.minute.toString()
                time.text = timeText
                todo.text = schedule.todo

                var friendsText = ""
                for (friend in schedule.friends) {
                    friendsText += friend
                }
                friends.text = friendsText
                location.text = schedule.location
            }
        }

    }
}