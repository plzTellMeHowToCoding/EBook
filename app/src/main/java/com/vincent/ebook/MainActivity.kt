package com.vincent.ebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import Homepage.HomeActivity
import Utils.FireBaseUtils
import android.content.Context

class MainActivity : AppCompatActivity() {

    private val handler = object:Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            HomeActivity.startHomeActivity(this@MainActivity,intent.getBooleanExtra("isLogin",false))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FireBaseUtils.setBookInfo()
        handler.sendEmptyMessageDelayed(0,4000)
    }

    companion object{
        fun startMainActivity(context : Context, isLogin : Boolean){
            val intent = Intent(context,MainActivity::class.java)
            intent.putExtra("isLogin",isLogin)
            context.startActivity(intent)
        }
    }
}