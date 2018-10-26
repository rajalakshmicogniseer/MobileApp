package com.ignovate.lectrefymob.userprofile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ignovate.lectrefymob.R;
import com.ignovate.lectrefymob.interfaces.setTitle;
import com.ignovate.lectrefymob.login.LoginActivity;
import com.ignovate.lectrefymob.utils.LoadingIndicator;
import com.ignovate.lectrefymob.utils.SessionManager;
import com.ignovate.lectrefymob.webservice.ConnectivityReceiver;
import com.ignovate.lectrefymob.webservice.MyApplication;


public class UserProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,setTitle, ConnectivityReceiver.ConnectivityReceiverListener {

    private SessionManager session;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        session = new SessionManager(UserProfileActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        displaySelectedScreen(R.id.nav_profile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        Fragment fragment = null;
        Intent intent = getIntent();
        String id = intent.getStringExtra("USER_ID");
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_profile:
                fragment = MyProfileFragment.newInstance("", id);
                break;
            case R.id.nav_reset:
                fragment = ResetPasswordFragment.newInstance("", id);
                break;
            case R.id.nav_logout:
                session.logoutUser();
                finish();
                break;
        }
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void title(String s) {
        toolbar.setTitle(s);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        LoadingIndicator.alertDialog(UserProfileActivity.this, isConnected);
    }
}
