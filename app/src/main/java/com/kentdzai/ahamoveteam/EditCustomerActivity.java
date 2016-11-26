package com.kentdzai.ahamoveteam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kentdzai.ahamoveteam.model.KhachHang;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class EditCustomerActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);
        server = getResources().getString(R.string.server);
        init();
    }

    String server;
    EditText etEditID, etEditName, etEditPhone, etEditAddress;
    Button btnEdit;
    KhachHang kh;

    private void init() {
        etEditID = (EditText) findViewById(R.id.etEditID);
        etEditName = (EditText) findViewById(R.id.etEditName);
        etEditPhone = (EditText) findViewById(R.id.etEditPhone);
        etEditAddress = (EditText) findViewById(R.id.etEditAddress);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(this);
        Intent it = getIntent();
        if (it != null) {
            kh = (KhachHang) it.getSerializableExtra("customer");
        }
        if (kh != null) {
            etEditID.setText(String.valueOf(kh.getId()));
            etEditName.setText(kh.getName());
            etEditPhone.setText(kh.getPhone());
            etEditAddress.setText(kh.getAddress());
        }
    }

    public boolean checkValid(String s) {
        if (s.length() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEdit:
                String ten = etEditName.getText().toString().trim();
                String diaChi = etEditAddress.getText().toString().trim();
                if (!checkValid(ten)) {
                    etEditName.setError("Tên không được để trống");
                    etEditName.requestFocus();
                    return;
                }
                if (!checkValid(diaChi)) {
                    etEditAddress.setError("Tên không được để trống");
                    etEditAddress.requestFocus();
                    return;
                }
                Ion.with(getApplicationContext())
                        .load(server + "updatecustomer.php")
                        .setBodyParameter("maKH", String.valueOf(kh.getId()))
                        .setBodyParameter("tenKH", ten)
                        .setBodyParameter("diaChiKH", diaChi)
                        .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null) {
                            String rs = result.get("update").getAsString();
                            if (rs.equals("true")) {
                                Toast.makeText(EditCustomerActivity.this
                                        , "Cập nhật thành công"
                                        , Toast.LENGTH_LONG).show();
                            }
                            Intent it = new Intent(EditCustomerActivity.this, ManagerActivity.class);
                            it.putExtra("from", "EditCustomers");
                            startActivity(it);
                            finish();
                        }
                    }
                });
                break;
        }
    }
}
