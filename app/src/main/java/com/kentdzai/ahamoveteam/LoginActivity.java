package com.kentdzai.ahamoveteam;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUsername, etPassword;
    Button btnLogin;
    CheckNetwork checkNetwork;
    CheckBox saveLogin;
    SharedPreferences pref;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        saveLogin = (CheckBox) findViewById(R.id.saveLogin);
        btnLogin.setOnClickListener(this);
        checkNetwork = new CheckNetwork();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(checkNetwork, filter);
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        edit = pref.edit();
        if (pref.getBoolean("logined", false)) {
            startActivity(new Intent(LoginActivity.this, ManagerActivity.class));
        }

    }

    private boolean checkValid(String s) {
        if (s.length() == 0) {
            return false;
        } else
            return true;
    }

    @Override
    public void onClick(View v) {
        String user, pass;
        if (v.getId() == R.id.btnLogin) {
            user = etUsername.getText().toString().trim();
            pass = etPassword.getText().toString().trim();
            if (!checkValid(user)) {
                etUsername.setError("Username không được để trống");
                etUsername.requestFocus();
                return;
            } else if (!checkValid(pass)) {
                etPassword.setError("Password không được để trống");
                etPassword.requestFocus();
                return;
            }
            if (CheckNetwork.network) {
                login(user, pass);
            } else {
                Toast.makeText(this, "Kiểm tra lại kết nối..", Toast.LENGTH_LONG).show();
            }
        }
    }


    void login(final String user, final String pass) {
        Ion.with(getApplicationContext())
//                .load("http://kentdzai.tk/aha/login.php")
                .load("http://192.168.1.102/php/aha/login.php")
                .setBodyParameter("username", user)
                .setBodyParameter("password", pass)
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject rr) {
                String result = rr.get("result").getAsString();
                if (result.trim().equals("success")) {
                    if (saveLogin.isChecked()) {
                        edit.putBoolean("logined", true);
                    } else {
                        edit.putBoolean("logined", false);
                    }
                    edit.putString("username", user);
                    edit.putString("password", pass);
                    edit.putString("maNhanVien", rr.get("maNhanVien").getAsString());
                    edit.putString("tenNhanVien", rr.get("tenNhanVien").getAsString());
                    edit.putString("diaChiNV", rr.get("diaChiNV").getAsString());
                    edit.putString("soDienThoaiNV", rr.get("soDienThoaiNV").getAsString());
                    edit.commit();
                    startActivity(new Intent(LoginActivity.this, ManagerActivity.class));
                } else if (result.trim().equals("wrongusername")) {
                    etUsername.setError("Sai tên đẳng nhập");
                    etUsername.requestFocus();
                } else if (result.trim().equals("failure")) {
                    etPassword.setError("Sai mật khẩu");
                    etPassword.requestFocus();
                }
            }
        });
    }
}


