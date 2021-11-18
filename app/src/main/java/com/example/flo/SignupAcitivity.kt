package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLoginBinding
import com.example.flo.databinding.ActivitySignupBinding

class SignupAcitivity: AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //회원가입 버튼 클릭 시 메인으로 이동
        binding.signupSignupbtnTv.setOnClickListener{
            signUp()
            finish()
    }

}

    private fun getUser(): User{
        val email: String = binding.signupIdEt.text.toString() + "@" + binding.signupIdAdressEt.text.toString()
        val password: String = binding.signupPwEt.text.toString()

        return User(email, password)
    }

    private fun signUp() {
        //id, email empty 확인
        if (binding.signupIdEt.text.toString().isEmpty() || binding.signupIdAdressEt.text.toString()
                .isEmpty()
        ) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        //비밀번호 validation 처리
        if (binding.signupPwEt.text.toString() != binding.signupPwConfirmEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        //DB에 저장
        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        val users = userDB.userDao().getUsers()
        Log.d("SIGNUP", users.toString())

    }
}
