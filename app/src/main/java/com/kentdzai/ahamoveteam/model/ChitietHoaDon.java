package com.kentdzai.ahamoveteam.model;

/**
 * Created by kentd on 23/11/2016.
 */

public class ChitietHoaDon {
    int maSanPham;
    String tenSanPham;
    String giaSanPham;
    int maLoai;
    int soLuong;

    public ChitietHoaDon(int maSanPham, String tenSanPham, String giaSanPham, int maLoai, int soLuong) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaSanPham = giaSanPham;
        this.maLoai = maLoai;
        this.soLuong = soLuong;
    }


    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getGiaSanPham() {
        return giaSanPham;
    }

    public void setGiaSanPham(String giaSanPham) {
        this.giaSanPham = giaSanPham;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public String toString() {
        return "ChitietHoaDon{" +
                "maSanPham=" + maSanPham +
                ", tenSanPham='" + tenSanPham + '\'' +
                ", giaSanPham='" + giaSanPham + '\'' +
                ", maLoai=" + maLoai +
                ", soLuong=" + soLuong +
                '}';
    }
}
