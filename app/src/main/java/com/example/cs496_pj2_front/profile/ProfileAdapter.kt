package com.example.cs496_pj2_front.profile

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cs496_pj2_front.APIService
import com.example.cs496_pj2_front.databinding.RowProfileBinding
import com.example.cs496_pj2_front.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ProfileAdapter(data: User): RecyclerView.Adapter<ProfileAdapter.CustomViewHolder>() {

    private lateinit var binding: RowProfileBinding
    private lateinit var context: Context
    private val ids = data.friends

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        context = parent.context
        binding = RowProfileBinding.inflate(LayoutInflater.from(context))

        return CustomViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return ids.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(ids[position])
    }

    inner class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = binding.profileName
        val status = binding.profileStatus
        val image = binding.imgProfile

        fun bind(item: UUID) {
            // Fetching User Data
            val call = APIService.retrofitInterface.getUserById(item)
            call.enqueue(object: Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(context, "Failed to Fetching User Data", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    // Regarding no failure
                    val user = response.body()!!
                    name.text = user.name
                    status.text = user.status

                    // Fetching Image

                    // Click Listener
                    itemView.setOnClickListener {
                        val intent = Intent(context, ProfileDetailActivity::class.java)
                        intent.putExtra("userData", user)
                        context.startActivity(intent)
                    }
                }
            })
        }
    }

}