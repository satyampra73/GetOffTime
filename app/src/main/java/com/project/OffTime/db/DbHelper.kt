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
        private const val DATABASE_VERSION = 2  // Incremented version number
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
        private const val COLUMN_EM_IS_ACTIVE = "em_active"  // New column

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
                + COLUMN_EM_RELATION + " TEXT,"
                + COLUMN_EM_IS_ACTIVE + " INTEGER DEFAULT 0"  // New column with default value
                + ")")
        db.execSQL(createTableMobile)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Add the new column to the existing table
            db.execSQL("ALTER TABLE $TABLE_MOBILE ADD COLUMN $COLUMN_EM_IS_ACTIVE INTEGER DEFAULT 1")
        }
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
    fun insertData(mobile: String, name: String, relation: String, isActive: Boolean = true): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EM_MOBILE, mobile)
            put(COLUMN_EM_NAME, name)
            put(COLUMN_EM_RELATION, relation)
            put(COLUMN_EM_IS_ACTIVE, if (isActive) 1 else 0)
        }
        val id = db.insert(TABLE_MOBILE, null, values)
        db.close()
        return id
    }

    // Update existing data in Mobile Table
    fun updateData(id: Long, mobile: String, name: String, relation: String, isActive: Int ): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EM_MOBILE, mobile)
            put(COLUMN_EM_NAME, name)
            put(COLUMN_EM_RELATION, relation)
            put(COLUMN_EM_IS_ACTIVE, isActive)
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
                val isActive = it.getInt(it.getColumnIndex(COLUMN_EM_IS_ACTIVE))
                dataList.add(EmergencyData(id, mobile, name, relation, isActive))
            }
        }
        db.close()
        return dataList
    }
    fun updateIsActive(id: Long, isActive: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EM_IS_ACTIVE, isActive)  // Use the integer directly
        }
        val result = db.update(TABLE_MOBILE, values, "$COLUMN_MOBILE_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }
}

