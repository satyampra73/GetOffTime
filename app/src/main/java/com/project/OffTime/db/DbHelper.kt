package com.project.OffTime.db
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.project.OffTime.model.Data
import com.project.OffTime.model.EmergencyData
import java.util.ArrayList

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "string_db"

        // Table 1: String Table
        private const val TABLE_STRING = "string_table"
        private const val COLUMN_STRING_ID = "_id"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_TIME = "time"

        // Table 2: Mobile Table
        private const val TABLE_MOBILE = "mobile_table"
        private const val COLUMN_MOBILE_ID = "_id"
        private const val COLUMN_EM_MOBILE = "em_mobile"
        private const val COLUMN_EM_NAME = "em_name"
        private const val COLUMN_EM_RELATION = "em_relation"
        private const val STATIC_ID = 1  // Predefined static ID for mobile_table
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Table 1
        val createTableString = ("CREATE TABLE " + TABLE_STRING + "("
                + COLUMN_STRING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TIME + " TEXT"
                + ")")
        db.execSQL(createTableString)

        // Create Table 2
        val createTableMobile = ("CREATE TABLE " + TABLE_MOBILE + "("
                + COLUMN_MOBILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EM_MOBILE + " TEXT,"
                + COLUMN_EM_NAME + " TEXT,"
                + COLUMN_EM_RELATION + " TEXT"
                + ")")
        db.execSQL(createTableMobile)
//        // Insert default entry into mobile_table
//        val values = ContentValues().apply {
//            put(COLUMN_MOBILE_ID, STATIC_ID)
//            put(COLUMN_EM_MOBILE, "")
//            put(COLUMN_EM_MOBILE, "")
//            put(COLUMN_EM_MOBILE, "")
//        }
//        db.insert(TABLE_MOBILE, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STRING")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOBILE")
        onCreate(db)
    }

    // Methods for String Table
    fun insertString(data: Data): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATE, data.date)
            put(COLUMN_TIME, data.time)
        }
        val id = db.insert(TABLE_STRING, null, values)
        db.close()
        return id
    }

    @SuppressLint("Range")
    fun getAllStrings(): ArrayList<Data> {
        val dataList = ArrayList<Data>()
        val selectQuery = "SELECT * FROM $TABLE_STRING"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        cursor.use {
            while (it.moveToNext()) {
                val date = it.getString(it.getColumnIndex(COLUMN_DATE))
                val time = it.getString(it.getColumnIndex(COLUMN_TIME))
                dataList.add(Data(date, time))
            }
        }
        db.close()
        return dataList
    }

    // Insert new data into Mobile Table
    fun insertData(mobile: String, name: String, relation: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EM_MOBILE, mobile)
            put(COLUMN_EM_NAME, name)
            put(COLUMN_EM_RELATION, relation)
        }
        val id = db.insert(TABLE_MOBILE, null, values)
        db.close()
        return id
    }

    // Update existing data in Mobile Table
    fun updateData(id: Long, mobile: String, name: String, relation: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EM_MOBILE, mobile)
            put(COLUMN_EM_NAME, name)
            put(COLUMN_EM_RELATION, relation)
        }
        val result = db.update(TABLE_MOBILE, values, "$COLUMN_MOBILE_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    // Delete data from Mobile Table
    fun deleteData(id: Long): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_MOBILE, "$COLUMN_MOBILE_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    // Retrieve all data from Mobile Table
    @SuppressLint("Range")
    fun getAllMobileData(): ArrayList<EmergencyData> {
        val dataList = ArrayList<EmergencyData>()
        val selectQuery = "SELECT * FROM $TABLE_MOBILE"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndex(COLUMN_MOBILE_ID))
                val mobile = it.getString(it.getColumnIndex(COLUMN_EM_MOBILE))
                val name = it.getString(it.getColumnIndex(COLUMN_EM_NAME))
                val relation = it.getString(it.getColumnIndex(COLUMN_EM_RELATION))
                dataList.add(EmergencyData(id, mobile, name, relation))
            }
        }
        db.close()
        return dataList
    }

//    @SuppressLint("Range")
//    fun getEmData(): String? {
//        val db = this.readableDatabase
//        val cursor = db.query(TABLE_MOBILE, arrayOf(COLUMN_EM_MOBILE), "$COLUMN_MOBILE_ID = ?", arrayOf(STATIC_ID.toString()), null, null, null)
//        var mobile: String? = null
//        if (cursor != null && cursor.moveToFirst()) {
//            mobile = cursor.getString(cursor.getColumnIndex(COLUMN_EM_MOBILE))
//            cursor.close()
//        }
//        db.close()
//        return mobile
//    }
}
