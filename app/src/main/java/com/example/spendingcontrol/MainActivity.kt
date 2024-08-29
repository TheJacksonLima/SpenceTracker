package com.example.spendingcontrol

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spendingcontrol.database.DatabaseHandler
import com.example.spendingcontrol.databinding.ActivityMainBinding
import com.example.spendingcontrol.entity.Spence

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding : ActivityMainBinding
    private lateinit var database : DatabaseHandler
    private lateinit var expenseType :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


       setSpinnerAdapter()
       setButtonListener()

       database = DatabaseHandler(this)


    }

    private fun setSpinnerAdapter() {
        ArrayAdapter.createFromResource(
            this,
            R.array.spense_type,
            android.R.layout.simple_spinner_item
        ).also { adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            binding.spExpenseType.adapter = adapter

        }
    }

    private fun setButtonListener() {
        binding.btBalance.setOnClickListener(){ btBalanceOnClick()}
        binding.btAddExpense.setOnClickListener(){ btAddExpense()}
        binding.btCheckExpenses.setOnClickListener(){ btCheckExpenses()}
        binding.spExpenseType.onItemSelectedListener = this




    }

    private fun spExpenseTypeOnClick() {

    }

    private fun btCheckExpenses() {
    }

    private fun btAddExpense() {

        database.insert(Spence(0,binding.spExpenseType.toString(),binding.etValue.text.toString().toFloat(),binding.etTime.text.toString()))
        binding.etValue.setText("")
        binding.etTime.setText("")
        Toast.makeText(this,"Despesa inserida com sucesso", Toast.LENGTH_LONG).show()


    }

    private fun btBalanceOnClick() {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
          expenseType = p0?.getItemAtPosition(p2).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}