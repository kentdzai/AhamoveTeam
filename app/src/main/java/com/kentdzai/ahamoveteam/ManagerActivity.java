package com.kentdzai.ahamoveteam;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.TextView;
import android.widget.Toast;

import com.kentdzai.ahamoveteam.tab.TabCustomers;
import com.kentdzai.ahamoveteam.tab.TabNewOrder;
import com.kentdzai.ahamoveteam.tab.TabOrderList;
import com.kentdzai.ahamoveteam.tab.TabProductList;

import java.util.List;

public class ManagerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    BroadcastReceiver checkInternet;

    TextView tvTenDangNhap, tvTenNhanVien;
    SharedPreferences pref;
    String tenNv, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_newOrder);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main, new TabNewOrder()).commit();

        pref = getSharedPreferences("Login",MODE_PRIVATE);
        userName = pref.getString("username","");
        tenNv = pref.getString("tenNhanVien","");

        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_manager);
        tvTenDangNhap = (TextView) hView.findViewById(R.id.tvTenDangNhap);
        tvTenNhanVien = (TextView) hView.findViewById(R.id.tvTenNhanVien);
        tvTenDangNhap.setText(userName);
        tvTenNhanVien.setText(tenNv);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_newOrder:
                fragment = new TabNewOrder();
                break;
            case R.id.nav_listProduct:
                fragment = new TabProductList();
                break;
            case R.id.nav_orderList:
                fragment = new TabOrderList();
                break;
            case R.id.nav_customers:
                fragment = new TabCustomers();
                break;
            case R.id.nav_logout:
                SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.clear();
                edit.commit();
                startActivity(new Intent(ManagerActivity.this, LoginActivity.class));
                break;

//            case R.id.nav_mark:
//                fragment = new TabMark();
//                break;
//            case R.id.nav_listid:
//                fragment = new TabListID();
//                break;


        }
        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.main, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent it = getIntent();
        MyLog.e("resume");
//        if (it != null) {
//            String s = it.getStringExtra("from");
//            if (s.equals("EditCustomers")) {
//                FragmentManager manager = getSupportFragmentManager();
//                manager.beginTransaction().replace(R.id.main, new TabCustomers()).commit();
//            }
//        }
    }
}
