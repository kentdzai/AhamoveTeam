package com.kentdzai.ahamoveteam;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kentdzai.ahamoveteam.model.ChitietHoaDon;

import java.util.ArrayList;

/**
 * Created by kentd on 18/11/2016.
 */

public class AdapterChiTiet extends ArrayAdapter<ChitietHoaDon> {
    Activity activity;
    int idLayout;
    ArrayList<ChitietHoaDon> arrC;


    public AdapterChiTiet(Activity activity, int idLayout, ArrayList<ChitietHoaDon> arrC) {
        super(activity, idLayout, arrC);
        this.activity = activity;
        this.idLayout = idLayout;
        this.arrC = arrC;
    }

    ChitietHoaDon c;

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolderChiTiet vh;
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(idLayout, null);
            vh = new ViewHolderChiTiet(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolderChiTiet) convertView.getTag();
        }


        c = arrC.get(position);
        vh.tvDetailTenSP.setText(c.getTenSanPham());
        vh.tvDetailGia.setText(c.getGiaSanPham());
        vh.tvDetailSoLuong.setText(String.valueOf(c.getSoLuong()));

        vh.ivDetailPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sl = Integer.parseInt(vh.tvDetailSoLuong.getText().toString());
                if (sl < 10) {
                    sl++;
                    vh.tvDetailSoLuong.setText(String.valueOf(sl));
                    setSoLuong(position, sl);
                }
            }
        });
        vh.ivDetailMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sl = Integer.parseInt(vh.tvDetailSoLuong.getText().toString());
                if (sl > 1) {
                    sl--;
                    vh.tvDetailSoLuong.setText(String.valueOf(sl));
                    setSoLuong(position, sl);
                }
            }
        });

        return convertView;
    }

    class ViewHolderChiTiet {
        TextView tvDetailTenSP, tvDetailGia, tvDetailSoLuong;
        ImageView ivDetailMinus, ivDetailPlus;

        public ViewHolderChiTiet(View v) {
            tvDetailTenSP = (TextView) v.findViewById(R.id.tvDetailTenSP);
            tvDetailGia = (TextView) v.findViewById(R.id.tvDetailGia);
            tvDetailSoLuong = (TextView) v.findViewById(R.id.tvSoLuong);
            ivDetailPlus = (ImageView) v.findViewById(R.id.ivDetailPlus);
            ivDetailMinus = (ImageView) v.findViewById(R.id.ivDetailMinus);
        }
    }

    public void setSoLuong(int pos, int sl) {
        arrC.get(pos).setSoLuong(sl);
    }

    public ArrayList<ChitietHoaDon> getChiTiet() {
        return arrC;
    }

}
