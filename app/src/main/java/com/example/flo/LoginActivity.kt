package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private var isPWHide: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickListener()
        setHide()
    }

    private fun initClickListener() {
        binding.loginLoginbtnTv.setOnClickListener {
            signIn()
            startMainActivity()
        }

        binding.loginPwHideIv.setOnClickListener {
            isPWHide = !isPWHide
            setHide()
        }
        binding.loginSignupbtnTv.setOnClickListener {
            startActivity(Intent(this, SignupAcitivity::class.java))
        }

        binding.loginClosebtnIv.setOnClickListener { finish() }
    }

    private fun signIn() {
        //id, email empty 확인
        if (binding.loginIdEt.text.toString().isEmpty() || binding.loginIdAdressEt.text.toString()
                .isEmpty()
        ) {
            Toast.makeText(this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        //비밀번호 validation 처리
        if (binding.loginPwEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        //DB의 UserTable에 해당하는 것이 있는지 확인
        val email: String = binding.loginIdEt.text.toString() + "@" + binding.loginIdAdressEt.text.toString()
        val password: String = binding.loginPwEt.text.toString()

        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.userDao().getUser(email, password)

        if(user == null) {
            Toast.makeText(this, "회원정보가 없습니다.", Toast.LENGTH_SHORT).show()
        }else{
            Log.d("LOGINACTIVITY", "userId ${user.id} $user")

            //발급받은 jwt를 저장해주는 함수
            saveJWT(user.id)
        }
    }

    private fun saveJWT(jwt: Int){
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt", jwt)
        editor.apply()
    }

    private fun startMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun setHide(){
        if(isPWHide){
            binding.loginPwEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.loginPwHideIv.setImageResource(R.drawable.btn_input_password)
        }else{
            binding.loginPwHideIv.setImageResource(R.drawable.btn_input_password_off)
            binding.loginPwEt.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }
}