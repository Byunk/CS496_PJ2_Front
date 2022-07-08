package com.example.cs496_pj2_front.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.ParcelUuid
import com.example.cs496_pj2_front.R
import com.example.cs496_pj2_front.databinding.ActivityProfileDetailBinding
import com.example.cs496_pj2_front.model.User

class ProfileDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // binding
        val tvFood = binding.tvFoodDes
        val tvHobby = binding.tvHobbyDes
        val tvFavorites = binding.tvFavoritesDes
        val tvWeekend = binding.tvWeekendDes
        val name = binding.tvName
        val status = binding.tvStatus
        val card = binding.cardProfile
        val imgProfile = binding.imgProfile

        // Fetching Profile Data
        val data = intent.getParcelableExtra<User>("userData")!!
        tvFood.text = data.food + "를 잘 먹어요!"
        tvHobby.text = data.hobby + "를 좋아해요"
        tvFavorites.text = data.favorites + "에 관심 있어요!"
        tvWeekend.text = data.weekend + "하는 편이에요!"
        name.text = data.name
        status.text = data.status

        // Background Click Event
        card.setOnClickListener {
            val id = data.id
            val intent = Intent(this, ProfileCalendarActivity::class.java)
            intent.putExtra("userId", ParcelUuid(id))
            startActivity(intent)
        }


        // Profile Click Event

        // Editing

    }
}