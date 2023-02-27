package com.example.servemesystem.fragments;

import static com.example.servemesystem.Constants.customerBid;
import static com.example.servemesystem.Constants.loggedInUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servemesystem.R;
import com.example.servemesystem.adapters.CustomerBidsAdapter;
import com.example.servemesystem.adapters.CustomerServicesAdapter;
import com.example.servemesystem.model.ServiceInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerBidsFragment extends Fragment {
    public List<ServiceInfo> servicList;
    RecyclerView rViewCustomerServices;
    CustomerBidsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.customer_bids_fragment, container, false);
        rViewCustomerServices = view.findViewById(R.id.rViewCustomerServices);
        servicList = new ArrayList<>();

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(customerBid).child(loggedInUser.getPhoneNo());
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

        return view;
    }

    private void setAdapterr() {
        mAdapter = new CustomerBidsAdapter(this);
        rViewCustomerServices.setLayoutManager(new LinearLayoutManager(getActivity()));
        rViewCustomerServices.setAdapter(mAdapter);
    }
}
