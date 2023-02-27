package com.example.servemesystem.adapters;

import static com.example.servemesystem.Constants.customerKey;
import static com.example.servemesystem.Constants.customerService;
import static com.example.servemesystem.Constants.customerServiceRequest;
import static com.example.servemesystem.Constants.loggedInUser;
import static com.example.servemesystem.Constants.vendorServiceRequest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servemesystem.CustomerHomeActivity;
import com.example.servemesystem.R;
import com.example.servemesystem.VendorOrCustomerActivity;
import com.example.servemesystem.fragments.CustomerServicesFragment;
import com.example.servemesystem.model.RatingInfo;
import com.example.servemesystem.model.ServiceInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomerServicesAdapter extends RecyclerView.Adapter<CustomerServicesAdapter.ViewHlder> {
    CustomerServicesFragment lActivity;

    public CustomerServicesAdapter(CustomerServicesFragment CustomerServicesFragment) {
        this.lActivity = CustomerServicesFragment;
    }

    @NonNull
    @Override
    public CustomerServicesAdapter.ViewHlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.customer_my_services_adapter, parent, false);
        CustomerServicesAdapter.ViewHlder holder = new CustomerServicesAdapter.ViewHlder(view);
        return holder;
    }

    ServiceInfo ratingVendor;

    @Override
    public void onBindViewHolder(@NonNull final CustomerServicesAdapter.ViewHlder holder, @SuppressLint("RecyclerView") final int position) {

        ServiceInfo info = lActivity.servicList.get(position);
        holder.tvVendorName.setText(info.getVendorName());
        holder.tvServiceDesc.setText(info.getDesc());
        holder.tvDuration.setText(info.getDuration() + " Mins");
        holder.tvServiceCost.setText(info.getCost() + " $");

        if (info.getServiceStatus().equalsIgnoreCase("Accept")) {
            holder.tvServiceStatus.setText("Accepted");
            try {
                String servDate = getDate(info.getServiceDate());
                String todayDate = getDate(System.currentTimeMillis());
                // String CurrentDate = "09/24/2018";
                // String FinalDate = "09/26/2019";
                /*Date date1;
                Date date2;*/
                SimpleDateFormat dates = new SimpleDateFormat("yy/mm/dd");
                Date date1 = dates.parse(todayDate);
                Date date2 = dates.parse(servDate);
                long difference = Math.abs(date1.getTime() - date2.getTime());
                long differenceDates = difference / (24 * 60 * 60 * 1000);
                String dayDifference = Long.toString(differenceDates);
                Toast.makeText(lActivity.getActivity(), "" + dayDifference, Toast.LENGTH_SHORT).show();

                if (differenceDates > 1) {
                    holder.btnDateOrCancel.setVisibility(View.VISIBLE);
                } else {
                    holder.btnDateOrCancel.setVisibility(View.GONE);
                }
                //  textView.setText("The difference between two dates is " + dayDifference + " days");
            } catch (Exception exception) {
                Toast.makeText(lActivity.getActivity(), "Unable to find difference", Toast.LENGTH_SHORT).show();
            }
        } else if (info.getServiceStatus().equalsIgnoreCase("Done")) {
            holder.tvServiceStatus.setText("Done");
            holder.btnDone.setVisibility(View.VISIBLE);
        } else if (info.getServiceStatus().equalsIgnoreCase("Completed")) {
            holder.tvServiceStatus.setText("Completed");
            holder.btnDone.setVisibility(View.GONE);
        } else if (info.getServiceStatus().equalsIgnoreCase("Cancelled")) {
            holder.tvServiceStatus.setText("Cancelled");
            holder.btnDone.setVisibility(View.GONE);
            holder.btnPayment.setVisibility(View.GONE);
            holder.btnDateOrCancel.setVisibility(View.VISIBLE);
            holder.btnDateOrCancel.setText("Delete");
        }

        if (info.getPaymentStatus().equalsIgnoreCase("Paid")) {
            holder.btnPayment.setVisibility(View.GONE);
            holder.tvPaimentStatus.setVisibility(View.VISIBLE);
            //  holder.btnDateOrCancel.setText("");
            holder.btnDateOrCancel.setVisibility(View.GONE);
        } else {
            holder.btnPayment.setVisibility(View.VISIBLE);
            holder.tvPaimentStatus.setVisibility(View.GONE);
        }

        holder.btnDateOrCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.btnDateOrCancel.getText().toString().equalsIgnoreCase("Delete")) {
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(customerServiceRequest).child(lActivity.servicList.get(position).getBidCustomerNo()).child(lActivity.servicList.get(position).getServiceName()).child("serviceStatus");
                    dR.setValue("Cancelled");
                    DatabaseReference drVendor = FirebaseDatabase.getInstance().getReference(vendorServiceRequest).child(lActivity.servicList.get(position).getVendorPhone()).child(lActivity.servicList.get(position).getServiceName()).child("serviceStatus");
                    drVendor.setValue("Cancelled");
                } else {
                    android.app.AlertDialog.Builder aBuilder = new android.app.AlertDialog.Builder(lActivity.getActivity());
                    aBuilder.setTitle("SMS");
                    aBuilder.setMessage("Are you sure, want to Delete..?");
                    aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference dR = FirebaseDatabase.getInstance().getReference(customerServiceRequest).child(lActivity.servicList.get(position).getBidCustomerNo()).child(lActivity.servicList.get(position).getServiceName());
                            dR.removeValue();
                            DatabaseReference dRNew = FirebaseDatabase.getInstance().getReference(vendorServiceRequest).child(lActivity.servicList.get(position).getVendorPhone()).child(lActivity.servicList.get(position).getServiceName());
                            dRNew.removeValue();
                            Toast.makeText(lActivity.getActivity(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            lActivity.servicList.remove(position);
                            notifyDataSetChanged();
                            //  finishAffinity();
                            //  startActivity(new Intent(CustomerHomeActivity.this, VendorOrCustomerActivity.class));
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
            }
        });

        holder.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  showBidDialog(lActivity.servicList.get(position));
            }
        });

        holder.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPaymentDialog(info, position);
            }
        });
        holder.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  DatabaseReference dR = FirebaseDatabase.getInstance().getReference(customerServiceRequest).child(loggedInUser.getPhoneNo()).child(info.getServiceName()).child("serviceStatus");
                dR.setValue("Completed");
                DatabaseReference dRVendor = FirebaseDatabase.getInstance().getReference(vendorServiceRequest).child(info.getVendorPhone()).child(info.getServiceName()).child("serviceStatus");
                dRVendor.setValue("Completed");*/

                ProgressDialog pDialog = new ProgressDialog(lActivity.getActivity());
                pDialog.show();
                DatabaseReference dRRating = FirebaseDatabase.getInstance().getReference(customerService).child(info.getServiceName()).child(info.getVendorPhone());

                ratingVendor = new ServiceInfo();
                dRRating.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pDialog.dismiss();
                        if (snapshot.exists()) {
                            ratingVendor = snapshot.getValue(ServiceInfo.class);
                        }
                        showRatingDialog(ratingVendor, position);
                        dRRating.removeEventListener(this);
                        // dRRating.addValueEventListener(null);
                        return;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                //  lActivity.servicList.get(position).setPaymentStatus("Completed");
                //   Toast.makeText(lActivity.getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
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
        TextView tvServiceCost, tvServiceStatus, tvPaimentStatus, btnDone;
        Button btnSelect, btnBid, btnPayment, btnDateOrCancel;

        public ViewHlder(View itemView) {
            super(itemView);
            custmr_lay = (LinearLayout) itemView.findViewById(R.id.custmr_lay);
            tvServiceDesc = (TextView) itemView.findViewById(R.id.tvServiceDesc);
            tvVendorName = (TextView) itemView.findViewById(R.id.tvVendorName);
            tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);
            tvServiceCost = (TextView) itemView.findViewById(R.id.tvServiceCost);
            btnSelect = itemView.findViewById(R.id.btnSelect);
            btnBid = itemView.findViewById(R.id.btnBid);
            tvServiceStatus = itemView.findViewById(R.id.tvServiceStatus);
            btnPayment = itemView.findViewById(R.id.btnPayment);
            tvPaimentStatus = itemView.findViewById(R.id.tvPaimentStatus);
            btnDone = itemView.findViewById(R.id.btnDone);
            btnDateOrCancel = itemView.findViewById(R.id.btnDateOrCancel);
        }
    }

    public void showPaymentDialog(ServiceInfo serviceInfo, int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(lActivity.getActivity());
        LayoutInflater inflater = lActivity.getActivity().getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.payment_dialog, null);
        dialogBuilder.setView(dialogView);
        CheckBox chkRewards = dialogView.findViewById(R.id.chkRewards);
        TextView tvRewards = dialogView.findViewById(R.id.tvRewards);
        if (loggedInUser.getRewardPoints() >= 50) {
            chkRewards.setEnabled(true);
        } else {
            chkRewards.setEnabled(false);
        }

        tvRewards.setText(loggedInUser.getRewardPoints() + "");

        EditText etAMount = dialogView.findViewById(R.id.etAMount);
        final EditText etCardNo = (EditText) dialogView.findViewById(R.id.etCardNo);
        final EditText etMonth = (EditText) dialogView.findViewById(R.id.etMonth);
        final EditText etYear = (EditText) dialogView.findViewById(R.id.etYear);
        EditText etCvv = dialogView.findViewById(R.id.etCvv);
        Spinner spinPaymentType = dialogView.findViewById(R.id.spinPaymentType);
        LinearLayout linCardDetails = dialogView.findViewById(R.id.linCardDetails);

        final Button btnSave = (Button) dialogView.findViewById(R.id.btnSave);
        final Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        etAMount.setText("$ " + serviceInfo.getBidAmount());

        chkRewards.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    int amounnt = serviceInfo.getBidAmount() - 1;
                    etAMount.setText("$ " + amounnt);
                } else {
                    int amounnt = serviceInfo.getBidAmount();
                    etAMount.setText("$ " + amounnt);
                }
            }
        });

        spinPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 3) {
                    linCardDetails.setVisibility(View.GONE);
                } else {
                    linCardDetails.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final AlertDialog b = dialogBuilder.create();
        b.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spinPaymentType.getSelectedItemPosition() != 3) {
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
                }

                if (chkRewards.isChecked()) {
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(customerKey).child(loggedInUser.getPhoneNo()).child("rewardPoints");
                    int points = loggedInUser.getRewardPoints() - 50;
                    dR.setValue(points);
                }

                DatabaseReference dR = FirebaseDatabase.getInstance().getReference(customerServiceRequest).child(loggedInUser.getPhoneNo()).child(serviceInfo.getServiceName()).child("paymentStatus");
                dR.setValue("Paid");

                DatabaseReference dRVendor = FirebaseDatabase.getInstance().getReference(vendorServiceRequest).child(serviceInfo.getVendorPhone()).child(serviceInfo.getServiceName()).child("paymentStatus");
                dRVendor.setValue("Paid");

                //DatabaseReference dRVendor = FirebaseDatabase.getInstance().getReference(vendorServiceRequest).child(serviceInfo.getVendorPhone()).child(serviceInfo.getServiceName()).child("paymentStatus");
                // dRVendor.setValue("Paid");

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

    public void showRatingDialog(ServiceInfo serviceInfo, int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(lActivity.getActivity());
        LayoutInflater inflater = lActivity.getActivity().getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.rating_popup, null);
        dialogBuilder.setView(dialogView);

        //    final EditText etFeedback = (EditText) dialogView.findViewById(R.id.etFeedback);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        TextView tvVendor = dialogView.findViewById(R.id.tvVendor);
        EditText etFeedback = dialogView.findViewById(R.id.etFeedback);

        final Button btnSubmitRating = (Button) dialogView.findViewById(R.id.btnSubmitRating);
        final Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        tvVendor.setText(serviceInfo.getVendorName());


        final AlertDialog b = dialogBuilder.create();
        b.show();

        btnSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                float newRating = serviceInfo.getRating();
                int noOFRatings = serviceInfo.getNoOfRatings();

                if (noOFRatings > 0) {
                    newRating = newRating + ratingBar.getRating();
                    noOFRatings = noOFRatings + 1;
                } else {
                    newRating = ratingBar.getRating();
                    noOFRatings = 1;
                }

                if (ratingBar.getRating() == 0) {
                    Toast.makeText(lActivity.getActivity(), "Please give rating", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etFeedback.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(lActivity.getActivity(), "Please provide your feedback", Toast.LENGTH_SHORT).show();
                    // Toast.makeText(lActivity, "Please provide your feedback", Toast.LENGTH_SHORT).show();
                    return;
                }

                RatingInfo info = new RatingInfo();
                info.setRating(ratingBar.getRating());
                info.setFeedback(etFeedback.getText().toString());

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                String key = reference.push().getKey();
                reference.child("Reviews").child(serviceInfo.getVendorPhone()).child(key).setValue(info);

                DatabaseReference dRvendorRaring = FirebaseDatabase.getInstance().getReference(customerService).child(serviceInfo.getServiceName()).child(serviceInfo.getVendorPhone()).child("rating");


                DatabaseReference dRRating = FirebaseDatabase.getInstance().getReference(customerService).child(serviceInfo.getServiceName()).child(serviceInfo.getVendorPhone()).child("rating");
                DatabaseReference dRNoOfRatings = FirebaseDatabase.getInstance().getReference(customerService).child(serviceInfo.getServiceName()).child(serviceInfo.getVendorPhone()).child("noOfRatings");

                dRRating.setValue(newRating);
                dRNoOfRatings.setValue(noOFRatings);

                DatabaseReference dR = FirebaseDatabase.getInstance().getReference(customerServiceRequest).child(loggedInUser.getPhoneNo()).child(serviceInfo.getServiceName()).child("serviceStatus");
                dR.setValue("Completed");
                DatabaseReference dRVendor = FirebaseDatabase.getInstance().getReference(vendorServiceRequest).child(serviceInfo.getVendorPhone()).child(serviceInfo.getServiceName()).child("serviceStatus");
                dRVendor.setValue("Completed");

                DatabaseReference dRPoints = FirebaseDatabase.getInstance().getReference(customerKey).child(loggedInUser.getPhoneNo()).child("rewardPoints");
                int points = loggedInUser.getRewardPoints() + 10;
                dRPoints.setValue(points);

                lActivity.servicList.get(position).setServiceStatus("Completed");
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

    public static String getDate(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/mm/dd");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}

