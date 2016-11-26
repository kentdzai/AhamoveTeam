package com.kentdzai.ahamoveteam.tab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kentdzai.ahamoveteam.AdapterDonHang;
import com.kentdzai.ahamoveteam.AdapterSanPham;
import com.kentdzai.ahamoveteam.R;
import com.kentdzai.ahamoveteam.model.DatabaseSanPham;
import com.kentdzai.ahamoveteam.model.HoaDon;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class TabOrderList extends Fragment {

    ListView lvOrderList;
    AdapterDonHang adapter;
    DatabaseSanPham db;

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
        init(v);
        return v;
    }

    private void init(View v) {
        db = new DatabaseSanPham(getContext());
        lvOrderList = (ListView) v.findViewById(R.id.lvOrderList);
        arrH = new ArrayList<>();
        showData();
        getData();
    }

    public void getData() {
        db.clearHD();
        Ion.with(getContext())
//                .load("http://192.168.1.102/php/aha/selectordeletehoadon.php")
                .load("http://192.168.15.103/php/aha/selectordeletehoadon.php")
//                .load("http://kentdzai.tk/aha/selectordeletehoadon.php")
                .setBodyParameter("keytask", "gethoadon")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (result.size() > 0) {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject j = result.get(i).getAsJsonObject();
                                arrH.add(new HoaDon(j.get("maHoaDon").getAsInt()
                                        , j.get("tenKhachHang").getAsString() + ""
                                        , j.get("soDienThoaiKH").getAsString() + ""
                                        , j.get("soLuong").getAsInt()
                                        , j.get("tongTien").getAsInt()
                                ));
                                adapter.notifyDataSetChanged();
                                ;
//                                db.insertHoaDon(new HoaDon(j.get("maHoaDon").getAsInt()
//                                        , j.get("tenKhachHang").getAsString() + ""
//                                        , j.get("soDienThoaiKH").getAsString() + ""
//                                        , j.get("soLuong").getAsInt()
//                                        , j.get("tongTien").getAsInt()
//                                ));
                            }
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
}
