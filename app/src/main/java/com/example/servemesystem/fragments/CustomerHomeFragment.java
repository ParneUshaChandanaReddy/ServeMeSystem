package com.example.servemesystem.fragments;

import static com.example.servemesystem.Constants.isGuest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.servemesystem.R;
import com.example.servemesystem.VendorSelectionActivity;

public class CustomerHomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);
        CardView cardPlumber = view.findViewById(R.id.cardPlumber);
        CardView cardElectrical = view.findViewById(R.id.cardElectrical);
        CardView cardHomeClean = view.findViewById(R.id.cardHomeClean);
        CardView cardHomePaint = view.findViewById(R.id.cardHomePaint);
        CardView cardPestControl = view.findViewById(R.id.cardPestControl);
        /*<item>Home Cleaning</item>
        <item>Plumbing</item>
        <item>Home Repair and Painting</item>
        <item>Pest Control</item>*/
        cardPlumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   if (isGuest) {
                    Toast.makeText(getActivity(), "Please login to continue", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                Intent intent = new Intent(getActivity(), VendorSelectionActivity.class);
                intent.putExtra("service", "Plumbing");
                startActivity(intent);
            }
        });

        cardElectrical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  if (isGuest) {
                    Toast.makeText(getActivity(), "Please login to continue", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                Intent intent = new Intent(getActivity(), VendorSelectionActivity.class);
                intent.putExtra("service", "Electrical");
                startActivity(intent);
            }
        });

        cardHomeClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (isGuest) {
                    Toast.makeText(getActivity(), "Please login to continue", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                Intent intent = new Intent(getActivity(), VendorSelectionActivity.class);
                intent.putExtra("service", "Home Cleaning");
                startActivity(intent);
            }
        });

        cardHomePaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (isGuest) {
                    Toast.makeText(getActivity(), "Please login to continue", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                Intent intent = new Intent(getActivity(), VendorSelectionActivity.class);
                intent.putExtra("service", "Home Repair and Painting");
                startActivity(intent);
            }
        });

        cardPestControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (isGuest) {
                    Toast.makeText(getActivity(), "Please login to continue", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                Intent intent = new Intent(getActivity(), VendorSelectionActivity.class);
                intent.putExtra("service", "Pest Control");
                startActivity(intent);
            }
        });

        return view;
    }
}
