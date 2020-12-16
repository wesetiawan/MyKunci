package com.example.mykunci;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.MenuPopupWindow;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

public class AdminPageActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    LinearLayout layout_laporan, layout_generateqr;
    Button btnDownloadLaporan;
    ImageView btnMenu;
    TextView tvTittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        btnMenu = findViewById(R.id.btn_adminMenu);
        tvTittle = findViewById(R.id.toolbar_tittle);
        btnDownloadLaporan = findViewById(R.id.btn_downloadLaporan);
        layout_laporan = findViewById(R.id.layout_laporan);
        layout_generateqr = findViewById(R.id.layout_generateqr);

        layout_generateqr.setVisibility(View.INVISIBLE);
        tvTittle.setText("Admin Laporan");
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
            }
        });
    }

    public void showPopUp(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.admin_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){

            case R.id.laporan:
                tvTittle.setText("Admin Laporan");
                layout_laporan.setVisibility(View.VISIBLE);
                layout_generateqr.setVisibility(View.INVISIBLE);
                return true;
            case R.id.generateqr:
                tvTittle.setText("Admin Generate QR");
                layout_laporan.setVisibility(View.INVISIBLE);
                layout_generateqr.setVisibility(View.VISIBLE);
                return true;
            default:
                return false;
        }
    }
}