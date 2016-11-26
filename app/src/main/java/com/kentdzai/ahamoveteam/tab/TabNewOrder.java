package com.kentdzai.ahamoveteam.tab;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kentdzai.ahamoveteam.AdapterChiTiet;
import com.kentdzai.ahamoveteam.CheckNetwork;
import com.kentdzai.ahamoveteam.MyLog;
import com.kentdzai.ahamoveteam.R;
import com.kentdzai.ahamoveteam.model.ChitietHoaDon;
import com.kentdzai.ahamoveteam.model.DatabaseSanPham;
import com.kentdzai.ahamoveteam.model.KhachHang;
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

    EditText etTenKhachHang, etDiaChi;
    Button btnAdd;
    SharedPreferences preferences;

    ArrayList<KhachHang> arrKH;
    AutoCompleteTextView acPhone;
    AutoCompleteTextView acSanPham;
    ArrayList<String> arrSDT;
    ListView lvDetail;
    FloatingActionButton fab_sent;
    AdapterChiTiet adapterC;
    ArrayList<ChitietHoaDon> arrC;
    ArrayAdapter<String> adapterSDT;
    ArrayList<String> arrNameSP;

    int positionSanPham = 0;
    ArrayAdapter<String> adapterTenSanPham;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tab_new_order, container, false);
        db = new DatabaseSanPham(getContext());
        init(v);
        return v;
    }


    public void init(View v) {
        arrC = new ArrayList<>();
        arrKH = new ArrayList<>();
        arrSDT = new ArrayList<>();
        arrSP = new ArrayList<>();
        fab_sent = (FloatingActionButton) v.findViewById(R.id.fab_sent);
        acSanPham = (AutoCompleteTextView) v.findViewById(R.id.acSanPham);
        lvDetail = (ListView) v.findViewById(R.id.lvDetail);
        preferences = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        spLoaiSP = (Spinner) v.findViewById(R.id.spLoaiSP);
        spSanPham = (Spinner) v.findViewById(R.id.spSanPham);
        acPhone = (AutoCompleteTextView) v.findViewById(R.id.acPhone);
        etTenKhachHang = (EditText) v.findViewById(R.id.etTenKhachHang);
        etDiaChi = (EditText) v.findViewById(R.id.etDiaChi);

        btnAdd = (Button) v.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(this);
        fab_sent.setOnClickListener(this);

        spLoaiSP.setOnItemSelectedListener(this);
        spSanPham.setOnItemSelectedListener(this);

        adapterC = new AdapterChiTiet(getActivity(), R.layout.adapter_detail_order, arrC);
        lvDetail.setAdapter(adapterC);

        setTenLoai();
        if (arrLoaiSanPham.size() > 0) {
            setTenSP(arrLoaiSanPham.get(0).getMaLoai());
        }
        if (CheckNetwork.network) {
            getKH();
        }
        getSanPham();

    }

    public void getSanPham() {
        arrSP = db.queryAllSanPham();
        arrNameSP = new ArrayList<>();
        for (int i = 0; i < arrSP.size(); i++) {
            arrNameSP.add(arrSP.get(i).getTenSanPham());
            MyLog.e(arrSP.get(i).toString());
        }
        adapterTenSanPham = new ArrayAdapter<String>(getContext()
                , android.R.layout.simple_list_item_1
                , arrNameSP);
        acSanPham.setAdapter(adapterTenSanPham);
        acSanPham.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    acSanPham.showDropDown();
            }
        });
        acSanPham.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                acSanPham.showDropDown();
                return false;
            }
        });
    }

    public void getKH() {
        Ion.with(getContext())
                .load("http://192.168.1.102/php/aha/getkhachhang.php")
                .setBodyParameter("anhdeptrai", "kentdzai")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray r) {
                        if (r != null) {
                            if (r.size() > 0) {
                                for (int i = 0; i < r.size(); i++) {
                                    JsonObject jop = r.get(i).getAsJsonObject();
                                    arrKH.add(new KhachHang(jop.get("maKhachHang").getAsInt()
                                            , jop.get("tenKhachHang").getAsString()
                                            , jop.get("soDienThoaiKH").getAsString()
                                            , jop.get("diaChiKH").getAsString()));
                                    arrSDT.add(jop.get("soDienThoaiKH").getAsString());
                                }
                                adapterSDT = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrSDT);
                                acPhone.setAdapter(adapterSDT);
                            }
                        }

                    }
                });
        acPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = acPhone.getText().toString();
                for (int i = 0; i < arrKH.size(); i++) {
                    KhachHang kh = arrKH.get(i);
                    if (kh.getPhone().equals(s)) {
                        etDiaChi.setText(kh.getAddress());
                        etTenKhachHang.setText(kh.getName());
                    }
                }

            }
        });
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
            case R.id.btnAdd:
                String ten = etTenKhachHang.getText().toString().trim();
                String sdt = acPhone.getText().toString().trim();
                String address = etDiaChi.getText().toString().trim();
                String tensp = acSanPham.getText().toString().trim();
                if (!checkValid(sdt)) {
                    acPhone.setError("Số điện thoại không được để trống");
                    acPhone.requestFocus();
                    return;
                }
                if (!checkValid(ten)) {
                    etTenKhachHang.setError("Tên không được để trống");
                    etTenKhachHang.requestFocus();
                    return;
                }
                if (!checkValid(address)) {
                    etDiaChi.setError("Địa chỉ không được để trống");
                    etDiaChi.requestFocus();
                    return;
                }
                if (!checkValid(tensp)) {
                    acSanPham.setError("Tên sản phẩm không được để trống");
                    acSanPham.requestFocus();
                    return;
                }
                SanPham sp = checkTenSanPham(tensp);
                if (sp == null) {
                    acSanPham.setText("");
                    acSanPham.setError("Tên sản phẩm này không có trong danh sách");
                    acSanPham.requestFocus();
                    return;
                }
                arrC.add(new ChitietHoaDon(sp.getMaSanPham(),
                        sp.getTenSanPham(),
                        String.valueOf(sp.getGiaSanPham()),
                        sp.getMaLoai(),
                        1));
                adapterC.notifyDataSetChanged();
                lvDetail.invalidateViews();
                lvDetail.refreshDrawableState();
                if (arrSP.size() > 0) {
                    arrSP.remove(positionSanPham);
                    arrNameSP.remove(positionSanPham);
                    adapterTenSanPham.remove(tensp);
                }

                MyLog.e("" + arrSP.size() + " - " + arrNameSP.size());

                acSanPham.setText("");


                break;
            case R.id.fab_sent:
                final ArrayList<ChitietHoaDon> arCclone = adapterC.getChiTiet();
                final String tenc = etTenKhachHang.getText().toString().trim();
                final String sdtc = acPhone.getText().toString().trim();
                final String addressc = etDiaChi.getText().toString().trim();
                if (!checkValid(sdtc)) {
                    acPhone.setError("Số điện thoại không được để trống");
                    acPhone.requestFocus();
                    return;
                }
                if (!checkValid(tenc)) {
                    etTenKhachHang.setError("Tên không được để trống");
                    etTenKhachHang.requestFocus();
                    return;
                }
                if (!checkValid(addressc)) {
                    etDiaChi.setError("Địa chỉ không được để trống");
                    etDiaChi.requestFocus();
                    return;
                }
                if (arCclone.size() == 0) {
                    acSanPham.setError("Phải có tối thiểu 1 sản phẩm");
                    acSanPham.requestFocus();
                    return;
                }
                final String manv = preferences.getString("maNhanVien", "");
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("Bạn có thực sự muốn thêm hóa đơn này?");
                alert.setPositiveButton("Hủy", null);
                alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Ion.with(getContext())
                                .load("http://192.168.1.102/php/aha/neworder.php")
                                .setBodyParameter("tenKH", tenc)
                                .setBodyParameter("sdtKH", sdtc)
                                .setBodyParameter("diaChiKH", addressc)
                                .setBodyParameter("maNV", manv)
                                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                if (result != null) {
                                    final String maHD = result.get("maHoaDon").getAsString();
                                    MyLog.e("maHD: " + maHD);
                                    for (int i = 0; i < arCclone.size(); i++) {
                                        ChitietHoaDon c = arCclone.get(i);
                                        final int maSP = c.getMaSanPham();
                                        final int soLuong = c.getSoLuong();
                                        final int tongTien = soLuong * Integer.parseInt(c.getGiaSanPham());


                                        Ion.with(getContext())
                                                .load("http://192.168.1.102/php/aha/detailorder.php")
                                                .setBodyParameter("maHD", maHD)
                                                .setBodyParameter("maSP", String.valueOf(maSP))
                                                .setBodyParameter("soLuong", String.valueOf(soLuong))
                                                .setBodyParameter("tongTien", String.valueOf(tongTien))
                                                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                                            @Override
                                            public void onCompleted(Exception e, JsonObject result) {
                                                if (result != null) {
                                                    String s = result.get("insert").getAsString();

                                                    etDiaChi.setText("");
                                                    etTenKhachHang.setText("");
                                                    acSanPham.setText("");
                                                    acPhone.setText("");
                                                    arrC.clear();
                                                    adapterC.notifyDataSetChanged();
                                                    lvDetail.invalidateViews();
                                                    lvDetail.refreshDrawableState();
                                                    if (s.trim().equals("true")) {
                                                        Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_LONG).show();
                                                        getKH();
                                                    } else {
                                                        Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                });
                alert.show();
                break;
        }
    }

    public SanPham checkTenSanPham(String tenSanPham) {
        for (int i = 0; i < arrSP.size(); i++) {
            if (tenSanPham.equals(arrSP.get(i).getTenSanPham())) {
                return arrSP.get(i);
            }
        }
        return null;
    }

    public void alert(final String ten, final String sdt, final String diaChi, final int maSP, final int soLuong, String tenSP, final int gia, final String manv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setNegativeButton("Xác Nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int tongtien = soLuong * gia;
                Ion.with(getContext())
//                        .load("http://kentdzai.tk/aha/pushdonhang.php")
                        .load("http://192.168.1.102/php/aha/pushdonhang.php")
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
