package com.example.cs496_pj2_front.model

import java.util.*
import kotlin.collections.ArrayList


data class Schedule(
    val year: Int,
    val month: Int,

    var scheduleList: ArrayList<ScheduleType> = arrayListOf()
)

class ScheduleType(
    val date: Date,
    val todo: String,
    val location: String,

    var friends: ArrayList<UUID> = arrayListOf()
)
