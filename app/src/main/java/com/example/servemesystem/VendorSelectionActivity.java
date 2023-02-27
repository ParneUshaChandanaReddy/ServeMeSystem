package com.example.servemesystem;

import static com.example.servemesystem.Constants.loggedInUser;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servemesystem.adapters.SelectVendorAdapter;
import com.example.servemesystem.adapters.ServicesAdapter;
import com.example.servemesystem.model.ServiceInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VendorSelectionActivity extends AppCompatActivity implements OnMapReadyCallback {
    RecyclerView rViewVendorSelection;
    //List<ServiceInfo> serviceInfoList;
    String selectedService = "";
    public List<ServiceInfo> servicList;
    public List<ServiceInfo> filteredList;
    public static EditText searchBox, search_by_edtxt;
    //List<ServiceInfo> tempList;
    TextView tvChooseVendor;
    ListPopupWindow search_by_popup;
    Button btnListView, btnMapView;
    FrameLayout linMapView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_selection_activity);
        filteredList = new ArrayList<>();
        rViewVendorSelection = findViewById(R.id.rViewVendorSeection);
        tvChooseVendor = findViewById(R.id.tvChooseVendor);
        searchBox = findViewById(R.id.search_box);
        search_by_edtxt = findViewById(R.id.search_by_edtxt);
        btnListView = findViewById(R.id.btbListView);
        btnMapView = findViewById(R.id.btnMapView);
        linMapView = findViewById(R.id.linMapView);

        servicList = new ArrayList<>();
        //tempList = new ArrayList<>();
        selectedService = getIntent().getStringExtra("service");
        tvChooseVendor.setText("Choose a vendor for your " + selectedService + " work");
        Toast.makeText(this, "" + selectedService, Toast.LENGTH_SHORT).show();

        SearchBy(VendorSelectionActivity.this, search_by_edtxt);
        search_by_edtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchBy(VendorSelectionActivity.this, search_by_edtxt);
            }
        });


        DatabaseReference dRNew = FirebaseDatabase.getInstance().getReference(Constants.customerService).child(selectedService);

        //  Query query = services.orderByChild(selectedService);
        dRNew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    servicList.clear();
                    filteredList.clear();
                    for (DataSnapshot dataSNp : snapshot.getChildren()) {
                        servicList.add(dataSNp.getValue(ServiceInfo.class));
                    }
                    filteredList.clear();
                    filteredList.addAll(servicList);

//                  Constants.createDueData(filteredList,ImportedActivity.this);
                    setAdapter();
                    try {
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(VendorSelectionActivity.this);
                    } catch (Exception e) {

                    }

                  /*  /////////
                    servicList.clear();
                    for (DataSnapshot dataSNp : snapshot.getChildren()) {
                        servicList.add(dataSNp.getValue(ServiceInfo.class));
                    }
                    setAdapter();*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAdapter() {
        btnMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linMapView.setVisibility(View.VISIBLE);
                rViewVendorSelection.setVisibility(View.GONE);
            }
        });
        btnListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linMapView.setVisibility(View.GONE);
                rViewVendorSelection.setVisibility(View.VISIBLE);
            }
        });

        // Toast.makeText(VendorSelectionActivity.this, "Total = " + servicList.size(), Toast.LENGTH_SHORT).show();
        rViewVendorSelection.setLayoutManager(new LinearLayoutManager(VendorSelectionActivity.this));
        SelectVendorAdapter mAdapter = new SelectVendorAdapter(VendorSelectionActivity.this, filteredList);
        rViewVendorSelection.setAdapter(mAdapter);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void SearchBy(Activity activity, final EditText edittext) {

        search_by_popup = new ListPopupWindow(activity);
        search_by_popup.setAnchorView(edittext);
        search_by_popup.setAdapter(new ArrayAdapter<String>(activity, R.layout.text_vw, R.id.search_by, Constants.search_by));
        search_by_popup.setModal(true);

        edittext.setText(Constants.search_by[0]);

        search_by_popup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edittext.setText(Constants.search_by[position]);
                search_by_popup.dismiss();
            }

        });

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_by_popup.show();
            }
        });
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_by_popup.show();
            }
        });

    }

    GoogleMap mMap;

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        //   mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //¬    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        for (int i = 0; i < servicList.size(); i++) {
            LatLng latLng = new LatLng(servicList.get(i).getLat(), servicList.get(i).getLang());
            mMap.addMarker(new MarkerOptions().position(latLng).title(servicList.get(i).getVendorName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }


       /* mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                //  LatLng sydney = new LatLng(-34, 151);
                //  selectedlatLng = latLng;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marked Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });*/
    }
}
