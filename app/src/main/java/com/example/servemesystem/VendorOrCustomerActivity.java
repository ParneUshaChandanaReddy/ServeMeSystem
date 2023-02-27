package com.example.servemesystem;

import static com.example.servemesystem.Constants.isCustomer;
import static com.example.servemesystem.Constants.isGuest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class VendorOrCustomerActivity extends AppCompatActivity {
    Button btnUser, btnVendor, btnGuest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_customer_activity);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE},
                    123);
        } else {
            // else block means user has already accepted.And make your phone call here.
        }
        btnUser = findViewById(R.id.btnUser);
        btnVendor = findViewById(R.id.btnVendor);
        btnGuest = findViewById(R.id.btnGuest);

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCustomer = true;
                startActivity(new Intent(VendorOrCustomerActivity.this, LoginActiivty.class));
            }
        });
        btnVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCustomer = false;
                startActivity(new Intent(VendorOrCustomerActivity.this, LoginActiivty.class));
            }
        });

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isGuest = true;
                startActivity(new Intent(VendorOrCustomerActivity.this, CustomerHomeActivity.class));
            }
        });
    }
}
