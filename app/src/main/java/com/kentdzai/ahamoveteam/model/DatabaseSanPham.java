package com.kentdzai.ahamoveteam.model;

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
    public final String TBL_LOAISANPHAM = "TBL_LOAISANPHAM";
    public final String MA_LOAI = "MA_LOAI";
    public final String TEN_LOAI = "TEN_LOAI";

    public final String TBL_SANPHAM = "TBL_SANPHAM";
    public final String MASANPHAM = "MASANPHAM";
    public final String TENSANPHAM = "TENSANPHAM";
    public final String GIASANPHAM = "GIASANPHAM";
    public final String MOTA = "MOTA";
    public final String FK_MA_LOAI = "FK_MA_LOAI";

    public DatabaseSanPham(Context context) {
        super(context, "dbSanPham2" + ".db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TBL_LOAISANPHAM(MA_LOAI INTEGER PRIMARY KEY, TEN_LOAI TEXT)");
        db.execSQL("CREATE TABLE TBL_SANPHAM(MASANPHAM INTEGER PRIMARY KEY, TENSANPHAM TEXT" +
                ", GIASANPHAM TEXT, MOTA TEXT, FK_MA_LOAI INTEGER)");
        infoDefault(db);
    }

    public void infoDefault(SQLiteDatabase db) {
        db.execSQL("INSERT INTO TBL_LOAISANPHAM VALUES(1, 'APPLE')");
        db.execSQL("INSERT INTO TBL_LOAISANPHAM VALUES(2, 'ANDROID')");
        db.execSQL("INSERT INTO TBL_SANPHAM VALUES(1, 'Iphone 7', '20.000.000đ', 'Điện Thoại Đẳng Cấp', 1)");
        db.execSQL("INSERT INTO TBL_SANPHAM VALUES(2, 'Samsung S7', '21.000.000đ', 'Điện Thoại Sang Trọng', 2)");
        db.execSQL("INSERT INTO TBL_SANPHAM VALUES(3, 'Samsung Galaxy Note 7', '18.200.000đ', 'Điện Thoại Thời Thượng', 2)");
        db.execSQL("INSERT INTO TBL_SANPHAM VALUES(4, 'OPPO F1S', '10.000.000đ', 'Điện Thoại Selfie', 2)");
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
            arrSP.add(new SanPham(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4)));
        }
        return arrSP;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
