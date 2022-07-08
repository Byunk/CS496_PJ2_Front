package com.example.cs496_pj2_front

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cs496_pj2_front.databinding.ActivityLoginBinding
import com.example.cs496_pj2_front.model.Login
import com.example.cs496_pj2_front.model.LoginRequest
import com.example.cs496_pj2_front.model.User
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
            val id = binding.etId.toString()
            val pw = binding.etPassword.toString()
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
                        Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
                    }
                    else if (tokenInfo != null) {
                        Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                        val kakaoId = tokenInfo.id!!
                        executeLogin(kakaoId)
                    }
                }
            }

            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                    executeKakaoLogin()
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
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
    private fun loginCallback(call: Call<Login>) {
        call.enqueue(object: Callback<Login> {
            override fun onFailure(call: Call<Login>, t: Throwable) {
                Log.e(TAG, t.message!!)
                Toast.makeText(context, "로그인 오류", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                if (response.body()?.userId == null) {
                    Toast.makeText(context, "계정정보가 존재하지 않음.", Toast.LENGTH_SHORT).show()
                } else {
                    // Move to Main Activity
                    val userId = response.body()?.userId!!
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("id", userId)
                    startActivity(intent)
                    finish()
                }
            }
        })
    }

    private fun executeLogin(id: String, pw: String) {
        val call = APIService.retrofitInterface.executeLogin(id, pw)
        loginCallback(call)
    }

    private fun executeLogin(kakaoId: Long) {
        val call = APIService.retrofitInterface.executeLogin(kakaoId)
        loginCallback(call)
    }

    private fun executeKakaoLogin() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 요청 정보 실패", error)
            } else if (user != null) {
                val kakaoId = user.id!!

                Log.i(TAG, "사용자 요청 정보 성공" +
                        "\n회원번호: ${kakaoId.toString()}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.profileImageUrl}")

                // Check DB
                val call = APIService.retrofitInterface.executeLogin(kakaoId)
                call.enqueue(object: Callback<Login> {
                    override fun onFailure(call: Call<Login>, t: Throwable) {
                        Log.e(TAG, t.message!!)
                    }

                    override fun onResponse(call: Call<Login>, response: Response<Login>) {
                        if (response.body()?.userId == null) {
                            // If no local ID, PW -> Auto Sign up
                            val id = user.kakaoAccount?.email!!
                            val pw = "password"
                            val username = user.kakaoAccount?.profile?.nickname!!

                            val request = LoginRequest(id, pw, username, kakaoId.toString())
                            val callSignup = APIService.retrofitInterface
                                .executeSignup(request)

                            callSignup.enqueue(object: Callback<Int>{
                                override fun onFailure(call: Call<Int>, t: Throwable) {
                                    Log.e(TAG, t.message!!)
                                    Toast.makeText(context, "자동 로그인 오류. 다시 시도하세요.", Toast.LENGTH_SHORT).show()
                                }

                                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                                   if (response.body() == FAILURE) {
                                       Toast.makeText(context, "회원 가입 실패.", Toast.LENGTH_SHORT).show()
                                   } else {
                                       Toast.makeText(context, "자동 로그인 성공. 비밀번호를 변경하세요. 기본 비밀번호: password", Toast.LENGTH_SHORT).show()

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
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            }
            else {
                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }
}