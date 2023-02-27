package com.example.servemesystem;

import static com.example.servemesystem.Constants.isCustomer;
import static com.example.servemesystem.Constants.latLng;
import static com.example.servemesystem.Constants.loggedInUser;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.servemesystem.model.ServiceInfo;
import com.example.servemesystem.model.VendorInfo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActiivty extends AppCompatActivity {

    RadioGroup radGroupLoginSignup;
    LinearLayout linSignup;
    Button btnLoginSignup;
    TextView txtFotgotPwd, txtUserType, tvTnC;
    EditText etVendorEmail, etPassword, etVendorName, etVendorMobile, etVendorAddress;
    TextInputLayout txtVendor;
    DatabaseReference dR;
    Button btnLocation;
    LatLng selectedLatLng;
    CheckBox chkTermsConditions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        radGroupLoginSignup = findViewById(R.id.radGroupLoginSignup);
        linSignup = findViewById(R.id.linSignup);
        btnLoginSignup = findViewById(R.id.btnLoginSignup);
        txtFotgotPwd = findViewById(R.id.txtFotgotPwd);

        etVendorEmail = findViewById(R.id.etVendorEmail);
        etPassword = findViewById(R.id.etPassword);
        etVendorName = findViewById(R.id.etVendorName);
        etVendorMobile = findViewById(R.id.etVendorMobile);
        etVendorAddress = findViewById(R.id.etVendorAddress);
        txtUserType = findViewById(R.id.txtUserType);
        txtVendor = findViewById(R.id.txtVendor);
        btnLocation = findViewById(R.id.btnLocation);
        chkTermsConditions = findViewById(R.id.chkTermsConditions);
        tvTnC = findViewById(R.id.tvTnC);

        tvTnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActiivty.this, TnCActivity.class));
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActiivty.this, MapsActivity.class);
                startActivityForResult(intent, 123);
                // startActivity(new Intent(LoginActiivty.this, MapsActivity.class));
            }
        });
        txtFotgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePwdDialog();
            }
        });

        if (isCustomer) {
            txtUserType.setText("Customer");
            txtVendor.setHint("Enter Customer Name");
        } else {
            txtUserType.setText("Vendor");
            txtVendor.setHint("Enter Vendor or Business Name");
        }

        btnLoginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (isCustomer) {
                    dR = FirebaseDatabase.getInstance().getReference(Constants.customerKey).child(etVendorMobile.getText().toString());
                } else {
                    dR = FirebaseDatabase.getInstance().getReference(Constants.vendorKey).child(etVendorMobile.getText().toString());
                }
                if (radGroupLoginSignup.getCheckedRadioButtonId() == R.id.radLogin) {
                    validateAndLogin();
                } else {
                    validateAndSignUp();
                }
            }
        });

        radGroupLoginSignup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                clearAllFields();
                etVendorMobile.requestFocus();
                if (i == R.id.radLogin) {
                    linSignup.setVisibility(View.GONE);
                    btnLoginSignup.setText("Login");
                    txtFotgotPwd.setVisibility(View.VISIBLE);
                } else {
                    linSignup.setVisibility(View.VISIBLE);
                    btnLoginSignup.setText("Signup");
                    txtFotgotPwd.setVisibility(View.GONE);
                    btnLoginSignup.setEnabled(false);
                }
            }
        });
    }

    private void validateAndLogin() {

        if (etVendorMobile.getText().toString().equals("")) {
            Toast.makeText(this, "Enter Mobile No", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    VendorInfo info = snapshot.getValue(VendorInfo.class);
                    if (info.getPassword().equalsIgnoreCase(etPassword.getText().toString())) {
                        Toast.makeText(LoginActiivty.this, "Login Success", Toast.LENGTH_SHORT).show();
                        Constants.loggedInUser = info;
                        //  intent.putExtra("vendor", info);
                        if (isCustomer) {
                            startActivity(new Intent(LoginActiivty.this, CustomerHomeActivity.class));
                        } else {
                            startActivity(new Intent(LoginActiivty.this, VendorHomeActivity.class));

                        }
                    } else {
                        Toast.makeText(LoginActiivty.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActiivty.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void validateAndSignUp() {
        //etVendorEmail, etPassword, etVendorName, etVendorMobile, etVendorAddress
        if (etVendorMobile.getText().toString().equals("")) {
            Toast.makeText(this, "Enter Mobile No", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etVendorMobile.getText().toString().length() < 10) {
            Toast.makeText(this, "Enter Valid mobile No", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etVendorEmail.getText().toString().equals("")) {
            Toast.makeText(this, "Enter Email id", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etVendorName.getText().toString().equals("")) {
            Toast.makeText(this, "Enter Vendor Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etVendorAddress.getText().toString().equals("")) {
            Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!chkTermsConditions.isChecked()) {
            Toast.makeText(this, "Agree to terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog pDialog = new ProgressDialog(LoginActiivty.this);
        pDialog.setTitle("Please wait while saving the data...");
        pDialog.show();

        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    pDialog.dismiss();
                    Toast.makeText(LoginActiivty.this, "User Already Exists, login to access the account", Toast.LENGTH_SHORT).show();
                } else {

                    // RatingBar ratingBar;
                    VendorInfo info = new VendorInfo();
                    info.setEmailId(etVendorEmail.getText().toString());
                    info.setPhoneNo(etVendorMobile.getText().toString());
                    info.setAddress(etVendorAddress.getText().toString());
                    info.setPassword(etPassword.getText().toString());
                    info.setApprovedVendor(false);
                    info.setName(etVendorName.getText().toString());
                    info.setRating(0.f);
                    info.setNoOfRatings(0);
                    info.setLat(selectedLatLng.latitude);
                    info.setLang(selectedLatLng.longitude);
                    if (isCustomer) {
                        info.setRewardPoints(50);
                    }
                    // info.setLatLng("12.333,13,222");
                    dR.setValue(info);
                    pDialog.dismiss();
                    clearAllFields();
                    Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pDialog.dismiss();
                Toast.makeText(LoginActiivty.this, "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void clearAllFields() {
        etVendorEmail.setText("");
        etVendorMobile.setText("");
        etVendorAddress.setText("");
        etPassword.setText("");
        etVendorName.setText("");
    }

    public void showChangePwdDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.change_password, null);

        dialogBuilder.setView(dialogView);
        final EditText etMobNo = (EditText) dialogView.findViewById(R.id.etMobileNo);
        final EditText etPasseord = (EditText) dialogView.findViewById(R.id.etPassword);
        final EditText etConfirmPassword = (EditText) dialogView.findViewById(R.id.etConfirmPwd);

        final Button btnSave = (Button) dialogView.findViewById(R.id.btnSave);
        final Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);

        //dialogBuilder.setTitle("Add Service:");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etMobNo.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(LoginActiivty.this, "Enter Mobile No", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etMobNo.getText().toString().length() != 10) {
                    Toast.makeText(LoginActiivty.this, "Enter Valid Mobile No", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etPasseord.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(LoginActiivty.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etConfirmPassword.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(LoginActiivty.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!etPasseord.getText().toString().equalsIgnoreCase(etConfirmPassword.getText().toString())) {
                    Toast.makeText(LoginActiivty.this, "Password and confirm Password should be same..", Toast.LENGTH_SHORT).show();
                    return;
                }

                checkIfUserExiss(etMobNo.getText().toString(), etPasseord.getText().toString());
                // Toast.makeText(LoginActiivty.this, "Updated Password Successfully", Toast.LENGTH_SHORT).show();
                b.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  deleteArtist(user.getBoxnumber());
                b.dismiss();
            }
        });

    }

    private void checkIfUserExiss(String mobNo, String pwd) {

        if (isCustomer) {
            dR = FirebaseDatabase.getInstance().getReference(Constants.customerKey).child(mobNo);
        } else {
            dR = FirebaseDatabase.getInstance().getReference(Constants.vendorKey).child(mobNo);
        }
        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    changePassword(mobNo, pwd);
                } else {
                    Toast.makeText(LoginActiivty.this, "User Does not exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void changePassword(String mobNo, String pwd) {
        DatabaseReference dRPwd;
        if (isCustomer) {
            dRPwd = FirebaseDatabase.getInstance().getReference(Constants.customerKey).child(mobNo).child("password");
        } else {
            dRPwd = FirebaseDatabase.getInstance().getReference(Constants.vendorKey).child(mobNo).child("password");
        }
        dRPwd.setValue(pwd);
        Toast.makeText(LoginActiivty.this, "Updated Password Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            if (requestCode == 123) {
                selectedLatLng = Constants.latLng;
                Toast.makeText(this, "==" + selectedLatLng.toString(), Toast.LENGTH_SHORT).show();

                if (btnLoginSignup.getText().toString().equalsIgnoreCase("Signup")) {
                    btnLoginSignup.setEnabled(true);
                }

                //  latLng.latitude;
                //  latLng.longitude;
                //data.get("location");
            }
        } else {
            Toast.makeText(this, "Mark your location", Toast.LENGTH_SHORT).show();
        }
    }
}
