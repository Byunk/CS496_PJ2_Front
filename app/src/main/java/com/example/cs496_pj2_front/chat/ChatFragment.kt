package com.example.cs496_pj2_front.chat

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startForegroundService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cs496_pj2_front.R
import com.example.cs496_pj2_front.databinding.FragmentChatBinding
import com.example.cs496_pj2_front.databinding.RowChatBinding
import com.example.cs496_pj2_front.databinding.RowProfileBinding
import com.example.cs496_pj2_front.model.Chat
import com.example.cs496_pj2_front.model.User
import com.example.cs496_pj2_front.profile.ProfileDetailActivity
import com.example.cs496_pj2_front.profile.ProfileFragment
import com.example.cs496_pj2_front.service.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatFragment(val id: String) : Fragment() {

    lateinit var binding: FragmentChatBinding
    private var chats: ArrayList<Chat>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val call = APIService.retrofitInterface.getChatsById(id)
        call.enqueue(object: Callback<ArrayList<Chat>> {
            override fun onFailure(call: Call<ArrayList<Chat>>, t: Throwable) {
                Log.e(APIService.TAG, t.message!!)
            }

            override fun onResponse(
                call: Call<ArrayList<Chat>>,
                response: Response<ArrayList<Chat>>
            ) {
                if (response.body() != null) {
                    chats = response.body()!!
                } else {
                    chats = arrayListOf()
                }

                val rvChat = binding.rvChat
                val emptyChat = binding.tvEmptyChat

                if (chats!!.isEmpty()) {
                    rvChat.visibility = View.INVISIBLE
                    emptyChat.visibility = View.VISIBLE
                } else {
                    rvChat.visibility = View.VISIBLE
                    emptyChat.visibility = View.INVISIBLE

                    rvChat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    rvChat.adapter = ChatAdapter(chats!!)
                }
            }
        })


    }
}

class ChatAdapter(private val chats: ArrayList<Chat>): RecyclerView.Adapter<ChatAdapter.CustomViewHolder>() {

    private lateinit var binding: RowChatBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        context = parent.context
        binding = RowChatBinding.inflate(LayoutInflater.from(context))

        return CustomViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    inner class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title = binding.tvChatTitle
        val numPeople = binding.tvNumPpl
        val lastChat = binding.tvLastChat

        fun bind(chat: Chat) {
            title.text = chat.title
            numPeople.text = chat.users.size.toString()
            lastChat.text = chat.chatLog.last()
        }
    }

}