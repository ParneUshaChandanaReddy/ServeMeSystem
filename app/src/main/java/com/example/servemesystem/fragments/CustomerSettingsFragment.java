package com.example.servemesystem.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.servemesystem.Constants;
import com.example.servemesystem.CustomerHomeActivity;
import com.example.servemesystem.R;
import com.example.servemesystem.VendorOrCustomerActivity;

public class CustomerSettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        TextView usernameTextView = view.findViewById(R.id.usernameTextView);
        TextView txtLogout = view.findViewById(R.id.txtLogout);
        TextView tvMyRewards = view.findViewById(R.id.tvMyRewards);
        usernameTextView.setText(Constants.loggedInUser.getName());

        tvMyRewards.setText("My Rewards: " + Constants.loggedInUser.getRewardPoints());

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(getActivity());
                aBuilder.setTitle("SMS");
                aBuilder.setMessage("Are you sure, want to Logout..?");
                aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finishAffinity();
                        startActivity(new Intent(getActivity(), VendorOrCustomerActivity.class));
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
        return view;
    }
}
