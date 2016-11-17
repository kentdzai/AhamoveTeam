package com.kentdzai.ahamoveteam.tab;

import android.content.DialogInterface;
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

import com.kentdzai.ahamoveteam.R;
import com.kentdzai.ahamoveteam.model.DatabaseSanPham;
import com.kentdzai.ahamoveteam.model.LoaiSanPham;
import com.kentdzai.ahamoveteam.model.SanPham;

import java.util.ArrayList;

public class TabNewOrder extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TabNewOrder() {
    }

    public static TabNewOrder newInstance(String param1, String param2) {
        TabNewOrder fragment = new TabNewOrder();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    DatabaseSanPham db;

    ArrayList<LoaiSanPham> arrLoaiSanPham;
    ArrayList<SanPham> arrSP;

    Spinner spLoaiSP;
    Spinner spSanPham;

    EditText etTenKhachHang, etSoDienThoai, etDiaChi, etSoLuong;
    Button btnPlus, btnMinus, btnPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tab_new_order, container, false);
        db = new DatabaseSanPham(getContext());
        init(v);
        return v;
    }

    public void init(View v) {
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
        setTenSP(arrLoaiSanPham.get(0).getMaLoai());
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
                    alert(ten, sdt, address, arrSP.get(posSP).getMaSanPham(), Integer.parseInt(soLuong), arrSP.get(posSP).getTenSanPham());
                }
                break;
        }
    }

    public void alert(String ten, String sdt, String diaChi, int maSP, int soLuong, String tenSP) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setNegativeButton("Xác Nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Hủy", null);
        builder.setTitle("Bạn có muốn đăng đơn hàng?");
        builder.setMessage("Tên KH: " + ten + "\n"
                + "SĐT: " + sdt + "\n"
                + "Địa Chỉ: " + diaChi + "\n"
                + "SP: " + tenSP + "\n"
                + "Số Lượng: " + soLuong);
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
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
