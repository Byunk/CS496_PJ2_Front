package com.example.cs496_pj2_front

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cs496_pj2_front.databinding.ActivitySignupBinding
import com.example.cs496_pj2_front.model.Login
import com.example.cs496_pj2_front.model.Profile
import com.example.cs496_pj2_front.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Binding
        val etUsername = binding.etUsername
        val etId = binding.etId
        val etPassword = binding.etPassword
        val etPasswordCheck = binding.etPasswordCheck
        val btnCancel = binding.btnCancel
        val btnSignup = binding.btnSignup

        // Check Login Path

        // Set Listeners
        btnSignup.setOnClickListener {
            val username = etUsername.text.toString()
            val id = etId.text.toString()
            val password = etPassword.text.toString()
            val passwordCheck = etPasswordCheck.text.toString()

            if (password != passwordCheck) {
                Toast.makeText(this, "Password Invalid", Toast.LENGTH_SHORT).show()
                // Invalid Effect

            } else {
                val call = APIService.retrofitInterface.executeSignup(username, id, password, null)

                call.enqueue(object: Callback<User> {
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.e(ContentValues.TAG, t.message!!)
                        Toast.makeText(this@SignupActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        // Move to Main Activity
                        Toast.makeText(this@SignupActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                })
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }


    }

}