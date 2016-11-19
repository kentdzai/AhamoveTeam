package com.kentdzai.ahamoveteam.tab;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kentdzai.ahamoveteam.MyLog;
import com.kentdzai.ahamoveteam.R;
import com.kentdzai.ahamoveteam.model.DatabaseSanPham;
import com.kentdzai.ahamoveteam.model.LoaiSanPham;
import com.kentdzai.ahamoveteam.model.SanPham;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class TabNewOrder extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    public TabNewOrder() {
    }

    public static TabNewOrder newInstance(String param1, String param2) {
        TabNewOrder fragment = new TabNewOrder();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    DatabaseSanPham db;

    ArrayList<LoaiSanPham> arrLoaiSanPham;
    ArrayList<SanPham> arrSP;

    Spinner spLoaiSP;
    Spinner spSanPham;

    EditText etTenKhachHang, etSoDienThoai, etDiaChi, etSoLuong;
    Button btnPlus, btnMinus, btnPost;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tab_new_order, container, false);
        db = new DatabaseSanPham(getContext());
        init(v);
        return v;
    }

    public void init(View v) {
        preferences = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        spLoaiSP = (Spinner) v.findViewById(R.id.spLoaiSP);
        spSanPham = (Spinner) v.findViewById(R.id.spSanPham);

        etTenKhachHang = (EditText) v.findViewById(R.id.etTenKhachHang);
        etSoDienThoai = (EditText) v.findViewById(R.id.etSoDienThoai);
        etDiaChi = (EditText) v.findViewById(R.id.etDiaChi);
        etSoLuong = (EditText) v.findViewById(R.id.etSoLuong);

        btnMinus = (Button) v.findViewById(R.id.btnMinus);
        btnPlus = (Button) v.findViewById(R.id.btnPlus);
        btnPost = (Button) v.findViewById(R.id.btnPost);

        btnMinus.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnPost.setOnClickListener(this);

        spLoaiSP.setOnItemSelectedListener(this);
        spSanPham.setOnItemSelectedListener(this);
        setTenLoai();
        if (arrLoaiSanPham.size() > 0) {
            setTenSP(arrLoaiSanPham.get(0).getMaLoai());
        }

    }

    public boolean checkValid(String s) {
        if (s.length() == 0) {
            return false;
        }
        return true;
    }

    int posSP = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMinus:
                String s = etSoLuong.getText().toString().trim();
                if (checkValid(s)) {
                    int ss = Integer.parseInt(s);
                    if (ss > 1) {
                        ss -= 1;
                    }
                    etSoLuong.setText(String.valueOf(ss));
                    return;
                }
                break;
            case R.id.btnPlus:
                String sss = etSoLuong.getText().toString().trim();
                if (checkValid(sss)) {
                    int ss = Integer.parseInt(sss);
                    ss += 1;
                    etSoLuong.setText(String.valueOf(ss));
                    return;
                }
                break;
            case R.id.btnPost:
                String ten = etTenKhachHang.getText().toString().trim();
                String sdt = etSoDienThoai.getText().toString().trim();
                String address = etDiaChi.getText().toString().trim();
                String soLuong = etSoLuong.getText().toString();
                if (!checkValid(ten)) {
                    etTenKhachHang.setError("Tên khách hàng không được để trống!");
                    return;
                } else {
                    String manv = preferences.getString("maNhanVien", "");
                    alert(ten, sdt, address, arrSP.get(posSP).getMaSanPham(), Integer.parseInt(soLuong), arrSP.get(posSP).getTenSanPham(), arrSP.get(posSP).getGiaSanPham(), manv);
                }
                break;
        }
    }

    public void alert(final String ten, final String sdt, final String diaChi, final int maSP, final int soLuong, String tenSP, final int gia, final String manv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setNegativeButton("Xác Nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int tongtien = soLuong * gia;
                Ion.with(getContext())
                        .load("http://kentdzai.tk/aha/pushdonhang.php")
//                        .load("http://192.168.1.102/php/aha/pushdonhang.php")
                        .setBodyParameter("tenKH", ten)
                        .setBodyParameter("sdtKH", sdt)
                        .setBodyParameter("diaChiKH", diaChi)
                        .setBodyParameter("maNV", manv)
                        .setBodyParameter("maSP", String.valueOf(maSP))
                        .setBodyParameter("soLuong", String.valueOf(soLuong))
                        .setBodyParameter("tongTien", String.valueOf(tongtien))
                        .asString().setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (result.equals("true")) {
                            Toast.makeText(getContext(), "Thành công!" + result, Toast.LENGTH_SHORT).show();
                            etDiaChi.setText("");
                            etTenKhachHang.setText("");
                            etSoDienThoai.setText("");
                            etTenKhachHang.requestFocus();
                        } else if (result.equals("false")) {
                            Toast.makeText(getContext(), "Thất bại!" + result, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
        builder.setPositiveButton("Hủy", null);
        builder.setTitle("Bạn có muốn đăng đơn hàng?");
        builder.setMessage("Tên KH: " + ten + "\n"
                + "SĐT: " + sdt + "\n"
                + "Địa Chỉ: " + diaChi + "\n"
                + "SP: " + tenSP + "\n"
                + "Số Lượng: " + soLuong + "\n"
                + "Tổng tiền: " + gia * soLuong);
        builder.show();
    }

    public void setTenLoai() {
        arrLoaiSanPham = new ArrayList<>();
        arrLoaiSanPham = db.querryLoaiSanPham();
        ArrayList<String> arrTL = new ArrayList<>();
        for (int i = 0; i < arrLoaiSanPham.size(); i++) {
            arrTL.add(i, arrLoaiSanPham.get(i).getTenLoai());
        }
        spLoaiSP.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrTL));
    }

    public void setTenSP(int maLoai) {
        arrSP = new ArrayList<>();
        arrSP = db.querySanPham(maLoai);
        ArrayList<String> arrTSP = new ArrayList<>();
        for (int i = 0; i < arrSP.size(); i++) {
            arrTSP.add(i, arrSP.get(i).getTenSanPham());
        }
        spSanPham.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrTSP));
    }


    public void onButtonPressed(Uri uri) {
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spLoaiSP:
                setTenSP(arrLoaiSanPham.get(position).getMaLoai());
                break;
            case R.id.spSanPham:
                posSP = position;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
