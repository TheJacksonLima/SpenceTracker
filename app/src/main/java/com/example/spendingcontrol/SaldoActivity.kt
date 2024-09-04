package com.example.spendingcontrol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spendingcontrol.adapter.SaldoAdapter
import com.example.spendingcontrol.database.DatabaseHandler
import com.example.spendingcontrol.databinding.ActivitySaldoBinding


 class SaldoActivity : AppCompatActivity() {
        private lateinit var binding : ActivitySaldoBinding
        private lateinit var database : DatabaseHandler

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivitySaldoBinding.inflate(layoutInflater)
            setContentView(binding.root)
            database = DatabaseHandler(this)

            val data = database.cursorList()
            val adapter = SaldoAdapter(this,data)
            binding.lvPrincipal.adapter = adapter
        }
}