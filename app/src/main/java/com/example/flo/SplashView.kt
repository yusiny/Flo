package com.example.flo

interface SplashView {
    fun onSplashLoading()
    fun onSplashSuccess()
    fun onSplashFailure(code: Int, message: String)
}