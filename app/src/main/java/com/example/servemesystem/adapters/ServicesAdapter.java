package com.example.servemesystem.adapters;

import static com.example.servemesystem.Constants.customerBid;
import static com.example.servemesystem.Constants.customerServiceRequest;
import static com.example.servemesystem.Constants.loggedInUser;
import static com.example.servemesystem.Constants.vendorBid;
import static com.example.servemesystem.Constants.vendorServiceRequest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servemesystem.Constants;
import com.example.servemesystem.VendorDashboardActivity;
import com.example.servemesystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHlder> {
    VendorDashboardActivity lActivity;

    public ServicesAdapter(VendorDashboardActivity VendorDashboardActivity) {
        this.lActivity = VendorDashboardActivity;
    }

    @NonNull
    @Override
    public ServicesAdapter.ViewHlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.listitm, parent, false);
        ServicesAdapter.ViewHlder holder = new ServicesAdapter.ViewHlder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ServicesAdapter.ViewHlder holder, @SuppressLint("RecyclerView") final int position) {
        if (lActivity.type.equalsIgnoreCase("myservice")) {
            holder.imgDelete.setVisibility(View.VISIBLE);
        } else if (lActivity.type.equalsIgnoreCase("bids")) {
            holder.imgNavigation.setVisibility(View.VISIBLE);
            if (lActivity.servicList.get(position).getBidAccepted().equalsIgnoreCase("Reject")) {
                holder.tvRejected.setVisibility(View.VISIBLE);
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.btnDeleteBid.setVisibility(View.VISIBLE);
            }
            holder.tvBidAmount.setText(lActivity.servicList.get(position).getBidAmount() + " $");
            holder.linBid.setVisibility(View.VISIBLE);
            holder.linCustomerName.setVisibility(View.VISIBLE);
            holder.linCustomerMobile.setVisibility(View.VISIBLE);
            holder.tvCustomerName.setText(lActivity.servicList.get(position).getBidCustomerName());
            holder.tvCustomerMobile.setText(lActivity.servicList.get(position).getBidCustomerNo());
            holder.linServiceDate.setVisibility(View.VISIBLE);
            holder.tvServiceDate.setText(getDate(lActivity.servicList.get(position).getServiceDate()));

        } else if (lActivity.type.equalsIgnoreCase("servicerequest")) {
            holder.linService.setVisibility(View.VISIBLE);
            holder.libBidAmount.setVisibility(View.GONE);
            holder.imgNavigation.setVisibility(View.VISIBLE);
            if (lActivity.servicList.get(position).getPaymentStatus().equalsIgnoreCase("Paid")) {
                holder.btnComplete.setVisibility(View.VISIBLE);
                holder.tvPaimentStatus.setVisibility(View.VISIBLE);
                holder.tvPaimentStatus.setText(lActivity.servicList.get(position).getPaymentStatus());
            } else {
                holder.btnComplete.setVisibility(View.GONE);
                holder.tvPaimentStatus.setVisibility(View.VISIBLE);
                holder.tvPaimentStatus.setText(lActivity.servicList.get(position).getPaymentStatus());
            }
            String serviceStatus = lActivity.servicList.get(position).getServiceStatus();
            if (serviceStatus.equalsIgnoreCase("Done") || serviceStatus.equalsIgnoreCase("Completed")) {
                holder.tvCompleted.setVisibility(View.VISIBLE);
                holder.tvCompleted.setText(lActivity.servicList.get(position).getServiceStatus());
                holder.btnComplete.setVisibility(View.GONE);
            }
            holder.linServiceDate.setVisibility(View.VISIBLE);
            holder.tvServiceDate.setText(getDate(lActivity.servicList.get(position).getServiceDate()));
        }

        holder.tvServiceType.setText(lActivity.servicList.get(position).getServiceName());
        holder.tvServiceDesc.setText(lActivity.servicList.get(position).getDesc());
        holder.tvDuration.setText(lActivity.servicList.get(position).getDuration() + " Mins");
        holder.tvServiceCost.setText(lActivity.servicList.get(position).getCost() + " $");

        holder.imgNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String lat = "" + lActivity.servicList.get(position).getCusLat();
                String lang = "" + lActivity.servicList.get(position).getCusLang();

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + lat + "," + lang));
                lActivity.startActivity(intent);

            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(lActivity);
                aBuilder.setTitle("SMS");
                aBuilder.setMessage("Are you sure, want to delete the service ?");
                aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(Constants.serviceKey).child(loggedInUser.getPhoneNo()).child(lActivity.servicList.get(position).getServiceName());
                        dR.removeValue();
                        lActivity.servicList.remove(position);
                        lActivity.loadExistingServices();
                        notifyDataSetChanged();
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

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(lActivity);
                aBuilder.setTitle("SMS");
                aBuilder.setMessage("Are you sure, want to Accept the Bid ?");
                aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        lActivity.servicList.get(position).setServiceStatus("Accept");
                        lActivity.servicList.get(position).setPaymentStatus("Not Paid");

                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(customerServiceRequest).child(lActivity.servicList.get(position).getBidCustomerNo()).child(lActivity.servicList.get(position).getServiceName());
                        dR.setValue(lActivity.servicList.get(position));
                        DatabaseReference dRNew = FirebaseDatabase.getInstance().getReference(vendorServiceRequest).child(loggedInUser.getPhoneNo()).child(lActivity.servicList.get(position).getServiceName());
                        dRNew.setValue(lActivity.servicList.get(position));

                        DatabaseReference dRBid = FirebaseDatabase.getInstance().getReference(customerBid).child(lActivity.servicList.get(position).getBidCustomerNo()).child(lActivity.servicList.get(position).getServiceName());
                        dRBid.removeValue();

                        DatabaseReference dRNewBid = FirebaseDatabase.getInstance().getReference(vendorBid).child(loggedInUser.getPhoneNo()).child(lActivity.servicList.get(position).getServiceName());
                        dRNewBid.removeValue();

                        lActivity.servicList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(lActivity, "Bid Accepted Successfully...", Toast.LENGTH_SHORT).show();
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

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(lActivity);
                aBuilder.setTitle("SMS");
                aBuilder.setMessage("Are you sure, want to Reject the Bid ?");
                aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        lActivity.servicList.get(position).setBidAccepted("Reject");

                        DatabaseReference dRBid = FirebaseDatabase.getInstance().getReference(customerBid).child(lActivity.servicList.get(position).getBidCustomerNo()).child(lActivity.servicList.get(position).getServiceName());
                        dRBid.setValue(lActivity.servicList.get(position));

                        DatabaseReference dRNewBid = FirebaseDatabase.getInstance().getReference(vendorBid).child(loggedInUser.getPhoneNo()).child(lActivity.servicList.get(position).getServiceName());
                        dRNewBid.setValue(lActivity.servicList.get(position));
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

        holder.btnDeleteBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(lActivity);
                aBuilder.setTitle("SMS");
                aBuilder.setMessage("Are you sure, want to Delete the Bid ?");
                aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference dRNewBid = FirebaseDatabase.getInstance().getReference(vendorBid).child(loggedInUser.getPhoneNo()).child(lActivity.servicList.get(position).getServiceName());
                        dRNewBid.removeValue();
                        lActivity.servicList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(lActivity, "Deleted bid Successfully", Toast.LENGTH_SHORT).show();
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

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(lActivity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(lActivity, new String[]{android.Manifest.permission.CALL_PHONE},
                            123);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + lActivity.servicList.get(position).getBidCustomerNo()));
                    lActivity.startActivity(intent);
                    // else block means user has already accepted.And make your phone call here.
                }
            }
        });

        holder.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(lActivity);
                aBuilder.setTitle("SMS");
                aBuilder.setMessage("Are you sure, Service request fulfilled ?");
                aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        lActivity.servicList.get(position).setServiceStatus("Done");
                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(customerServiceRequest).child(lActivity.servicList.get(position).getBidCustomerNo()).child(lActivity.servicList.get(position).getServiceName());
                        dR.setValue(lActivity.servicList.get(position));
                        DatabaseReference dRNew = FirebaseDatabase.getInstance().getReference(vendorServiceRequest).child(loggedInUser.getPhoneNo()).child(lActivity.servicList.get(position).getServiceName());
                        dRNew.setValue(lActivity.servicList.get(position));
                        holder.tvCompleted.setVisibility(View.VISIBLE);
                        holder.btnComplete.setVisibility(View.GONE);
                        notifyDataSetChanged();
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
    public int getItemCount() {
        return lActivity.servicList.size();
    }

    class ViewHlder extends RecyclerView.ViewHolder {
        LinearLayout custmr_lay, libBidAmount;
        TextView tvServiceDesc, tvCustomerName, tvCustomerMobile, tvRejected, tvCompleted, tvPaimentStatus, tvBidAmount;
        TextView tvServiceType, tvServiceDate;
        TextView tvDuration;
        TextView tvServiceCost;
        ImageView imgDelete;
        LinearLayout linBid, linService, linCustomerName, linCustomerMobile, linServiceDate;
        Button btnAccept, btnReject, btnCall, btnDeleteBid, btnComplete;
        ImageView imgNavigation;

        public ViewHlder(View itemView) {
            super(itemView);
            custmr_lay = (LinearLayout) itemView.findViewById(R.id.custmr_lay);
            tvServiceDesc = (TextView) itemView.findViewById(R.id.tvServiceDesc);
            tvServiceType = (TextView) itemView.findViewById(R.id.tvServiceType);
            tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);
            tvServiceCost = (TextView) itemView.findViewById(R.id.tvServiceCost);
            tvServiceDate = itemView.findViewById(R.id.tvServiceDate);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            linBid = itemView.findViewById(R.id.linBid);
            linService = itemView.findViewById(R.id.linService);
            linCustomerName = itemView.findViewById(R.id.linCustomerName);
            linCustomerMobile = itemView.findViewById(R.id.linCustomerMobile);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvCustomerMobile = itemView.findViewById(R.id.tvCustomerMobile);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
            btnCall = itemView.findViewById(R.id.btnCall);
            tvRejected = itemView.findViewById(R.id.tvRejected);
            btnDeleteBid = itemView.findViewById(R.id.btnDeleteBid);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            tvCompleted = itemView.findViewById(R.id.tvCompleted);
            tvPaimentStatus = itemView.findViewById(R.id.tvPaimentStatus);
            tvBidAmount = itemView.findViewById(R.id.tvBidAmount);
            libBidAmount = itemView.findViewById(R.id.libBidAmount);
            linServiceDate = itemView.findViewById(R.id.linServiceDate);
            imgNavigation = itemView.findViewById(R.id.imgNavigation);

        }
    }

    public static String getDate(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/mm/dd");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
