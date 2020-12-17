package com.example.mykunci

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AdminPageActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    private lateinit var btnAdminMenu : ImageView
    private lateinit var layoutLaporan : LinearLayout
    private lateinit var layoutGenerateQr : LinearLayout
    private lateinit var tittle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_page)

        btnAdminMenu = findViewById(R.id.btn_adminMenu)
        layoutLaporan = findViewById(R.id.layout_laporan)
        layoutGenerateQr = findViewById(R.id.layout_generateqr)
        tittle = findViewById(R.id.toolbar_tittle)

        layoutLaporan.visibility = View.VISIBLE
        layoutGenerateQr.visibility = View.INVISIBLE
        tittle.text = "Admin Laporan"

        btnAdminMenu.setOnClickListener {
            showPopUp(btnAdminMenu)
        }


    }

    private fun showPopUp(v: View?) {
        val popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.admin_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.laporan -> {
                layoutLaporan.visibility = View.VISIBLE
                layoutGenerateQr.visibility = View.INVISIBLE
                tittle.text = "Admin Laporan"
                true
            }
            R.id.generateqr -> {
                layoutLaporan.visibility = View.INVISIBLE
                layoutGenerateQr.visibility = View.VISIBLE
                tittle.text = "Admin Generate QR"
                true
            }
            else -> false
        }
    }
}