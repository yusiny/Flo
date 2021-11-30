package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySplashBinding

class SplashActivity:AppCompatActivity(), SplashView {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //jwt 받아오기
        val jwt = getJwt(this)
        Log.d("SPLACHACT/JWT", "Splash Act의 jwt 는 ${jwt}")

        //auto
        val authService = AuthService()
        authService.setSplashView(this)
        authService.autoLogIn(jwt)

    }

    override fun onSplashLoading() {
        //로딩
    }

    override fun onSplashSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onSplashFailure(code: Int, message: String) {
        when(code){
            2001, 2002 -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}