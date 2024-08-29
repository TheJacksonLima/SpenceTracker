package com.example.spendingcontrol.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.spendingcontrol.entity.Spence

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context,DATABASE_NAME, null,DATABASE_VERSION){

    companion object {
        private const val DATABASE_NAME = "dbfile.sqlite"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "my_spenses"
        private const val COD = 0
        private const val TYPE = 1
        private const val VALUE = 2
        private const val DATE = 3
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS ${TABLE_NAME}(_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, telefone TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE ${TABLE_NAME}" )
        onCreate(db)
    }

    fun insert(spence : Spence)
    {
        val db = this.writableDatabase
        val data = ContentValues()

        data.put("type",spence.type)
        data.put("value",spence.value)
        data.put("date",spence.date)

        db.insert(TABLE_NAME,null,data)
    }

    fun update(spence: Spence)
    {
        val db = this.writableDatabase
        val data = ContentValues()

        data.put("type",spence.type)
        data.put("value",spence.value)
        data.put("date",spence.date)

        db.update(TABLE_NAME,data, "_id=${spence._id}",null)
    }

    fun delete(id : Int)
    {
        val db = this.writableDatabase
        db.delete(TABLE_NAME,"_id=${id}",null)
    }

    fun find(id : Int): Spence? {
        val db = this.writableDatabase
        val data = db.query(
            TABLE_NAME,
            null,
            "_id=${id}",
            null,
            null,
            null,
            null
        )

        if (data.moveToNext())
        {
            return Spence(
                id,
                data.getString(TYPE),
                data.getFloat(VALUE),
                data.getString(DATE),
            )
        }
        else
            return null

    }

    fun list(): MutableList<Spence>{
        val db = this.writableDatabase
        val data = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        var lData = mutableListOf<Spence>()

        while(data.moveToNext()) {
            lData.add(
                Spence(
                    data.getInt(COD),
                    data.getString(TYPE),
                    data.getFloat(VALUE),
                    data.getString(DATE),
                )
            )
        }
        return lData
    }

    fun cursorList(): Cursor {
        val db = this.writableDatabase

        return db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

    }

}

