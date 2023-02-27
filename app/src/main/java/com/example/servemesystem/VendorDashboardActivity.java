package com.example.servemesystem;

import static com.example.servemesystem.Constants.loggedInUser;
import static com.example.servemesystem.Constants.serviceKey;
import static com.example.servemesystem.Constants.vendorBid;
import static com.example.servemesystem.Constants.vendorServiceRequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servemesystem.adapters.ServicesAdapter;
import com.example.servemesystem.model.ServiceInfo;
import com.example.servemesystem.model.VendorInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VendorDashboardActivity extends AppCompatActivity {
    TextView txtWelcome;
    RecyclerView rViewVendorServices;
    Button btnAddService;
    public List<ServiceInfo> servicList;
    public String type;
    TextView tvType;
    String keyToUse = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        servicList = new ArrayList<>();

        txtWelcome = findViewById(R.id.txtWelcome);
        tvType = findViewById(R.id.tvType);
        rViewVendorServices = findViewById(R.id.rViewVendorServices);
        btnAddService = findViewById(R.id.btnAddService);
        type = getIntent().getStringExtra("type");


        //myservice,bids,servicerequest

        if (type.equalsIgnoreCase("myservice")) {
            tvType.setText("My Services");
            btnAddService.setVisibility(View.VISIBLE);
            keyToUse = serviceKey;
        } else if (type.equalsIgnoreCase("bids")) {
            tvType.setText("Bids Received");
            keyToUse = vendorBid;
        } else if (type.equalsIgnoreCase("servicerequest")) {
            tvType.setText("Service Requests");
            keyToUse = vendorServiceRequest;
        }

        VendorInfo info = loggedInUser;
        txtWelcome.setText("Welcome back " + info.getName());

        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateDeleteDialog();
            }
        });

        loadExistingServices();
    }

    public void loadExistingServices() {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(keyToUse).child(loggedInUser.getPhoneNo());
        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    servicList.clear();
                    for (DataSnapshot dataSNp : snapshot.getChildren()) {
                        servicList.add(dataSNp.getValue(ServiceInfo.class));
                    }
                    if (servicList.size() > 0) {
                        setAdapterr();
                    }
                }
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showUpdateDeleteDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText etServiceDescription = (EditText) dialogView.findViewById(R.id.etServiceDescription);
        final EditText etDuration = (EditText) dialogView.findViewById(R.id.etDuration);
        final EditText etCost = (EditText) dialogView.findViewById(R.id.etCost);
        Spinner spinServiceType = dialogView.findViewById(R.id.spinServiceType);

        final Button btnSave = (Button) dialogView.findViewById(R.id.btnSave);
        final Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);

        dialogBuilder.setTitle("Add Service:");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spinServiceType.getSelectedItemPosition() == 0) {
                    Toast.makeText(VendorDashboardActivity.this, "Select Service", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etServiceDescription.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(VendorDashboardActivity.this, "Enter Description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etCost.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(VendorDashboardActivity.this, "Enter Cost", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etDuration.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(VendorDashboardActivity.this, "Enter Duration", Toast.LENGTH_SHORT).show();
                    return;
                }
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setServiceName(spinServiceType.getSelectedItem().toString());
                serviceInfo.setDesc(etServiceDescription.getText().toString());
                serviceInfo.setCost(Integer.parseInt(etCost.getText().toString()));
                serviceInfo.setDuration(Integer.parseInt(etDuration.getText().toString()));
                serviceInfo.setVendorName(loggedInUser.getName());
                serviceInfo.setVendorAddress(loggedInUser.getAddress());
                serviceInfo.setVendorPhone(loggedInUser.getPhoneNo());
                serviceInfo.setLat(loggedInUser.getLat());
                serviceInfo.setLang(loggedInUser.getLang());

                DatabaseReference dR = FirebaseDatabase.getInstance().getReference(serviceKey).child(loggedInUser.getPhoneNo()).child(spinServiceType.getSelectedItem().toString());
                dR.setValue(serviceInfo);
                DatabaseReference dRNew = FirebaseDatabase.getInstance().getReference(Constants.customerService).child(spinServiceType.getSelectedItem().toString()).child(loggedInUser.getPhoneNo());
                dRNew.setValue(serviceInfo);

                Toast.makeText(VendorDashboardActivity.this, "Service Added Successfully", Toast.LENGTH_SHORT).show();
                b.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

    }

    ServicesAdapter mAdapter;

    public void setAdapterr() {
        rViewVendorServices.setLayoutManager(new LinearLayoutManager(VendorDashboardActivity.this));
        mAdapter = new ServicesAdapter(VendorDashboardActivity.this);
        rViewVendorServices.setAdapter(mAdapter);

    }
}
