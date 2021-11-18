package com.example.flo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLoginBinding
import com.example.flo.databinding.ActivitySignupBinding

class SignupAcitivity: AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupSignupbtnTv.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}