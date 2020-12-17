package com.example.mykunci

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnAdminLogin: ImageView
    private lateinit var pageTittle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAdminLogin = findViewById(R.id.btn_adminPage)
        pageTittle = findViewById(R.id.tv_tittle)

        pageTittle.text = "Android Key"

        btnAdminLogin.setOnClickListener{
            val adminLoginActivity = Intent(this,AdminLoginActivity::class.java)
            startActivity(adminLoginActivity)
        }


    }
}