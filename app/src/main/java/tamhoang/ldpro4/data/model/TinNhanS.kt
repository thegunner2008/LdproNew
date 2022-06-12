package tamhoang.ldpro4.data.model

import android.content.ContentValues
import android.database.Cursor

data class TinNhanS (
    var ID: Int?,
    var ngay_nhan: String,
    var gio_nhan: String,
    var type_kh: Int,
    var ten_kh: String,
    var so_dienthoai: String,
    var use_app: String,
    var so_tin_nhan: Int,
    var nd_goc: String,
    var nd_sua: String?,
    var nd_phantich: String,
    var phat_hien_loi: String,
    var tinh_tien: Int,
    var ok_tn: Int,
    var del_sms: Int,
    var phan_tich: String?) {
    companion object {
        const val TABLE_NAME = "tbl_tinnhanS"

        const val NGAY_NHAN = "ngay_nhan"
        const val GIO_NHAN = "gio_nhan"
        const val TYPE_KH = "type_kh"
        const val TEN_KH = "ten_kh"
        const val SO_DIENTHOAI = "so_dienthoai"
        const val USE_APP = "use_app"
        const val SO_TIN_NHAN = "so_tin_nhan"
        const val ND_GOC = "nd_goc"
        const val ND_SUA = "nd_sua"
        const val ND_PHANTICH = "nd_phantich"
        const val PHAT_HIEN_LOI = "phat_hien_loi"
        const val TINH_TIEN = "tinh_tien"
        const val OK_TN = "ok_tn"
        const val DEL_SMS = "del_sms"
        const val PHAN_TICH = "phan_tich"

        fun parseCursor(cursor: Cursor) = TinNhanS(
            ID = cursor.getInt(0),
            ngay_nhan= cursor.getString(1),
            gio_nhan = cursor.getString(2),
            type_kh = cursor.getInt(3),
            ten_kh = cursor.getString(4),
            so_dienthoai = cursor.getString(5),
            use_app = cursor.getString(6),
            so_tin_nhan= cursor.getInt(7),
            nd_goc = cursor.getString(8),
            nd_sua = cursor.getString(9),
            nd_phantich = cursor.getString(10),
            phat_hien_loi = cursor.getString(11),
            tinh_tien = cursor.getInt(12),
            ok_tn = cursor.getInt(13),
            del_sms = cursor.getInt(14),
            phan_tich = cursor.getString(15))

        fun toContentValues(tinNhanS: TinNhanS): ContentValues {
            val values = ContentValues()
            tinNhanS.apply {
                values.put("ID", ID)
                values.put(NGAY_NHAN, ngay_nhan)
                values.put(GIO_NHAN, gio_nhan)
                values.put(TYPE_KH, type_kh)
                values.put(TEN_KH, ten_kh)
                values.put(SO_DIENTHOAI, so_dienthoai)
                values.put(USE_APP, use_app)
                values.put(SO_TIN_NHAN, so_tin_nhan)
                values.put(ND_GOC, nd_goc)
                values.put(ND_SUA, nd_sua)
                values.put(ND_PHANTICH, nd_phantich)
                values.put(PHAT_HIEN_LOI, phat_hien_loi)
                values.put(TINH_TIEN, tinh_tien)
                values.put(OK_TN, ok_tn)
                values.put(DEL_SMS, del_sms)
                values.put(PHAN_TICH, phan_tich)
            }
            return values
        }
    }

}
