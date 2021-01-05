package com.example.mykunci

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var btnAdminPageActivity : ImageView
    private lateinit var pageTittle: TextView
    private lateinit var btnLogin: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)


        btnAdminPageActivity = findViewById(R.id.btnActionTopBar)
        pageTittle = findViewById(R.id.tv_tittle)
        btnLogin = findViewById(R.id.btn_adminLogin)

        pageTittle.text = "Admin Login"

        btnAdminPageActivity.visibility = View.INVISIBLE

        btnLogin.setOnClickListener {
            val adminPageActivity = Intent(this,AdminPageActivity::class.java)
            startActivity(adminPageActivity)
        }


    }
}