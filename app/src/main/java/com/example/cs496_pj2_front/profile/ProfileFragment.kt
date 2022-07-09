package com.example.cs496_pj2_front.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs496_pj2_front.LoginActivity
import com.example.cs496_pj2_front.databinding.FragmentProfileBinding
import com.example.cs496_pj2_front.databinding.RowProfileBinding
import com.example.cs496_pj2_front.model.Friends
import com.example.cs496_pj2_front.model.User
import com.example.cs496_pj2_front.service.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment(val id: String) : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var friendsData: Friends

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // Fetch Data
        val call = APIService.retrofitInterface.getUserFriends(id)
        call.enqueue(object: Callback<Friends> {
            override fun onFailure(call: Call<Friends>, t: Throwable) {
                Log.e(APIService.TAG, t.message!!)
            }

            override fun onResponse(call: Call<Friends>, response: Response<Friends>) {
                // Regard No Failure
                friendsData = response.body()!!
            }
        })

        // Binding
        val rvProfile = binding.rvProfile

        rvProfile.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rvProfile.adapter = ProfileAdapter()
    }

    inner class ProfileAdapter(): RecyclerView.Adapter<ProfileAdapter.CustomViewHolder>() {

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

        override fun getItemCount(): Int {
            return friendsData.friendsID.size + 1
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            // Set First row to My Profile
            if (position == 0) {
                holder.bind(id)
            } else {
                holder.bind(friendsData.friendsID[position-1])
            }
        }

        inner class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val name = binding.profileName
            val status = binding.profileStatus
            val image = binding.imgProfile

            fun bind(id: String) {

                // Fetching User Data
                val call = APIService.retrofitInterface.getUserById(id)
                call.enqueue(object: Callback<User> {
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(context, "Failed to Fetching User Data", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        // Regarding no failure
                        val userData = response.body()!!

                        name.text = userData.name
                        status.text = userData.status

                        // Fetching Image

                        // Click Listener
                        itemView.setOnClickListener {
                            val intent = Intent(context, ProfileDetailActivity::class.java)
                            intent.putExtra("userData", userData)
                            context.startActivity(intent)
                        }
                    }
                })
            }
        }

    }
}