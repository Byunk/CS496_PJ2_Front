package com.example.cs496_pj2_front

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cs496_pj2_front.databinding.ActivityLoginBinding
import com.example.cs496_pj2_front.model.LoginResponse
import com.example.cs496_pj2_front.model.LoginRequest
import com.example.cs496_pj2_front.model.SignupRequest
import com.example.cs496_pj2_front.model.User
import com.example.cs496_pj2_front.service.APIService
import com.example.cs496_pj2_front.service.FAILURE
import com.example.cs496_pj2_front.service.ResponseCode
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.Account
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val context = this

    lateinit var data: User
    lateinit var kakaoData: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hash Key
        //val keyHash = Utility.getKeyHash(this)
        //Log.i(TAG, "Hash Key: " + keyHash)

        // Binding Buttons
        val btnLogin = binding.btnLogin
        val btnSignup = binding.btnSignup
        val btnKakaoLogin = binding.btnKakaoSignin

        // Login
        btnLogin.setOnClickListener {
            val id = binding.etId.text.toString()
            val pw = binding.etPassword.text.toString()
            executeLogin(id, pw)
        }

        // Signup
        btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        // Kakao Login
        btnKakaoLogin.setOnClickListener {
            // Check Login Token Info
            if (AuthApiClient.instance.hasToken()) {
                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                    if (error != null) {
                        Toast.makeText(this, "?????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show()
                    }
                    else if (tokenInfo != null) {
                        Toast.makeText(this, "?????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show()
                        val kakaoId = tokenInfo.id!!
                        executeLogin(kakaoId)
                    }
                }
            }

            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "????????????????????? ????????? ??????", error)
                } else if (token != null) {
                    Log.i(TAG, "????????????????????? ????????? ?????? ${token.accessToken}")
                    executeKakaoLogin()
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "?????????????????? ????????? ??????", error)

                        // ???????????? ???????????? ?????? ??? ???????????? ?????? ?????? ???????????? ???????????? ????????? ??????,
                        // ???????????? ????????? ????????? ?????? ????????????????????? ????????? ?????? ?????? ????????? ????????? ?????? (???: ?????? ??????)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // ??????????????? ????????? ?????????????????? ?????? ??????, ????????????????????? ????????? ??????
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show()
                        executeKakaoLogin()
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    //region Login Logic
    private fun loginCallback(call: Call<LoginResponse>) {
        call.enqueue(object: Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(APIService.TAG, t.message!!)
                Toast.makeText(context, "????????? ??????", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.i(APIService.TAG, response.body()?.id!!)
                if (response.body()?.id == null) {
                    Toast.makeText(context, "??????????????? ???????????? ??????.", Toast.LENGTH_SHORT).show()
                } else {
                    // Move to Main Activity
                    val userId = response.body()?.id!!
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("id", userId)
                    startActivity(intent)
                    finish()
                }
            }
        })
    }

    private fun executeLogin(id: String, pw: String) {
        val request = LoginRequest(id, pw, null)
        val call = APIService.retrofitInterface.executeLogin(request)
        loginCallback(call)
    }

    private fun executeLogin(kakaoId: Long) {
        val request = LoginRequest(null, null, kakaoId.toString())
        val call = APIService.retrofitInterface.executeLogin(request)
        loginCallback(call)
    }

    private fun executeKakaoLogin() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "????????? ?????? ?????? ??????", error)
            } else if (user != null) {
                val kakaoId = user.id!!

                Log.i(TAG, "????????? ?????? ?????? ??????" +
                        "\n????????????: ${kakaoId.toString()}" +
                        "\n?????????: ${user.kakaoAccount?.email}" +
                        "\n?????????: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n???????????????: ${user.kakaoAccount?.profile?.profileImageUrl}")

                // Check DB
                val request = LoginRequest(null, null, kakaoId.toString())
                val call = APIService.retrofitInterface.executeLogin(request)
                call.enqueue(object: Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.e(TAG, t.message!!)
                    }

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.body()?.id == null) {
                            // If no local ID, PW -> Auto Sign up
                            val id = user.kakaoAccount?.email!!
                            val pw = "password"
                            val username = user.kakaoAccount?.profile?.nickname!!

                            val request = SignupRequest(id, pw, username, kakaoId.toString())
                            val callSignup = APIService.retrofitInterface
                                .executeSignup(request)
                            callSignup.enqueue(object: Callback<ResponseCode>{
                                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                                    Log.e(TAG, t.message!!)
                                    Toast.makeText(context, "?????? ????????? ??????. ?????? ???????????????.", Toast.LENGTH_SHORT).show()
                                }

                                override fun onResponse(call: Call<ResponseCode>, response: Response<ResponseCode>) {
                                   if (response.body()!!.code == FAILURE) {
                                       Toast.makeText(context, "?????? ?????? ??????.", Toast.LENGTH_SHORT).show()
                                   } else {
                                       Toast.makeText(context, "?????? ????????? ??????. ??????????????? ???????????????. ?????? ????????????: password", Toast.LENGTH_SHORT).show()

                                       // Make Data
                                       executeLogin(id, pw)
                                   }
                                }
                            })
                        } else {
                            executeLogin(kakaoId)
                        }
                    }
                })
            }
        }
    }

    //endregion

    private fun logoutWithKaKao() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "???????????? ??????. SDK?????? ?????? ?????????", error)
            }
            else {
                Log.i(TAG, "???????????? ??????. SDK?????? ?????? ?????????")
            }
        }
    }
}