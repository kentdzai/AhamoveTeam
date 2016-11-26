package com.kentdzai.ahamoveteam;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kentdzai.ahamoveteam.model.HoaDon;
import com.kentdzai.ahamoveteam.model.SanPham;

import java.util.ArrayList;

/**
 * Created by kentd on 18/11/2016.
 */

public class AdapterDonHang extends ArrayAdapter<HoaDon> {
    Activity activity;
    int idLayout;
    ArrayList<HoaDon> arrH;
    TextView tvMaHoaDon, tvTenKhachHang, tvTenMay, tvSoLuong, tvTongTien;

    public AdapterDonHang(Activity activity, int idLayout, ArrayList<HoaDon> arrH) {
        super(activity, idLayout, arrH);
        this.activity = activity;
        this.idLayout = idLayout;
        this.arrH = arrH;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = activity.getLayoutInflater().inflate(idLayout, null);
        tvMaHoaDon = (TextView) v.findViewById(R.id.tvMaHoaDon);
        tvTenKhachHang = (TextView) v.findViewById(R.id.tvTenKhachHang);
        tvTenMay = (TextView) v.findViewById(R.id.tvTenMay);
        tvSoLuong = (TextView) v.findViewById(R.id.tvSoLuong);
        tvTongTien = (TextView) v.findViewById(R.id.tvTongTien);
        HoaDon h = arrH.get(position);
        tvMaHoaDon.setText(String.valueOf(h.getMaHoaDon()));
        tvTenKhachHang.setText(h.getTenKhachHang());
        tvTenMay.setText(h.getTenMay());
        tvSoLuong.setText(String.valueOf(h.getSoLuong()));
        tvTongTien.setText(String.valueOf(h.getTongTien()));
        return v;
    }
}
