package tamhoang.ldpro4.data

import android.content.ContentValues.TAG
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.squareup.sqlbrite2.BriteDatabase
import com.squareup.sqlbrite2.SqlBrite
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import tamhoang.ldpro4.data.model.Chat
import tamhoang.ldpro4.data.model.ChuyenThang
import tamhoang.ldpro4.data.model.KhachHang
import tamhoang.ldpro4.data.model.TinNhanS

object BriteDb {
    lateinit var sqlBrite: SqlBrite
    lateinit var db : BriteDatabase

    fun init(context: Context){
        sqlBrite = SqlBrite.Builder().build()
        db = sqlBrite.wrapDatabaseHelper(Database(context), Schedulers.io());
    }

    private fun getCursorField(table: String, field: String, where: String): Cursor {
        val query = "SELECT $field FROM $table WHERE $where"
        return db.query(query)
    }

    fun getStringField(table: String, field: String, where: String): String? {
        getCursorField(table, field, where).use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getString(0)
            }
        }
        return ""
    }

    fun getIntField(table: String, field: String, where: String): Int {
        getCursorField(table, field, where).use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getInt(0)
            }
        }
        return -1
    }

    fun getDoubleField(table: String, field: String, where: String): Double {
        getCursorField(table, field, where).use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getDouble(0)
            }
        }
        return -1.0
    }

    fun selectKhachHangQuery(query: String): KhachHang?{
        val query1 = "Select * From ${KhachHang.TABLE_NAME} $query"

        val cursor = db.query(query1)
        return if (cursor != null && cursor.moveToFirst())
            KhachHang.parseCursor(cursor) else null
    }

    fun selectKhachHang(Ten_kh: String): KhachHang?{
        val query = "Select * From ${KhachHang.TABLE_NAME} Where ${KhachHang.TEN_KH} = '$Ten_kh'"

        val cursor = db.query(query)
        return if (cursor != null && cursor.moveToFirst())
            KhachHang.parseCursor(cursor) else null
    }

    fun selectListKhachHang(query: String): List<KhachHang>{
        val queryRaw = "Select * From ${KhachHang.TABLE_NAME} $query"

        val list = mutableListOf<KhachHang>()
        val cursor: Cursor = db.query(queryRaw)
        while (cursor.moveToNext()) {
            list.add(KhachHang.parseCursor(cursor))
        }
        cursor.close()
        return list
    }

    fun getMaxSoTinNhan(ngay_nhan: String, type_kh: Int?, query: String): Int {
        val queryRaw = "Select max(${TinNhanS.SO_TIN_NHAN}) from ${TinNhanS.TABLE_NAME} WHERE ngay_nhan = '$ngay_nhan' AND " +
                (if(type_kh != null) "type_kh = $type_kh AND " else "") + query
        val cursor = db.query(queryRaw)
        return if (cursor.moveToFirst() && cursor != null )
            cursor.getInt(0)
        else 0
    }

    fun selectTinNhanS(ID: Int): TinNhanS?{
        val query = "Select * From ${TinNhanS.TABLE_NAME} Where ID = $ID"

        val cursor = db.query(query)
        return if (cursor != null && cursor.moveToFirst())
            TinNhanS.parseCursor(cursor) else null
    }

    fun selectTinNhanS(Ngay_nhan: String, Ten_kh: String, So_tin_nhan: Int, Type_kh: Int): TinNhanS?{
        val query = "Select * From ${TinNhanS.TABLE_NAME} Where" +
                " ${TinNhanS.NGAY_NHAN} = '$Ngay_nhan'" +
                " AND ${TinNhanS.TEN_KH} = '$Ten_kh'" +
                " AND ${TinNhanS.SO_TIN_NHAN} = $So_tin_nhan" +
                " AND ${TinNhanS.TYPE_KH} = $Type_kh"

        val cursor = db.query(query)
        return if (cursor != null && cursor.moveToFirst())
            TinNhanS.parseCursor(cursor) else null
    }

    fun selectTinNhanS(query: String): TinNhanS?{
        val queryRaw = "Select * From ${TinNhanS.TABLE_NAME} Where $query"

        val cursor = db.query(queryRaw)
        return if (cursor != null && cursor.moveToFirst())
            TinNhanS.parseCursor(cursor) else null
    }

    fun selectListTinNhanS(query: String): List<TinNhanS>{
        val queryRaw = "Select * From ${TinNhanS.TABLE_NAME} $query"

        val list = mutableListOf<TinNhanS>()
        val cursor: Cursor = db.query(queryRaw)
        while (cursor.moveToNext()) {
            list.add(TinNhanS.parseCursor(cursor))
        }
        cursor.close()
        return list
    }

    fun insertTinNhanS(tinNhanS: TinNhanS) {
        val transaction = db.newTransaction()
        try {
            db.insert(TinNhanS.TABLE_NAME, TinNhanS.toContentValues(tinNhanS), SQLiteDatabase.CONFLICT_REPLACE)
            transaction.markSuccessful()
        } catch (e: Exception) {
            Log.e(TAG, "Error $e")
        } finally {
            transaction.end()
        }
    }

    fun insertListTinNhanS(listTinNhanS: List<TinNhanS>){
        val transaction = db.newTransaction()
        try {
            listTinNhanS.forEach {
                db.insert(TinNhanS.TABLE_NAME,
                    TinNhanS.toContentValues(it),
                    SQLiteDatabase.CONFLICT_REPLACE)
            }
            transaction.markSuccessful()
        } catch (e: Exception) {
            Log.e(TAG, "Error $e")
        } finally {
            transaction.end()
        }
    }

    fun selectChatsQuery(query: String): List<Chat>{
        val fullQuery = "Select * From ${Chat.TABLE_NAME} $query"

        val list = mutableListOf<Chat>()
        val cursor: Cursor = db.query(fullQuery)
        while (cursor.moveToNext()) {
            list.add(Chat.parseCursor(cursor))
        }
        return list
    }

    fun selectChats(mDate: String): List<Chat>{
        val query = "Select * From ${Chat.TABLE_NAME} WHERE ngay_nhan = '" + mDate + "' ORDER BY Gio_nhan DESC, ID DESC"

        val list = mutableListOf<Chat>()
        val cursor: Cursor = db.query(query)
        while (cursor.moveToNext()) {
            list.add(Chat.parseCursor(cursor))
        }
        return list
    }

    fun insertChat(chat: Chat) {
        val transaction = db.newTransaction()
        try {
            db.insert(Chat.TABLE_NAME, Chat.toContentValues(chat), SQLiteDatabase.CONFLICT_REPLACE)
            transaction.markSuccessful()
        } catch (e: Exception) {
            Log.e(TAG, "Error $e")
        } finally {
            transaction.end()
        }
    }

    fun selectChuyenThang(sdt_nhan: String): ChuyenThang?{
        val query = "Select * From ${ChuyenThang.TABLE_NAME} Where ${ChuyenThang.SDT_NHAN} = $sdt_nhan"

        val cursor = db.query(query)
        return if (cursor != null && cursor.moveToFirst())
            ChuyenThang.parseCursor(cursor) else null
    }

//        this.db.QueryData("Insert Into tbl_tinnhanS values (null, '" + Ngay_gui + "', '" + Gio_gui + "', 2, '" + Ten_kh + "', '" + cursor.getString(1) + "', '" +
//                cursor.getString(2) + "', " + (getSoTN.getInt(0) + 1) + ", '" + mText.replaceAll("'", " ").trim() + "', '" + mText.replaceAll("'", " ").trim() + "', '" + mText.replaceAll("'", " ").trim() + "', 'ko',0, 0, 0, null)");
//        String query = "Select id From tbl_tinnhanS WHERE ngay_nhan = '" + Ngay_gui + "' AND ten_kh = '" + Ten_kh + "' AND so_tin_nhan = " + getSoTN.getInt(0) + 1 + " And type_kh = 2";
//        Cursor c = db.GetData(query);
//        c.moveToFirst();

}