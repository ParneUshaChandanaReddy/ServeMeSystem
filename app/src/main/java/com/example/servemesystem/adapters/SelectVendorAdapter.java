package com.example.servemesystem.adapters;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.servemesystem.Constants.isGuest;
import static com.example.servemesystem.Constants.loggedInUser;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servemesystem.Constants;
import com.example.servemesystem.VendorSelectionActivity;
import com.example.servemesystem.R;
import com.example.servemesystem.model.ServiceInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SelectVendorAdapter extends RecyclerView.Adapter<SelectVendorAdapter.ViewHlder> implements Filterable {
    VendorSelectionActivity lActivity;

    private SelectVendorAdapter.MyFilter mFilter;
    List<ServiceInfo> filteredList;

    public SelectVendorAdapter(VendorSelectionActivity VendorSelectionActivity, List<ServiceInfo> filteredList) {
        this.lActivity = VendorSelectionActivity;
        this.filteredList = filteredList;
        this.mFilter = new SelectVendorAdapter.MyFilter(SelectVendorAdapter.this);
    }

    @NonNull
    @Override
    public SelectVendorAdapter.ViewHlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.vendor_select_item, parent, false);
        SelectVendorAdapter.ViewHlder holder = new SelectVendorAdapter.ViewHlder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectVendorAdapter.ViewHlder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tvVendorName.setText(filteredList.get(position).getVendorName());
        holder.tvServiceDesc.setText(filteredList.get(position).getDesc());
        holder.tvDuration.setText(filteredList.get(position).getDuration() + " Mins");
        holder.tvServiceCost.setText(filteredList.get(position).getCost() + " $");
        holder.tvVendorAddress.setText(filteredList.get(position).getVendorAddress());
        holder.tvNoOfRatings.setText(lActivity.servicList.get(position).getNoOfRatings() + " Ratings");

        float rating = lActivity.servicList.get(position).getRating() / lActivity.servicList.get(position).getNoOfRatings();
        holder.ratingBar.setRating(rating);

        holder.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGuest) {
                    Toast.makeText(lActivity, "Please login to continue", Toast.LENGTH_SHORT).show();
                } else {
                    showBidDialog(filteredList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    class ViewHlder extends RecyclerView.ViewHolder {
        LinearLayout custmr_lay;
        TextView tvServiceDesc;
        TextView tvVendorName;
        TextView tvDuration;
        TextView tvServiceCost, tvVendorAddress;
        Button btnSelect, btnBid;
        RatingBar ratingBar;
        TextView tvNoOfRatings;

        public ViewHlder(View itemView) {
            super(itemView);
            custmr_lay = (LinearLayout) itemView.findViewById(R.id.custmr_lay);
            tvServiceDesc = (TextView) itemView.findViewById(R.id.tvServiceDesc);
            tvVendorName = (TextView) itemView.findViewById(R.id.tvVendorName);
            tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);
            tvServiceCost = (TextView) itemView.findViewById(R.id.tvServiceCost);
            btnSelect = itemView.findViewById(R.id.btnSelect);
            btnBid = itemView.findViewById(R.id.btnBid);
            tvVendorAddress = itemView.findViewById(R.id.tvVendorAddress);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvNoOfRatings = itemView.findViewById(R.id.tvNoOfRatings);
        }
    }

    public void showBidDialog(ServiceInfo mServiceInfo) {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(lActivity);
        LayoutInflater inflater = lActivity.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.bid_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText etServiceDescription = dialogView.findViewById(R.id.etServiceDescription);
        final EditText etBidDuration = dialogView.findViewById(R.id.etBidDuration);
        final EditText etBidCost = dialogView.findViewById(R.id.etBidCost);
        final EditText etServiceType = dialogView.findViewById(R.id.etServiceType);
        final Button btnSave = dialogView.findViewById(R.id.btnSave);
        final Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        final TextView tvDate = dialogView.findViewById(R.id.tvDate);

        etBidCost.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    try {
                        InputMethodManager imm = (InputMethodManager) lActivity.getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(lActivity.getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager) lActivity.getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(lActivity.getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                Calendar cal = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(lActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        tvDate.setText(year + "/" + month + "/" + day);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                dpd.getDatePicker().setMinDate(new Date().getTime());
                dpd.show();
            }
        });

        etServiceType.setText(mServiceInfo.getServiceName());
        etBidCost.setText(mServiceInfo.getCost() + "");
        etBidDuration.setText(mServiceInfo.getDuration() + "");
        etServiceDescription.setText(mServiceInfo.getDesc());

        final AlertDialog b = dialogBuilder.create();
        b.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etBidCost.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(lActivity, "Enter Bid amount", Toast.LENGTH_SHORT).show();
                    return;
                } else if (tvDate.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(lActivity, "Enter Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setServiceName(etServiceType.getText().toString());
                serviceInfo.setDesc(etServiceDescription.getText().toString());
                serviceInfo.setCost(Integer.parseInt(mServiceInfo.getCost() + ""));
                serviceInfo.setBidAmount(Integer.parseInt(etBidCost.getText().toString()));
                serviceInfo.setDuration(Integer.parseInt(etBidDuration.getText().toString()));
                serviceInfo.setVendorName(mServiceInfo.getVendorName());
                serviceInfo.setVendorAddress(mServiceInfo.getVendorAddress());
                serviceInfo.setBidCustomerNo(loggedInUser.getPhoneNo());
                serviceInfo.setBidCustomerName(loggedInUser.getName());
                serviceInfo.setVendorPhone(mServiceInfo.getVendorPhone());
                serviceInfo.setServiceAccepted(false);
                String givenDateString = tvDate.getText().toString();

                serviceInfo.setLat(mServiceInfo.getLat());
                serviceInfo.setLang(mServiceInfo.getLang());

                serviceInfo.setCusLat(loggedInUser.getLat());
                serviceInfo.setCusLang(loggedInUser.getLang());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");
                long timeInMilliseconds = System.currentTimeMillis();
                try {
                    Date mDate = sdf.parse(givenDateString);
                    timeInMilliseconds = mDate.getTime();
                    System.out.println("Date in milli :: " + timeInMilliseconds);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                serviceInfo.setServiceDate(timeInMilliseconds);

                DatabaseReference dR = FirebaseDatabase.getInstance().getReference(Constants.customerBid).child(loggedInUser.getPhoneNo()).child(serviceInfo.getServiceName());
                dR.setValue(serviceInfo);
                DatabaseReference dRNew = FirebaseDatabase.getInstance().getReference(Constants.vendorBid).child(mServiceInfo.getVendorPhone()).child(serviceInfo.getServiceName());
                dRNew.setValue(serviceInfo);

                Toast.makeText(lActivity, "Bid Sent Successfully", Toast.LENGTH_SHORT).show();
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

    class MyFilter extends Filter {

        private SelectVendorAdapter mAdapter;

        private MyFilter(SelectVendorAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            lActivity.filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                lActivity.filteredList.addAll(lActivity.servicList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final ServiceInfo mWords : lActivity.servicList) {

                    if (VendorSelectionActivity.search_by_edtxt.getText().toString().trim().equalsIgnoreCase("Name")) {
                        if (mWords.getVendorName().toLowerCase().startsWith(filterPattern)) {
                            lActivity.filteredList.add(mWords);
                        }
                    } else if (VendorSelectionActivity.search_by_edtxt.getText().toString().trim().equalsIgnoreCase("Address")) {
                        if (mWords.getVendorAddress().toLowerCase().startsWith(filterPattern)) {
                            lActivity.filteredList.add(mWords);
                        }
                    }

                }
            }
            results.values = lActivity.filteredList;
            results.count = lActivity.filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.mAdapter.notifyDataSetChanged();

        }
    }


}
