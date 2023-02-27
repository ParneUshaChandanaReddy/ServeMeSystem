package com.example.servemesystem.adapters;

import static com.example.servemesystem.Constants.customerServiceRequest;
import static com.example.servemesystem.Constants.loggedInUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servemesystem.R;
import com.example.servemesystem.fragments.CustomerBidsFragment;
import com.example.servemesystem.model.ServiceInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerBidsAdapter extends RecyclerView.Adapter<CustomerBidsAdapter.ViewHlder> {
    //LayoutInflater inflater;
    CustomerBidsFragment lActivity;

    public CustomerBidsAdapter(CustomerBidsFragment CustomerBidsFragment) {
        //  inflater = LayoutInflater.from(CustomerBidsFragment.getActivity());
        this.lActivity = CustomerBidsFragment;
    }

    @NonNull
    @Override
    public CustomerBidsAdapter.ViewHlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cutomer_bids_adapter, parent, false);
        CustomerBidsAdapter.ViewHlder holder = new CustomerBidsAdapter.ViewHlder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomerBidsAdapter.ViewHlder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tvVendorName.setText(lActivity.servicList.get(position).getVendorName());
        holder.tvServiceDesc.setText(lActivity.servicList.get(position).getDesc());
        holder.tvDuration.setText(lActivity.servicList.get(position).getDuration() + " Mins");
        holder.tvServiceCost.setText(lActivity.servicList.get(position).getCost() + " $");
        holder.tvBidCost.setText(lActivity.servicList.get(position).getBidAmount() + " $");

        if (lActivity.servicList.get(position).getBidAccepted().equalsIgnoreCase("Reject")) {
            holder.tvServiceStatus.setText("Rejected");
            holder.btnBidDelete.setVisibility(View.VISIBLE);
        } else {
            holder.tvServiceStatus.setText("No Action");
            holder.btnBidDelete.setVisibility(View.GONE);
        }

      /*  if (lActivity.servicList.get(position).getPaymentStatus().equalsIgnoreCase("Paid")) {
            holder.btnPayment.setVisibility(View.GONE);
            holder.tvPaimentStatus.setVisibility(View.VISIBLE);
        } else {
            holder.btnPayment.setVisibility(View.VISIBLE);
            holder.tvPaimentStatus.setVisibility(View.GONE);
        }*/

        holder.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.btnBidDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  showBidDialog(lActivity.servicList.get(position));
            }
        });

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(lActivity.getActivity(), "" + lActivity.servicList.get(position).getVendorPhone(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + lActivity.servicList.get(position).getVendorPhone()));
                lActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lActivity.servicList.size();
    }

    class ViewHlder extends RecyclerView.ViewHolder {
        LinearLayout custmr_lay;
        TextView tvServiceDesc;
        TextView tvVendorName;
        TextView tvDuration;
        TextView tvServiceCost, tvServiceStatus, tvPaimentStatus, tvBidCost;
        Button btnSelect, btnBidDelete, btnCall;

        public ViewHlder(View itemView) {
            super(itemView);
            custmr_lay = (LinearLayout) itemView.findViewById(R.id.custmr_lay);
            tvServiceDesc = (TextView) itemView.findViewById(R.id.tvServiceDesc);
            tvVendorName = (TextView) itemView.findViewById(R.id.tvVendorName);
            tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);
            tvServiceCost = (TextView) itemView.findViewById(R.id.tvServiceCost);
            btnSelect = itemView.findViewById(R.id.btnSelect);
            btnBidDelete = itemView.findViewById(R.id.btnBidDelete);
            tvServiceStatus = itemView.findViewById(R.id.tvServiceStatus);
            tvPaimentStatus = itemView.findViewById(R.id.tvPaimentStatus);
            tvBidCost = itemView.findViewById(R.id.tvBidCost);
            btnCall = itemView.findViewById(R.id.btnCall);
        }
    }

/*
    public void showPaymentDialog(ServiceInfo serviceInfo, int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(lActivity.getActivity());
        LayoutInflater inflater = lActivity.getActivity().getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.payment_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText etCardNo = (EditText) dialogView.findViewById(R.id.etCardNo);
        final EditText etMonth = (EditText) dialogView.findViewById(R.id.etMonth);
        final EditText etYear = (EditText) dialogView.findViewById(R.id.etYear);
        EditText etCvv = dialogView.findViewById(R.id.etCvv);
        Spinner spinPaymentType = dialogView.findViewById(R.id.spinPaymentType);

        final Button btnSave = (Button) dialogView.findViewById(R.id.btnSave);
        final Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);

        //dialogBuilder.setTitle("Add Service:");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spinPaymentType.getSelectedItemPosition() == 0) {
                    Toast.makeText(lActivity.getActivity(), "Select Service", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etCardNo.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(lActivity.getActivity(), "Enter Card No", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etYear.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(lActivity.getActivity(), "Enter Year", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etMonth.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(lActivity.getActivity(), "Enter Month", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etCvv.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(lActivity.getActivity(), "Enter CVV", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference dR = FirebaseDatabase.getInstance().getReference(customerServiceRequest).child(loggedInUser.getPhoneNo()).child(serviceInfo.getServiceName()).child("paymentStatus");
                dR.setValue("Paid");
                lActivity.servicList.get(position).setPaymentStatus("Paid");
                notifyDataSetChanged();

                Toast.makeText(lActivity.getActivity(), "Payment Successfully", Toast.LENGTH_SHORT).show();
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
*/

}
