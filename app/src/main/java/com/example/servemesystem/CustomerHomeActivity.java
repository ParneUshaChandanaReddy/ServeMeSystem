package com.example.servemesystem;

import static com.example.servemesystem.Constants.isGuest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.servemesystem.fragments.CustomerBidsFragment;
import com.example.servemesystem.fragments.CustomerHomeFragment;
import com.example.servemesystem.fragments.CustomerServicesFragment;
import com.example.servemesystem.fragments.CustomerSettingsFragment;
import com.google.android.material.navigation.NavigationView;

public class CustomerHomeActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_home_activity);
        FragmentManager fMannager = getSupportFragmentManager();
        FragmentTransaction ftx = fMannager.beginTransaction();
        ftx.replace(R.id.frame, new CustomerHomeFragment());
        ftx.commit();
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);


        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_account) {
                    drawerLayout.closeDrawer(GravityCompat.START);

                    if (isGuest) {
                        Toast.makeText(CustomerHomeActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                    } else {
                        //   Toast.makeText(CustomerHomeActivity.this, "Clicked on My Account", Toast.LENGTH_SHORT).show();
                        FragmentManager fMannager = getSupportFragmentManager();
                        FragmentTransaction ftx = fMannager.beginTransaction();
                        ftx.replace(R.id.frame, new CustomerHomeFragment());
                        ftx.commit();
                    }
                    return false;
                }
                if (item.getItemId() == R.id.nav_settings) {
                    // Toast.makeText(CustomerHomeActivity.this, "Clicked on My Account", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    if (isGuest) {
                        Toast.makeText(CustomerHomeActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                    } else {
                        FragmentManager fMannager = getSupportFragmentManager();
                        FragmentTransaction ftx = fMannager.beginTransaction();
                        ftx.replace(R.id.frame, new CustomerSettingsFragment());
                        ftx.commit();
                    }
                    return false;
                }
                if (item.getItemId() == R.id.nav_bids) {
                    // Toast.makeText(CustomerHomeActivity.this, "Clicked on My Account", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    if (isGuest) {
                        Toast.makeText(CustomerHomeActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                    } else {
                        FragmentManager fMannager = getSupportFragmentManager();
                        FragmentTransaction ftx = fMannager.beginTransaction();
                        ftx.replace(R.id.frame, new CustomerBidsFragment());
                        ftx.commit();
                    }
                    return false;
                }
                if (item.getItemId() == R.id.nav_services) {
                    // Toast.makeText(CustomerHomeActivity.this, "Clicked on My Account", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    if (isGuest) {
                        Toast.makeText(CustomerHomeActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                    } else {
                        FragmentManager fMannager = getSupportFragmentManager();
                        FragmentTransaction ftx = fMannager.beginTransaction();
                        ftx.replace(R.id.frame, new CustomerServicesFragment());
                        ftx.commit();
                    }
                    return false;
                }
                if (item.getItemId() == R.id.nav_logout) {
                    drawerLayout.closeDrawer(GravityCompat.START);

                    if (isGuest) {
                        isGuest = false;
                        finish();
                    } else {
                        // Toast.makeText(CustomerHomeActivity.this, "Clicked on My Account", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder aBuilder = new AlertDialog.Builder(CustomerHomeActivity.this);
                        aBuilder.setTitle("SMS");
                        aBuilder.setMessage("Are you sure, want to Logout..?");
                        aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(CustomerHomeActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                                finishAffinity();
                                startActivity(new Intent(CustomerHomeActivity.this, VendorOrCustomerActivity.class));
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
                    return false;
                }
                return false;
            }
        });
        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            // Toast.makeText(this, "Cliccked", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isGuest) {
            super.onBackPressed();
        }
    }
}
