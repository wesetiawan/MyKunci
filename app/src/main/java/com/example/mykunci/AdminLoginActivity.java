package com.example.mykunci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AdminLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


        ImageView btnAdmin = findViewById(R.id.btnAdminLogin);
        TextView tvTittle = findViewById(R.id.toolbar_tittle);
        Button btnLogin = findViewById(R.id.btn_adminLogin);

        btnAdmin.setVisibility(View.INVISIBLE);
        tvTittle.setText("Android Key");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminPage = new Intent(AdminLoginActivity.this, AdminPageActivity.class);
                startActivity(adminPage);
            }
        });


    }
}