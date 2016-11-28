package com.kentdzai.ahamoveteam.tab;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kentdzai.ahamoveteam.AdapterCustomers;
import com.kentdzai.ahamoveteam.EditCustomerActivity;
import com.kentdzai.ahamoveteam.ManagerActivity;
import com.kentdzai.ahamoveteam.MyLog;
import com.kentdzai.ahamoveteam.R;
import com.kentdzai.ahamoveteam.model.KhachHang;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class TabCustomers extends Fragment implements AdapterView.OnItemLongClickListener {

    public TabCustomers() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ListView lvCustomers;
    String server;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_customers, container, false);
        server = getResources().getString(R.string.server);
        ((ManagerActivity) getActivity())
                .setActionBarTitle("Danh Sách Khách Hàng");
        init(v);
        MyLog.e("create");
        return v;
    }

    private void init(View v) {
        lvCustomers = (ListView) v.findViewById(R.id.lvCustomers);
        lvCustomers.setOnItemLongClickListener(this);

        arrKH = new ArrayList<>();
        adapter = new AdapterCustomers(getActivity(), R.layout.adapter_customers, arrKH);
        lvCustomers.setAdapter(adapter);
        getKH();
    }

    ArrayList<KhachHang> arrKH;
    AdapterCustomers adapter;

    public void getKH() {

        Ion.with(getContext())
                .load(server + "getkhachhang.php")
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
                                }
                                adapter.notifyDataSetChanged();
                                lvCustomers.invalidateViews();
                                lvCustomers.refreshDrawableState();
                            }
                        }

                    }
                });
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
        builder.setTitle("Sửa khách hàng");
        builder.setMessage("Bạn có muốn sửa khách hàng?");
        builder.setPositiveButton("Hủy", null);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent it = new Intent(getActivity(), EditCustomerActivity.class);
                it.putExtra("customer", arrKH.get(position));
                startActivity(it);
            }
        });
        builder.show();
        return false;
    }
}
