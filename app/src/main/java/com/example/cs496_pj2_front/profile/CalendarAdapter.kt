package com.example.cs496_pj2_front.profile

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.ParcelUuid
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cs496_pj2_front.R
import com.example.cs496_pj2_front.model.CustomCalendar
import com.example.cs496_pj2_front.model.Schedule
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(val context: Context, val calendarLayout: LinearLayout, val date: Date, val id: UUID, val schedule: Schedule)
    : RecyclerView.Adapter<CalendarAdapter.CalendarItemHolder>() {

    var dataList: ArrayList<Int> = arrayListOf()
    var customCalendar: CustomCalendar = CustomCalendar(date)

    val calendarMonth = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()
    val calendarYear = SimpleDateFormat("yyyy", Locale.KOREA).format(date).toInt()

    init {
        customCalendar.initBaseCalendar()
        dataList = customCalendar.dateList
    }

    override fun onBindViewHolder(holder: CalendarItemHolder, position: Int) {
        // Configure Layout
        val h = calendarLayout.height / 6
        holder.itemView.layoutParams.height = h

        holder.bind(dataList[position], position, context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarItemHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.calendar, parent, false)
        return CalendarItemHolder(view)
    }

    override fun getItemCount(): Int{
        return dataList.size
    }

    inner class CalendarItemHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var itemCalendarDateText: TextView = itemView!!.findViewById(R.id.calendar)

        fun bind(data: Int, position: Int, context: Context) {
            val firstDateIndex = customCalendar.prevTail
            val lastDateIndex = dataList.size - customCalendar.nextHead - 1

            // 날짜 표시
            itemCalendarDateText.setText(data.toString())

            // 오늘 날짜 처리
            val dateString: String = SimpleDateFormat("dd", Locale.KOREA).format(date)
            val dateInt = dateString.toInt()

            if ((dataList[position] == dateInt) && isMatchYearMonth() ){
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
                intent.putExtra("userId", ParcelUuid(id))
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
