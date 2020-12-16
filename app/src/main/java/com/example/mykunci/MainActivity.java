package com.example.mykunci;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView btnAdmin = findViewById(R.id.btnAdminLogin);
        TextView tvTittle = findViewById(R.id.toolbar_tittle);

        tvTittle.setText("Android Key");

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminLogin = new Intent(MainActivity.this, AdminLoginActivity.class);
                startActivity(adminLogin);
            }
        });




    }

}