package com.example.servemesystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class VendorHomeActivity extends AppCompatActivity {
    TextView tvVendorWelcome;
    Button btnMyServices, btnBidsReceived, btnServiceRequests, btnLogout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_home_activity);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE},
                    123);

        } else {

            // else block means user has already accepted.And make your phone call here.

        }
        tvVendorWelcome = findViewById(R.id.tvVendorWelcome);
        tvVendorWelcome.setText("Welcome back " + Constants.loggedInUser.getName());
        btnMyServices = findViewById(R.id.btnMyServices);
        btnBidsReceived = findViewById(R.id.btnBidsReceived);
        btnServiceRequests = findViewById(R.id.btnServiceRequests);
        btnLogout = findViewById(R.id.btnLogout);

        btnMyServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendorHomeActivity.this, VendorDashboardActivity.class);
                intent.putExtra("type", "myservice");
                startActivity(intent);
            }
        });
        btnBidsReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendorHomeActivity.this, VendorDashboardActivity.class);
                intent.putExtra("type", "bids");
                startActivity(intent);

            }
        });
        btnServiceRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendorHomeActivity.this, VendorDashboardActivity.class);
                intent.putExtra("type", "servicerequest");
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(VendorHomeActivity.this);
                aBuilder.setTitle("SMS");
                aBuilder.setMessage("Are you sure, want to Logout..?");
                aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        startActivity(new Intent(VendorHomeActivity.this, VendorOrCustomerActivity.class));
                    }
                });
                aBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                aBuilder.show();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
