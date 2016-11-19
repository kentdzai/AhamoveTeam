package com.kentdzai.ahamoveteam.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kentdzai.ahamoveteam.MyLog;

import java.util.ArrayList;

/**
 * Created by kentd on 18/11/2016.
 */

public class DatabaseSanPham extends SQLiteOpenHelper {
    public SQLiteDatabase db;
    ContentValues values;

    public final String TBL_LOAISANPHAM = "TBL_LOAISANPHAM";
    public final String MA_LOAI = "MA_LOAI";
    public final String TEN_LOAI = "TEN_LOAI";

    public final String TBL_SANPHAM = "TBL_SANPHAM";
    public final String MASANPHAM = "MASANPHAM";
    public final String TENSANPHAM = "TENSANPHAM";
    public final String GIASANPHAM = "GIASANPHAM";
    public final String MOTA = "MOTA";
    public final String FK_MA_LOAI = "FK_MA_LOAI";

    public final String TBL_HOADON = "TBL_HOADON";
    public final String MAHD = "MAHD";
    public final String TENKH = "TENKH";
    public final String SDTKH = "SDTKH";
    public final String SOLUONGMAY = "SOLUONGMAY";
    public final String TONGTIEN = "TONGTIEN";


    public DatabaseSanPham(Context context) {
        super(context, "dbSanPham8" + ".db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TBL_LOAISANPHAM(MA_LOAI INTEGER PRIMARY KEY, TEN_LOAI TEXT)");
        db.execSQL("CREATE TABLE TBL_SANPHAM(MASANPHAM INTEGER PRIMARY KEY, TENSANPHAM TEXT" +
                ", GIASANPHAM INTEGER, FK_MA_LOAI INTEGER, MOTA TEXT)");
        db.execSQL("CREATE TABLE TBL_HOADON(MAHD INTEGER PRIMARY KEY, TENKH TEX, SDTKH TEXT, SOLUONGMAY INTEGER, TONGTIEN INTEGER)");
        infoDefault(db);
    }

    public void clearDbToUpdate() {
        db = getWritableDatabase();
        db.execSQL("delete from " + TBL_LOAISANPHAM);
        db.execSQL("delete from " + TBL_SANPHAM);
    }
    public void clearHD() {
        db = getWritableDatabase();
        db.execSQL("delete from " + TBL_HOADON);
    }


    public void infoDefault(SQLiteDatabase db) {
        db.execSQL("INSERT INTO TBL_LOAISANPHAM VALUES(1, 'APPLE')");
        db.execSQL("INSERT INTO TBL_LOAISANPHAM VALUES(2, 'ANDROID')");
        db.execSQL("INSERT INTO TBL_SANPHAM VALUES(1, 'Iphone 7', '20000000',1 ,'Điện Thoại Đẳng Cấp')");
        db.execSQL("INSERT INTO TBL_SANPHAM VALUES(2, 'Samsung S7', '21000000',2 , 'Điện Thoại Sang Trọng')");
        db.execSQL("INSERT INTO TBL_SANPHAM VALUES(3, 'Samsung Galaxy Note 7',2 , '18200000', 'Điện Thoại Thời Thượng')");
        db.execSQL("INSERT INTO TBL_SANPHAM VALUES(4, 'OPPO F1S', '10000000',2 , 'Điện Thoại Selfie')");
    }

    public boolean insertLoaiSanPham(int maLoai, String tenLoai) {
        db = getWritableDatabase();
        values = new ContentValues();
        values.put(MA_LOAI, maLoai);
        values.put(TEN_LOAI, tenLoai);
        long result = db.insert(TBL_LOAISANPHAM, null, values);
        if (result != -1) {
            return true;
        }
        return false;
    }

    public boolean insertSanPham(int maSanPham, String tenSanPham, int giaSanPham, int fk_MaLoai, String moTa) {
        db = getWritableDatabase();
        values = new ContentValues();
        values.put(MASANPHAM, maSanPham);
        values.put(TENSANPHAM, tenSanPham);
        values.put(GIASANPHAM, giaSanPham);
        values.put(FK_MA_LOAI, fk_MaLoai);
        values.put(MOTA, moTa);
        long result = db.insert(TBL_SANPHAM, null, values);
        if (result != -1) {
            return true;
        }
        return false;
    }

    public boolean insertHoaDon(HoaDon h) {
        db = getWritableDatabase();
        values = new ContentValues();
        values.put(MAHD, h.getMaHoaDon());
        values.put(TENKH, h.getTenKhachHang());
        values.put(SDTKH, h.getSdtKhachHang());
        values.put(SOLUONGMAY, h.getSoLuong());
        values.put(TONGTIEN, h.getTongTien());
        long result = db.insert(TBL_SANPHAM, null, values);
        if (result != -1) {
            return true;
        }
        return false;
    }


    public ArrayList<LoaiSanPham> querryLoaiSanPham() {
        db = getReadableDatabase();
        ArrayList<LoaiSanPham> arrL = new ArrayList<>();
        String query = "SELECT * FROM " + TBL_LOAISANPHAM;
        Cursor c = db.query(TBL_LOAISANPHAM, null, null, null, null, null, null);
        while (c.moveToNext()) {
            arrL.add(new LoaiSanPham(c.getInt(c.getColumnIndex(MA_LOAI)), c.getString(c.getColumnIndex(TEN_LOAI))));
        }
        return arrL;
    }


    public ArrayList<SanPham> querySanPham(int maLoai) {
        db = getReadableDatabase();
        ArrayList<SanPham> arrSP = new ArrayList<>();
        Cursor c = db.query(TBL_SANPHAM, null, FK_MA_LOAI + " = ?", new String[]{String.valueOf(maLoai)}, null, null, null);
        while (c.moveToNext()) {
            arrSP.add(new SanPham(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getString(4)));
        }
        return arrSP;
    }

    public ArrayList<HoaDon> queryHoaDon() {
        db = getReadableDatabase();
        ArrayList<HoaDon> arrH = new ArrayList<>();
        Cursor c = db.query(TBL_HOADON, null, null, null, null, null, null);
        while (c.moveToNext()) {
            arrH.add(new HoaDon(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4)));
        }
        return arrH;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
