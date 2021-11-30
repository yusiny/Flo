package com.example.flo

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.sign

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView
    private lateinit var splashView: SplashView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }

    fun setSplashView(splashView: SplashView){
        this.splashView = splashView
    }

    fun signUp(user: User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        signUpView.onSignUpLoading()

        authService.signUp(user).enqueue(object : Callback<AuthResponse> {
            @SuppressLint("LongLogTag")
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUPACT/API-RESPONSE", response.toString())

                val resp = response.body()!!

                Log.d("SIGNUPACT/API-RESPONSE-FLO", resp.toString())

                when(resp.code){
                    1000 -> signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUPACT/API-ERROR", t.message.toString())

                signUpView.onSignUpFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
        Log.d("SIGNUPACT/ASYNC", "hello")
    }

    fun login(user: User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        loginView.onLoginLoading()

        authService.login(user).enqueue(object : Callback<AuthResponse> {
            @SuppressLint("LongLogTag")
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGINACT/API-RESPONSE", response.toString())

                val resp = response.body()!!

                Log.d("LOGINACT/API-RESPONSE-FLO", resp.toString())

                when(resp.code){
                    1000 -> loginView.onLoginSuccess(resp.result!!)
                    else -> loginView.onLoginFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGINACT/API-ERROR", t.message.toString())

                loginView.onLoginFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
        Log.d("LOGINACT/ASYNC", "hello")
    }

    fun autoLogIn(jwt: String){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        splashView.onSplashLoading()

        authService.autoLogin(jwt).enqueue(object : Callback<AuthResponse>{
            @SuppressLint("LongLogTag")

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SPLASHACT/API-RESPONSE", response.toString())

                val resp = response.body()!!

                Log.d("SPLASHACT/API-RESPONSE-FLO", resp.toString())

                when(resp.code){
                    1000 -> splashView.onSplashSuccess()
                    else -> splashView.onSplashFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SPLASHACT/API-ERROR", t.message.toString())

                splashView.onSplashFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
    }
}
