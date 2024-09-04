package com.example.spendingcontrol

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spendingcontrol.database.DatabaseHandler
import com.example.spendingcontrol.databinding.ActivityMainBinding
import com.example.spendingcontrol.entity.Spence
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var database : DatabaseHandler
    private lateinit var expenseCategory: String
    private var expenseType = "N"
    private lateinit var expenseDetails : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       setCategorySpinnerAdapter()
       setListeners()

       database = DatabaseHandler(this)


    }


    private fun setCategorySpinnerAdapter() {
        ArrayAdapter.createFromResource(
            this,
            R.array.tipos_de_despesas,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spSpenceCategory.adapter = adapter
        }

        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ){
                expenseCategory = parent.getItemAtPosition(position).toString()
                setDetailsSpinnerAdapter(position)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }.also { binding.spSpenceCategory.onItemSelectedListener = it }

    }

    private fun setDetailsSpinnerAdapter(position: Int) {

        var type: Int

        when(position) {
            1 -> type = R.array.moradia_despesas
            2 -> type = R.array.alimentacao_despesas
            3 -> type = R.array.transporte_despesas
            4 -> type = R.array.saude_despesas
            5 -> type = R.array.educacao_despesas
            6 -> type = R.array.lazer_entretenimento_despesas
            7 -> type = R.array.despesas_pessoais
            8 -> type = R.array.servicos_utilidades_despesas
            9 -> type = R.array.dividas_financiamentos_despesas
            10 -> type = R.array.impostos_taxas_despesas
            11 -> type = R.array.investimentos_poupanca_despesas
            else -> type = -1
        }

        if (type == -1) {
            binding.spSpenceDetail.adapter =
               ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, emptyList())
            expenseDetails = "--"
            return
        }

        ArrayAdapter.createFromResource(
            this,
            type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spSpenceDetail.adapter = adapter
        }

        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ){
                expenseDetails = parent.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }.also { binding.spSpenceDetail.onItemSelectedListener = it }

    }

    private fun setListeners() {
        binding.btAddExpense.setOnClickListener(){ btAddExpenseOnClick()}
        binding.btFinancialStatement.setOnClickListener(){ btFinancialStatement()}
        binding.rdCredit.setOnClickListener(){ rdCreditOnClick()}
        binding.rdIncome.setOnClickListener(){ rdIncomeOnClick()}

        binding.etDate.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showDatePickerDialog()
                return@setOnTouchListener true
            }
            false
        }
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
             val formattedDate = String.format("%02d/%02d/%02d", selectedDay, selectedMonth + 1,selectedYear)
             binding.etDate.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun rdIncomeOnClick() {
        binding.rdCredit.isChecked = false
        expenseType = "C"
    }

    private fun rdCreditOnClick() {
        binding.rdIncome.isChecked = false
        expenseType = "D"
    }

    private fun btFinancialStatement() {
        val intent = Intent(this,SaldoActivity::class.java)
        startActivity(intent)
    }

    private fun btAddExpenseOnClick() {
        if (validateFields())
        {
            database.insert(
                Spence(
                    0,
                    expenseCategory,
                    expenseDetails,
                    expenseType,
                    binding.etValue.text.toString().toDouble(),
                    binding.etDate.text.toString()
                )
            )

            // reset view
            binding.etValue.setText("")
            binding.etDate.setText("")
            binding.rdIncome.isChecked = false
            binding.rdCredit.isChecked = false
            binding.spSpenceCategory.setSelection(0)
            binding.spSpenceDetail.setSelection(0)
            expenseType = "N"

            Toast.makeText(this, "Despesa inserida com sucesso", Toast.LENGTH_LONG).show()
        }
    }

    private fun validateFields(): Boolean {
      if (expenseCategory == "--") {
          Toast.makeText(this, "Favor escolher uma categoria", Toast.LENGTH_LONG).show()
          binding.spSpenceCategory.requestFocus()
          return false
      }

      if (expenseDetails == "--") {
          Toast.makeText(this, "Favor escolher um detalhe", Toast.LENGTH_LONG).show()
          binding.spSpenceDetail.requestFocus()
          return false
      }

      if (binding.etValue.text.toString().length == 0) {
            Toast.makeText(this, "Favor digitar um valor", Toast.LENGTH_LONG).show()
            binding.etValue.requestFocus()
            return false
      }

      if (binding.etDate.text.toString().length == 0) {
            Toast.makeText(this, "Favor digitar uma data", Toast.LENGTH_LONG).show()
            binding.etDate.requestFocus()
            return false
      }

      if (expenseType != "C" && expenseType !=  "D") {
          Toast.makeText(this, "Favor selecionar: Saida ou Entrada", Toast.LENGTH_LONG).show()
          binding.rdCredit.requestFocus()
          return false
      }
      return true
    }
}