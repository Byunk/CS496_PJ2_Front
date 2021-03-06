package com.example.cs496_pj2_front

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cs496_pj2_front.databinding.ActivitySignupBinding
import com.example.cs496_pj2_front.model.SignupRequest
import com.example.cs496_pj2_front.service.APIService
import com.example.cs496_pj2_front.service.FAILURE
import com.example.cs496_pj2_front.service.ResponseCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                val request = SignupRequest(id, password, username, null)
                val call = APIService.retrofitInterface.executeSignup(request)

                call.enqueue(object: Callback<ResponseCode> {
                    override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                        Log.e(APIService.TAG, t.message!!)
                        Toast.makeText(this@SignupActivity, "???????????? ??????", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ResponseCode>, response: Response<ResponseCode>) {
                        if (response.body()!!.code == FAILURE) {
                            Toast.makeText(this@SignupActivity, "???????????? ??????. ?????? ???????????????.", Toast.LENGTH_SHORT).show()
                        } else {
                            // Move to Login Activity
                            Toast.makeText(this@SignupActivity, "???????????? ??????", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                })
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }


    }

}