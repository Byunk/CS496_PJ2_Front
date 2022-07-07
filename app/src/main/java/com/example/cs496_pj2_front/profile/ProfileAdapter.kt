package com.example.cs496_pj2_front.profile

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cs496_pj2_front.databinding.RowProfileBinding
import com.example.cs496_pj2_front.model.Profile

class ProfileAdapter: RecyclerView.Adapter<ProfileAdapter.CustomViewHolder>() {

    private lateinit var binding: RowProfileBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        context = parent.context
        binding = RowProfileBinding.inflate(LayoutInflater.from(context))
        return CustomViewHolder(binding.root)
    }

    inner class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = binding.profileName
        val status = binding.profileStatus
        val image = binding.imgProfile

        fun bind(item: Profile) {
            name.text = item.name
            status.text = item.status
            // Fetching Img


            itemView.setOnClickListener {
                val intent = Intent(context, ProfileDetailActivity::class.java).apply {
                    putExtra("name", item.name)
                    putExtra("status", item.status)
                }.run { context.startActivity(this) }
            }

        }
    }

}