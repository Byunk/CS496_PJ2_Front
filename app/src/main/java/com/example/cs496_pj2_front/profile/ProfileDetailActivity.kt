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
        val userData: User = intent.getParcelableExtra("userData")!!
        tvFood.text = userData.food + "를 잘 먹어요!"
        tvHobby.text = userData.hobby + "를 좋아해요"
        tvFavorites.text = userData.favorites + "에 관심 있어요!"
        tvWeekend.text = userData.weekend + "하는 편이에요!"
        name.text = userData.name
        status.text = userData.status

        // Background Click Event
        card.setOnClickListener {
            val id = userData.id
            val intent = Intent(this, ProfileCalendarActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }


        // Profile Click Event

        // Editing

    }
}