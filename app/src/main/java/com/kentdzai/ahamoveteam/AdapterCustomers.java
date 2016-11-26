package com.kentdzai.ahamoveteam;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kentdzai.ahamoveteam.model.HoaDon;
import com.kentdzai.ahamoveteam.model.KhachHang;

import java.util.ArrayList;

/**
 * Created by kentd on 18/11/2016.
 */

public class AdapterCustomers extends ArrayAdapter<KhachHang> {
    Activity activity;
    int idLayout;
    ArrayList<KhachHang> arrK;
    TextView tvCusomerID, tvCustomerName, tvCustomerPhone, tvCustomerAddress;

    public AdapterCustomers(Activity activity, int idLayout, ArrayList<KhachHang> arrK) {
        super(activity, idLayout, arrK);
        this.activity = activity;
        this.idLayout = idLayout;
        this.arrK = arrK;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = activity.getLayoutInflater().inflate(idLayout, null);
        tvCusomerID = (TextView) v.findViewById(R.id.tvCustomerID);
        tvCustomerName = (TextView) v.findViewById(R.id.tvCustomerName);
        tvCustomerPhone = (TextView) v.findViewById(R.id.tvCustomerPhone);
        tvCustomerAddress = (TextView) v.findViewById(R.id.tvCustomerAddress);
        KhachHang kh = arrK.get(position);
        tvCusomerID.setText(String.valueOf(kh.getId()));
        tvCustomerName.setText(kh.getName());
        tvCustomerPhone.setText(kh.getPhone());
        tvCustomerAddress.setText(kh.getAddress());
        return v;
    }
}
