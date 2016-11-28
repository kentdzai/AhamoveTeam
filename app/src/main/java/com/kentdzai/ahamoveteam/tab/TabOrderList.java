package com.kentdzai.ahamoveteam.tab;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kentdzai.ahamoveteam.AdapterDonHang;
import com.kentdzai.ahamoveteam.AdapterSanPham;
import com.kentdzai.ahamoveteam.ManagerActivity;
import com.kentdzai.ahamoveteam.MyLog;
import com.kentdzai.ahamoveteam.R;
import com.kentdzai.ahamoveteam.model.DatabaseSanPham;
import com.kentdzai.ahamoveteam.model.HoaDon;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class TabOrderList extends Fragment implements AdapterView.OnItemLongClickListener {

    ListView lvOrderList;
    AdapterDonHang adapter;
    DatabaseSanPham db;
    String server;

    public TabOrderList() {
    }

    public static TabOrderList newInstance(String param1, String param2) {
        TabOrderList fragment = new TabOrderList();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_order_list, container, false);
        server = getResources().getString(R.string.server);
        ((ManagerActivity) getActivity())
                .setActionBarTitle("Danh Sách Đơn Hàng");
        init(v);
        return v;
    }

    private void init(View v) {
        db = new DatabaseSanPham(getContext());
        lvOrderList = (ListView) v.findViewById(R.id.lvOrderList);
        arrH = new ArrayList<>();
        showData();
        getData();
        lvOrderList.setOnItemLongClickListener(this);
    }

    public void getData() {
        db.clearHD();
        Ion.with(getContext())
                .load(server + "selectordeletehoadon.php")
//                .load("http://kentdzai.tk/aha/selectordeletehoadon.php")
                .setBodyParameter("keytask", "gethoadon")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (result != null) {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject j = result.get(i).getAsJsonObject();
                                String tenSanPham = db.getTenSanPham(j.get("fk_maSanPham").getAsString());
                                arrH.add(new HoaDon(j.get("maHoaDon").getAsInt()
                                        , j.get("tenKhachHang").getAsString() + ""
                                        , j.get("soDienThoaiKH").getAsString() + ""
                                        , tenSanPham + ""
                                        , j.get("soLuong").getAsInt()
                                        , j.get("tongTien").getAsInt()
                                ));

                                ;
//                                db.insertHoaDon(new HoaDon(j.get("maHoaDon").getAsInt()
//                                        , j.get("tenKhachHang").getAsString() + ""
//                                        , j.get("soDienThoaiKH").getAsString() + ""
//                                        , j.get("soLuong").getAsInt()
//                                        , j.get("tongTien").getAsInt()
//                                ));
                            }
                            adapter.notifyDataSetChanged();
                            lvOrderList.invalidateViews();
                            lvOrderList.refreshDrawableState();
                        }

                    }
                });
    }

    ArrayList<HoaDon> arrH;

    public void showData() {
        arrH = new ArrayList<>();
        arrH = db.queryHoaDon();
        adapter = new AdapterDonHang(getActivity(), R.layout.adapter_donhang, arrH);
        lvOrderList.setAdapter(adapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Mã Hóa Đơn " + arrH.get(position).getMaHoaDon());
        builder.setMessage("Chọn tác vụ");
        builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Ion.with(getContext())
                        .load(server + "selectordeletehoadon.php")
                        .setBodyParameter("keytask", "deletehoadon")
                        .setBodyParameter("maHoaDon", arrH.get(position).getMaHoaDon() + "")
                        .asString().setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                        arrH.clear();
                        getData();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        builder.setPositiveButton("Hủy", null);
        builder.show();
        return false;
    }
}
