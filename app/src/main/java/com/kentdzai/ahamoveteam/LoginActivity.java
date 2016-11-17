package com.kentdzai.ahamoveteam;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUsername, etPassword;
    Button btnLogin;
    CheckNetwork checkNetwork;

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
        btnLogin.setOnClickListener(this);
        checkNetwork = new CheckNetwork();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(checkNetwork, filter);
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
//            do something
            if (CheckNetwork.network)
                startActivity(new Intent(LoginActivity.this, ManagerActivity.class));
            else{
                Toast.makeText(this,"Kiểm tra lại kết nối..",Toast.LENGTH_LONG).show();
            }
        }
    }
}


