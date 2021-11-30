package com.example.flo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLoginBinding
import com.example.flo.databinding.ActivitySignupBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SignupAcitivity : AppCompatActivity(), SignUpView {
    lateinit var binding: ActivitySignupBinding
    private var isPWhide: Boolean = true
    private var isPWChide: Boolean = true

    private var ready: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //회원가입 버튼 클릭 시 메인으로 이동
        binding.signupSignupbtnTv.setOnClickListener {
            signUp()
        }

        initClickListener()
        setPWstatus()

        if(binding.signupIdEt.isFocused) { binding.signupPwEt.setBackgroundColor(808080)}
        if(binding.signupPwConfirmEt.isFocused) { binding.signupPwEt.setBackgroundColor(808080)}
        if(binding.signupIdEt.isFocused) { binding.signupPwEt.setBackgroundColor(808080)}
        if(binding.signupIdAdressEt.isFocused) { binding.signupPwEt.setBackgroundColor(808080)}

    }

    private fun initClickListener() {
        binding.signupPwHidebtnIv.setOnClickListener {
            isPWhide = !isPWhide
           setPWstatus()
        }
        binding.signupPwConfirmHidebtnIv.setOnClickListener {
            isPWChide = !isPWChide
            setPWstatus()
        }

        binding.signupNicknameConfirmbtnTv.setOnClickListener {
            //nickname 확인
            if(binding.signupNicknameEt.text.toString().isEmpty()){
                Toast.makeText(this, "닉네임을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }else{
                val songDB = SongDatabase.getInstance(this)!!
                val existedUser = songDB.userDao().isSameName(binding.signupNicknameEt.text.toString())
                if(existedUser != null){
                    Toast.makeText(this, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    binding.signupNicknameEt.setText("")
                }else{
                    Toast.makeText(this, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    ready = true
                }
            }
        }
    }

    private fun setPWstatus(){
        //hide 상태 -> hide 해제, text 모드
        if (isPWhide) {
            binding.signupPwEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.signupPwHidebtnIv.setImageResource(R.drawable.btn_input_password)
        } else {
            binding.signupPwEt.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.signupPwHidebtnIv.setImageResource(R.drawable.btn_input_password_off)
        }
        //hide 상태 -> hide 해제, text 모드
        if (isPWChide) {
            binding.signupPwConfirmEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.signupPwConfirmHidebtnIv.setImageResource(R.drawable.btn_input_password)
        } else {
            binding.signupPwConfirmEt.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.signupPwConfirmHidebtnIv.setImageResource(R.drawable.btn_input_password_off)
        }
    }

    private fun getUser(): User {
        val email: String =
            binding.signupIdEt.text.toString() + "@" + binding.signupIdAdressEt.text.toString()
        val password: String = binding.signupPwEt.text.toString()
        val name: String = binding.signupNicknameEt.text.toString()


        return User(email, password, name)
    }

//    private fun signUp1() {
//        //id, email empty 확인
//        if (binding.signupIdEt.text.toString().isEmpty() || binding.signupIdAdressEt.text.toString()
//                .isEmpty()
//        ) {
//            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//
//        if(!ready){
//            Toast.makeText(this, "닉네임이 적용되지 않았습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        //비밀번호 validation 처리
//        if (binding.signupPwEt.text.toString() != binding.signupPwConfirmEt.text.toString()) {
//            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        //DB에 저장
//        val userDB = SongDatabase.getInstance(this)!!
//        userDB.userDao().insert(getUser())
//
//        val users = userDB.userDao().getUsers()
//        Log.d("SIGNUP", users.toString())
//    }

    private fun signUp(){
        //nickname validation 처리
        if(binding.signupNicknameEt.text.toString().isEmpty()){
            Toast.makeText(this, "닉네임을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        //id, email validation 처리
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
//        val userDB = SongDatabase.getInstance(this)!!
//        userDB.userDao().insert(getUser())

        val authService = AuthService()
        authService.setSignUpView(this)
        authService.signUp(getUser())

    }

    override fun onSignUpLoading() {
        //로딩바
        //binding.signuploadingbar.visibility = View.VISIBLE
    }

    override fun onSignUpSuccess() {
        //로딩바 GONE
        finish()
    }

    override fun onSignUpFailure(code: Int, message: String) {
       //로딩바 GONE

        when(code){
            2016, 2017 -> {
                binding.signupIdErrorTv.visibility = View.VISIBLE
                binding.signupIdErrorTv.text = message
            }
        }
    }
}
