package com.example.cs496_pj2_front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServerConnectingTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_connecting_test)

        val call = APIService.retrofitInterface.test()
        call.enqueue(object: Callback<ResponseCode> {
            override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                Log.e("Test", t.message!!)
            }

            override fun onResponse(call: Call<ResponseCode>, response: Response<ResponseCode>) {
                Log.i("Test", response.body().toString())
            }
        })
    }
}