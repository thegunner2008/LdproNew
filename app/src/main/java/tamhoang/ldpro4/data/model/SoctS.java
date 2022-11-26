package tamhoang.ldpro4.data.model;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;

public class SoctS {
    public Integer id;
    public String ngay_nhan;
    public int type_kh;
    public String ten_kh;
    public String so_dienthoai;
    public int so_tin_nhan;
    public String the_loai;
    public String so_chon;
    public double diem;
    public double diem_quydoi;
    public double diem_khachgiu;
    public double diem_dly_giu;
    public double diem_ton;
    public double gia;
    public double lan_an;
    public double so_nhay;
    public double tong_tien;
    public double ket_qua;

    public SoctS(Integer id, String ngay_nhan, int type_kh, String ten_kh, String so_dienthoai, int so_tin_nhan, String the_loai,
                 String so_chon, double diem, double diem_quydoi, double diem_khachgiu, double diem_dly_giu, double diem_ton,
                 double gia, double lan_an, double so_nhay, double tong_tien, double ket_qua) {
        this.id = id;
        this.ngay_nhan = ngay_nhan;
        this.type_kh = type_kh;
        this.ten_kh = ten_kh;
        this.so_dienthoai = so_dienthoai;
        this.so_tin_nhan = so_tin_nhan;
        this.the_loai = the_loai;
        this.so_chon = so_chon;
        this.diem = diem;
        this.diem_quydoi = diem_quydoi;
        this.diem_khachgiu = diem_khachgiu;
        this.diem_dly_giu = diem_dly_giu;
        this.diem_ton = diem_ton;
        this.gia = gia;
        this.lan_an = lan_an;
        this.so_nhay = so_nhay;
        this.tong_tien = tong_tien;
        this.ket_qua = ket_qua;
    }

    static public String TABLE_NAME = "tbl_soctS";

    static public ContentValues toContentValues(SoctS soctS) {
        ContentValues values = new ContentValues();
        values.put("ID", soctS.id);
        values.put("ngay_nhan", soctS.ngay_nhan);
        values.put("type_kh", soctS.type_kh);
        values.put("ten_kh", soctS.ten_kh);
        values.put("so_dienthoai", soctS.so_dienthoai);
        values.put("so_tin_nhan", soctS.so_tin_nhan);
        values.put("the_loai", soctS.the_loai);
        values.put("so_chon", soctS.so_chon);
        values.put("diem", soctS.diem);
        values.put("diem_quydoi", soctS.diem_quydoi);
        values.put("diem_khachgiu", soctS.diem_khachgiu);
        values.put("diem_dly_giu", soctS.diem_dly_giu);
        values.put("diem_ton", soctS.diem_ton);
        values.put("gia", soctS.gia);
        values.put("lan_an", soctS.lan_an);
        values.put("so_nhay", soctS.so_nhay);
        values.put("tong_tien", soctS.tong_tien);
        values.put("ket_qua", soctS.ket_qua);
        return values;
    }

    static public SoctS parseCursor(Cursor cursor) {
        return new SoctS(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getInt(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getDouble(8),
                cursor.getDouble(9),
                cursor.getDouble(10),
                cursor.getDouble(11),
                cursor.getDouble(12),
                cursor.getDouble(13),
                cursor.getDouble(14),
                cursor.getDouble(15),
                cursor.getDouble(16),
                cursor.getDouble(17)
        );
    }
}
