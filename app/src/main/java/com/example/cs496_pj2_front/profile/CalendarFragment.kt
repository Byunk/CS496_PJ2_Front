package com.example.cs496_pj2_front.profile

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs496_pj2_front.R
import com.example.cs496_pj2_front.service.APIService
import com.example.cs496_pj2_front.databinding.FragmentCalendarBinding
import com.example.cs496_pj2_front.model.CustomCalendar
import com.example.cs496_pj2_front.model.Schedule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment(position: Int, val id: String) : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private lateinit var mContext: Context
    private var pageIndex = position

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

        val datetime: String = SimpleDateFormat("yyyy년 MM월", Locale.KOREA).format(date.time)
        binding.calendarYearMonthText.text = datetime

        // Init Calendar VIew
        binding.calendarView.layoutManager = GridLayoutManager(mContext, CustomCalendar.DAYS_OF_WEEK)
        binding.calendarView.adapter = CalendarAdapter(currentDate)
    }

    inner class CalendarAdapter(val date: Date)
        : RecyclerView.Adapter<CalendarAdapter.CalendarItemHolder>() {

        var datelist: ArrayList<Int> = arrayListOf()
        var customCalendar: CustomCalendar = CustomCalendar(date)

        val calendarMonth = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()
        val calendarYear = SimpleDateFormat("yyyy", Locale.KOREA).format(date).toInt()

        var schedules: ArrayList<Schedule>? = null

        init {
            customCalendar.initBaseCalendar()
            datelist = customCalendar.dateList
        }

        override fun onBindViewHolder(holder: CalendarItemHolder, position: Int) {
            // Configure Layout
            val h = binding.calendarLayout.height / 6
            holder.itemView.layoutParams.height = h

            holder.bind(datelist[position], position, mContext)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarItemHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.calendar, parent, false)

            // Fetch Data
            val year = SimpleDateFormat("yyyy", Locale.KOREA).format(date).toInt()
            val month = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()

            val call = APIService.retrofitInterface.getUserMonthlySchedule(id, year, month)
            call.enqueue(object: Callback<ArrayList<Schedule>> {
                override fun onFailure(call: Call<ArrayList<Schedule>>, t: Throwable) {
                    Log.e(APIService.TAG, t.message!!)
                }

                override fun onResponse(call: Call<ArrayList<Schedule>>, response: Response<ArrayList<Schedule>>) {
                    if (response.body() != null) {
                        schedules = response.body()!!
                    }
                }
            })

            return CalendarItemHolder(view)
        }

        override fun getItemCount(): Int{
            return datelist.size
        }

        inner class CalendarItemHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
            var itemCalendarDateText: TextView = itemView!!.findViewById(R.id.calendar)

            fun bind(date: Int, position: Int, context: Context) {
                val firstDateIndex = customCalendar.prevTail
                val lastDateIndex = datelist.size - customCalendar.nextHead - 1

                // 날짜 표시
                itemCalendarDateText.setText(date.toString())

                // Schedule 처리
                if (schedules != null) {
                    val index = schedules!!.indexOfFirst { it.date == datelist[position] }

                    if (index != -1) {
                        //TODO: Showing Mark
                        if (schedules!![index].todo != null) {
                            itemCalendarDateText.append("\n" + schedules!![index].todo!!)
                        }
                    }
                }

                // 오늘 날짜 처리
                val dateString: String = SimpleDateFormat("dd", Locale.KOREA).format(date)
                val dateInt = dateString.toInt()

                if ((datelist[position] == dateInt) && isMatchYearMonth() ){
                    itemCalendarDateText.setTypeface(itemCalendarDateText.typeface, Typeface.BOLD)
                    itemCalendarDateText.setTextSize(Dimension.SP, 25F)
                }

                // 현재 월의 1일 이전, 현재 월의 마지막일 이후 값의 텍스트를 회색처리
                if (position < firstDateIndex || position > lastDateIndex) {
                    itemCalendarDateText.setTextColor(
                        ContextCompat.getColor(
                            context,
                            Color.parseColor("#CCD7ED")!!
                        )
                    )
                }

                itemView.setOnClickListener {
                    val intent = Intent(context, CalendarDetailActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("year", calendarYear)
                    intent.putExtra("month", calendarMonth)
                    intent.putExtra("day", datelist[position])
                    context.startActivity(intent)
                }
            }

            private fun isMatchYearMonth(): Boolean {
                //val monthInt = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()
                val actualMonth = SimpleDateFormat("MM", Locale.KOREA).format(Date()).toInt()
                val actualYear = SimpleDateFormat("yyyy", Locale.KOREA).format(Date()).toInt()

                return calendarMonth == actualMonth && calendarYear == actualYear

            }

        }
    }

}