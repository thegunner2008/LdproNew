package tamhoang.ldpro4.util

import android.content.ContentValues.TAG
import android.database.Cursor
import android.util.Log

/**
 * Lấy giá trị String của 1 item trong database theo tên cột. Nếu cột đó không tồn tại thì sử dụng
 * giá trị mặc định.
 *
 * @param columnName tên cột.
 * @param defaultValue giá trị mặc định.
 * @return value String
 * */
fun Cursor.getString(columnName: String, defaultValue: String = "") : String {
    val index = getColumnIndex(columnName)
    return getString(index) ?: defaultValue
}

/**
 * Lấy giá trị Int của 1 item trong database theo tên cột. Nếu cột đó không tồn tại thì sử dụng
 * giá trị mặc định.
 *
 * @param columnName tên cột.
 * @param defaultValue giá trị mặc định.
 * @return value int
 * */
fun Cursor.getInt(columnName: String, defaultValue: Int = 0) : Int {
    val index = getColumnIndex(columnName)
    Log.e(TAG, "getInt: "+ index )
    return if (index >= 0) getInt(index) else defaultValue
}

/**
 * Lấy giá trị Long của 1 item trong database theo tên cột. Nếu cột đó không tồn tại thì sử dụng
 * giá trị mặc định.
 *
 * @param columnName tên cột.
 * @param defaultValue giá trị mặc định.
 * @return value long
 * */
fun Cursor.getLong(columnName: String, defaultValue: Long = 0L) : Long {
    val index = getColumnIndex(columnName)
    return if (index >= 0) getLong(index) else defaultValue
}

/**
 * Lấy giá trị Boolean của 1 item trong database theo tên cột. Nếu cột đó không tồn tại thì sử dụng
 * giá trị mặc định.
 *
 * @param columnName tên cột.
 * @param defaultValue giá trị mặc định.
 * @return value boolean
 * */
fun Cursor.getBoolean(columnName: String, defaultValue: Boolean = false) : Boolean {
    val index = getColumnIndex(columnName)
    return if(index >= 0) getInt(index) == 1 else defaultValue
}

fun Cursor.getFloat(columnName: String, defaultValue: Float = 0F) : Float {
    val index = getColumnIndex(columnName)
    return if (index >= 0) getFloat(index) else defaultValue
}

fun Cursor.getDouble(columnName: String, defaultValue: Double = 0.0) : Double {
    val index = getColumnIndex(columnName)
    return if (index >= 0) getDouble(index) else defaultValue
}
