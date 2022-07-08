package com.example.cs496_pj2_front.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs496_pj2_front.LoginActivity
import com.example.cs496_pj2_front.databinding.FragmentProfileBinding
import com.example.cs496_pj2_front.model.User

class ProfileFragment(data: User) : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val data: User = data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // Binding
        val rvProfile = binding.rvProfile

        rvProfile.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rvProfile.adapter = ProfileAdapter(data)
    }
}