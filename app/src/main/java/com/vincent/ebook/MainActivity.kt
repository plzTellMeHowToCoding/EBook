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

    private val activity = this
    private val handler = object:Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            val intent = Intent(activity,HomeActivity::class.java)
            startActivity(intent)
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
        fun startMainActivity(context : Context){
            val intent = Intent(context,MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}