package com.kentdzai.ahamoveteam.tab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kentdzai.ahamoveteam.AdapterSanPham;
import com.kentdzai.ahamoveteam.MyLog;
import com.kentdzai.ahamoveteam.R;
import com.kentdzai.ahamoveteam.model.DatabaseSanPham;
import com.kentdzai.ahamoveteam.model.LoaiSanPham;
import com.kentdzai.ahamoveteam.model.SanPham;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class TabProductList extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public TabProductList() {
    }

    public static TabProductList newInstance(String param1, String param2) {
        TabProductList fragment = new TabProductList();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    DatabaseSanPham db;
    Button btnUpdateProduct;
    Spinner spGetListProduct;
    ListView lvProduct;
    AdapterSanPham adapter;
    ArrayList<LoaiSanPham> arrLoaiSanPham;
    ArrayList<SanPham> arrSanPham;
    String server;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_product_list, container, false);
        server = getResources().getString(R.string.server);
        init(v);
        return v;
    }

    private void init(View v) {
        db = new DatabaseSanPham(getContext());
        btnUpdateProduct = (Button) v.findViewById(R.id.btnUpdateProduct);
        spGetListProduct = (Spinner) v.findViewById(R.id.spGetListProduct);
        lvProduct = (ListView) v.findViewById(R.id.lvProduct);
        btnUpdateProduct.setOnClickListener(this);
        spGetListProduct.setOnItemSelectedListener(this);
//        getData();
        arrLoaiSanPham = new ArrayList<>();
        arrSanPham = new ArrayList<>();
        setTenLoai();
        setSanPham(1, false);
    }

    public void setTenLoai() {
        arrLoaiSanPham = new ArrayList<>();
        arrLoaiSanPham = db.querryLoaiSanPham();
        ArrayList<String> arrTL = new ArrayList<>();
        for (int i = 0; i < arrLoaiSanPham.size(); i++) {
            arrTL.add(i, arrLoaiSanPham.get(i).getTenLoai());
        }
        spGetListProduct.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrTL));
    }


    public void getData() {
        db.clearDbToUpdate();
        Ion.with(getContext()).load(server + "getmaloai.php")
                .setBodyParameter("anhdeptrai", "kentdzai")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        for (int i = 0; i < result.size(); i++) {
                            JsonObject jj = result.get(i).getAsJsonObject();
                            db.insertLoaiSanPham(jj.get("maLoai").getAsInt(), jj.get("tenLoai").getAsString());
                            Ion.with(getContext()).load(server + "getsanpham.php")
                                    .setBodyParameter("anhdeptrai", "kentdzai")
                                    .asJsonArray()
                                    .setCallback(new FutureCallback<JsonArray>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonArray result) {
                                            for (int i = 0; i < result.size(); i++) {
                                                JsonObject jj = result.get(i).getAsJsonObject();
                                                db.insertSanPham(jj.get("maSanPham").getAsInt(),
                                                        jj.get("tenSanPham").getAsString(),
                                                        jj.get("giaSanPham").getAsInt(),
                                                        jj.get("fk_maLoai").getAsInt(),
                                                        jj.get("moTa").getAsString());
                                                setTenLoai();
                                                setSanPham(spGetListProduct.getChildCount(), true);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    public void getData2() {
        db.clearDbToUpdate();
        Ion.with(getContext())
//                .load("http://kentdzai.tk/aha/getmaloaiandsanpham.php")
                .load(server + "getmaloaiandsanpham.php")
                .setBodyParameter("anhdeptrai", "kentdzai")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray arr) {
                        JsonArray arrLoai = arr.get(0).getAsJsonArray();
                        for (int i = 0; i < arrLoai.size(); i++) {
                            JsonObject jLoai = arrLoai.get(i).getAsJsonObject();
                            db.insertLoaiSanPham(jLoai.get("maLoai").getAsInt(), jLoai.get("tenLoai").getAsString());
                        }
                        JsonArray arrSP = arr.get(1).getAsJsonArray();
                        for (int i = 0; i < arrSP.size(); i++) {
                            JsonObject jSP = arrSP.get(i).getAsJsonObject();
                            db.insertSanPham(jSP.get("maSanPham").getAsInt(),
                                    jSP.get("tenSanPham").getAsString(),
                                    jSP.get("giaSanPham").getAsInt(),
                                    jSP.get("fk_maLoai").getAsInt(),
                                    jSP.get("moTa").getAsString());
                            setTenLoai();
                            setSanPham(spGetListProduct.getChildCount(), true);
                        }
                    }
                });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setSanPham(int maLoai, boolean update) {
        arrSanPham.clear();
        ArrayList<SanPham> list1 = db.querySanPham(maLoai);
        arrSanPham.addAll(list1);
        if (update) {

            adapter.notifyDataSetChanged();
            lvProduct.invalidateViews();
            lvProduct.refreshDrawableState();

        } else {
            adapter = new AdapterSanPham(getActivity(), R.layout.adapter_sanpham, arrSanPham);
            lvProduct.setAdapter(adapter);
        }

    }

    int posLSP = 0;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spGetListProduct:
                posLSP = position;
                setSanPham(arrLoaiSanPham.get(position).getMaLoai(), true);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateProduct:
                getData2();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
