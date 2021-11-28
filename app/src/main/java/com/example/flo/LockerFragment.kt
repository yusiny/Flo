package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    val information = arrayListOf("저장한 곡", "음악파일", "저장한 앨범")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        setLockerVP()
        initView()

        return binding.root
    }

    private fun setLockerVP() {
        val lockerAdapter = LockerViewpagerAdapter(this)
        binding.lockerVp.adapter = lockerAdapter

        //tabLayout 연결
        TabLayoutMediator(binding.lockerTb, binding.lockerVp) { tab, position ->
            tab.text = information[position]
        }.attach()
    }

    private fun initView(){
        val userId = getUserIdx(requireContext())!!
        Log.d("LOCKERFRG/JWT", "userId는 ${userId}")
        if(userId == 0){
            binding.lockerLoginTv.text = "로그인"
            binding.lockerUsernicknameTv.visibility = View.GONE
            binding.lockerUserimgIv.visibility = View.GONE

            binding.lockerLoginTv.setOnClickListener{
                startActivity(Intent(activity, LoginActivity::class.java))
            }

        }else{
            val songDB = SongDatabase.getInstance(requireContext())!!
            val user = songDB.userDao().getUserById(userId)
            Log.d("LOCKERFRG/USER", user.toString())
            binding.lockerLoginTv.text = "로그아웃"
            if(user != null) {
                binding.lockerUsernicknameTv.visibility = View.VISIBLE
                binding.lockerUserimgIv.visibility = View.VISIBLE
                binding.lockerUsernicknameTv.text = user!!.name
            }

            binding.lockerLoginTv.setOnClickListener {
                //로그아웃
                logOut()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    private fun logOut(){
        removeUserIdx(requireContext())!!
    }

}