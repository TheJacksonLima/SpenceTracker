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
        private const val DATABASE_VERSION = 5
        private const val TABLE_NAME = "my_spenses"
        const val COD = 0
        const val CATEGORY = 1
        const val DETAILS = 2
        const val TYPE = 3
        const val VALUE = 4
        const val DATE = 5
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS ${TABLE_NAME}(_id INTEGER PRIMARY KEY AUTOINCREMENT, category String, details String,type String, value Float, date String)")

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE ${TABLE_NAME}" )
        onCreate(db)
    }

    fun insert(spence : Spence)
    {
        val db = this.writableDatabase
        val data = ContentValues()

        data.put("category",spence.category)
        data.put("details",spence.details)
        data.put("type",spence.type)
        data.put("value",spence.value)
        data.put("date",spence.date)

        db.insert(TABLE_NAME,null,data)
    }

    fun update(spence: Spence)
    {
        val db = this.writableDatabase
        val data = ContentValues()

        data.put("category",spence.category)
        data.put("details",spence.details)
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
                data.getString(CATEGORY),
                data.getString(DETAILS),
                data.getString(TYPE),
                data.getDouble(VALUE),
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
                    data.getString(CATEGORY),
                    data.getString(DETAILS),
                    data.getString(TYPE),
                    data.getDouble(VALUE),
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
            "date, category",
            null,
            "date"
        )


    }

    fun getValues(type : String): Double {
        var sumValue: Double = 0.00

        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT SUM(value) as sum FROM my_spenses WHERE type == ?", arrayOf(type))

        if(cursor.moveToFirst()) {
            sumValue = cursor.getDouble(cursor.getColumnIndexOrThrow("sum"))
        }
        cursor.close()
        return sumValue
    }

    fun getIncome(): String {
        return getValues("D").toString()
    }

    fun getOutcome(): String{
       return getValues("C").toString()
    }

}

