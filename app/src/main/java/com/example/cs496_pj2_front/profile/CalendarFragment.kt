package com.example.cs496_pj2_front.profile

import android.content.Context
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cs496_pj2_front.APIService
import com.example.cs496_pj2_front.R
import com.example.cs496_pj2_front.databinding.FragmentCalendarBinding
import com.example.cs496_pj2_front.model.CustomCalendar
import com.example.cs496_pj2_front.model.Schedule
import com.example.cs496_pj2_front.model.ScheduleType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment(position: Int, id: UUID) : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private lateinit var mContext: Context
    private var pageIndex = position
    private val id = id

    lateinit var schedule: Schedule

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }

    private fun initView() {
        pageIndex -= (Int.MAX_VALUE / 2)

        val date = Calendar.getInstance().run {
            add(Calendar.MONTH, pageIndex)
            time
        }

        val currentDate: Date = date

        // Fetch Data
        val month = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()
        val year = SimpleDateFormat("yyyy", Locale.KOREA).format(date).toInt()
        val call = APIService.retrofitInterface.getUserSchedule(year, month)
        call.enqueue(object: Callback<Schedule> {
            override fun onFailure(call: Call<Schedule>, t: Throwable) {
                Log.e(tag, t.message!!)
            }

            override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {
                schedule = response.body()!!
            }
        })

        var datetime: String = SimpleDateFormat("yyyy년 MM월", Locale.KOREA).format(date.time)
        binding.calendarYearMonthText.text = datetime

        // Init Calendar VIew
        binding.calendarView.layoutManager = GridLayoutManager(mContext, CustomCalendar.DAYS_OF_WEEK)
        binding.calendarView.adapter = CalendarAdapter(mContext, binding.calendarLayout, currentDate, id, schedule)
    }
}