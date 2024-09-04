package com.example.spendingcontrol.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.spendingcontrol.R
import com.example.spendingcontrol.database.DatabaseHandler.Companion.COD
import com.example.spendingcontrol.database.DatabaseHandler.Companion.CATEGORY
import com.example.spendingcontrol.database.DatabaseHandler.Companion.DATE
import com.example.spendingcontrol.database.DatabaseHandler.Companion.DETAILS
import com.example.spendingcontrol.database.DatabaseHandler.Companion.TYPE
import com.example.spendingcontrol.database.DatabaseHandler.Companion.VALUE
import com.example.spendingcontrol.entity.Spence

class SaldoAdapter (val context: Context, val cursor: Cursor): BaseAdapter() {

        override fun getCount(): Int {
            return cursor.count
        }

        override fun getItem(position: Int): Any {
            cursor.moveToPosition(position)

            return  Spence(
                cursor.getInt(COD),
                cursor.getString(CATEGORY),
                cursor.getString(DETAILS),
                cursor.getString(TYPE),
                cursor.getDouble(VALUE),
                cursor.getString(DATE),
            )
        }

        // chave primaria
        override fun getItemId(position: Int): Long {
            cursor.moveToPosition(position)
            return cursor.getLong(0)
        }

        override fun getView(position: Int, contextView: View?, parent: ViewGroup?): View {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v = inflater.inflate(R.layout.elemento_lista,null)

            val tvSpenceDetail = v.findViewById<TextView>(R.id.tvSpenceDetail)
            val tvSpenceInfo = v.findViewById<TextView>(R.id.tvSpenceInfo)
            val ivSpenceIcon = v.findViewById<ImageView>(R.id.ivIcon)

            cursor.moveToPosition(position)
            tvSpenceDetail.setText(cursor.getString(DETAILS))


            val info = cursor.getString(DATE).toString() +"  $" + cursor.getString(VALUE).toString()
            tvSpenceInfo.setText(info)

            val type = cursor.getString(TYPE)

            if (type == "C")
                ivSpenceIcon.setImageResource(android.R.drawable.ic_input_add)
            else
                ivSpenceIcon.setImageResource(android.R.drawable.ic_delete)

            return v
        }

 }
