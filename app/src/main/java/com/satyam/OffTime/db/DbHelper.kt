package com.satyam.OffTime.db
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.ArrayList

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "string_db"
        private const val TABLE_NAME = "string_table"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_STRING = "string_value"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_STRING TEXT)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertString(stringValue: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_STRING, stringValue)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()

        return id
    }

    @SuppressLint("Range")
    fun getAllStrings(): ArrayList<String> {
        val stringList = ArrayList<String>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        cursor.use {
            while (it.moveToNext()) {
                val stringValue = it.getString(it.getColumnIndex(COLUMN_STRING))
                stringList.add(stringValue)
            }
        }
        db.close()
        return stringList
    }


}