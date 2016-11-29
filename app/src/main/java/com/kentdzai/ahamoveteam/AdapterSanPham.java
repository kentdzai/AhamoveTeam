package com.kentdzai.ahamoveteam;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kentdzai.ahamoveteam.model.SanPham;

import java.util.ArrayList;

/**
 * Created by kentd on 18/11/2016.
 */

public class AdapterSanPham extends ArrayAdapter<SanPham> {
    Activity activity;
    int idLayout;
    ArrayList<SanPham> arrS;
    TextView tvTenSanPham, tvGiaSanPham, tvMoTa;

    public AdapterSanPham(Activity activity, int idLayout, ArrayList<SanPham> arrS) {
        super(activity, idLayout, arrS);
        this.activity = activity;
        this.idLayout = idLayout;
        this.arrS = arrS;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = activity.getLayoutInflater().inflate(idLayout, null);
        tvTenSanPham = (TextView) v.findViewById(R.id.tvTenSanPham);
        tvGiaSanPham = (TextView) v.findViewById(R.id.tvGiaSanPham);
        tvMoTa = (TextView) v.findViewById(R.id.tvMoTa);
        SanPham s = arrS.get(position);
        tvTenSanPham.setText(s.getTenSanPham());
        tvGiaSanPham.setText(String.valueOf(s.getGiaSanPham()));
        tvMoTa.setText(s.getMoTa());
        //ahihi
        return v;

    }
}
