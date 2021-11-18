package com.example.flo

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.flo.databinding.ToastLikedunlikedBinding

object ToastLiked {
   fun customedLikeToast(context: Context, message: String): Toast? {
        val inflater = LayoutInflater.from(context)
        val binding: ToastLikedunlikedBinding =
            DataBindingUtil.inflate(inflater, R.layout.toast_likedunliked, null, false)

        binding.tvToast.text = message

        return Toast(context).apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 100.toPx())
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}

