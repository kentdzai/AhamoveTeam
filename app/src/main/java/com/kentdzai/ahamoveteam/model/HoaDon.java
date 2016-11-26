package com.kentdzai.ahamoveteam.model;

/**
 * Created by kentd on 17/11/2016.
 */

public class HoaDon {
    int maHoaDon;
    String tenKhachHang;
    String sdtKhachHang;
    String tenMay;
    int soLuong;
    int tongTien;

    public HoaDon(int maHoaDon, String tenKhachHang, String sdtKhachHang, String tenMay, int soLuong, int tongTien) {
        this.maHoaDon = maHoaDon;
        this.tenKhachHang = tenKhachHang;
        this.sdtKhachHang = sdtKhachHang;
        this.tenMay = tenMay;
        this.soLuong = soLuong;
        this.tongTien = tongTien;
    }

    public String getTenMay() {
        return tenMay;
    }

    public void setTenMay(String tenMay) {
        this.tenMay = tenMay;
    }

    public HoaDon(int maHoaDon, String tenKhachHang, String sdtKhachHang, int soLuong, int tongTien) {
        this.maHoaDon = maHoaDon;
        this.tenKhachHang = tenKhachHang;
        this.sdtKhachHang = sdtKhachHang;
        this.soLuong = soLuong;
        this.tongTien = tongTien;


    }

    public String getSdtKhachHang() {
        return sdtKhachHang;
    }

    public void setSdtKhachHang(String sdtKhachHang) {
        this.sdtKhachHang = sdtKhachHang;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }
}
