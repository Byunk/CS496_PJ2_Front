package com.example.cs496_pj2_front.profile

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cs496_pj2_front.databinding.RowProfileBinding
import com.example.cs496_pj2_front.model.Profile
import com.example.cs496_pj2_front.model.User

class ProfileAdapter(user: User): RecyclerView.Adapter<ProfileAdapter.CustomViewHolder>() {

    private lateinit var binding: RowProfileBinding
    private lateinit var context: Context
    private val data = user

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        context = parent.context
        binding = RowProfileBinding.inflate(LayoutInflater.from(context))

        return CustomViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return data.friends.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

    }

    inner class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = binding.profileName
        val status = binding.profileStatus
        val image = binding.imgProfile

        fun bind(item: User) {
            name.text = item.username
            status.text = item.status

            // Fetching Img

            // Listener
            itemView.setOnClickListener {
                val intent = Intent(context, ProfileDetailActivity::class.java)
                context.startActivity(intent)
            }

        }
    }

}