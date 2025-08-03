package com.mobicom.s16.presas.shanette.mco

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.mobicom.s16.presas.shanette.mco.ExpenseDataHelper
import com.mobicom.s16.presas.shanette.mco.model.ExpenseModel
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        val spinner: Spinner = findViewById(R.id.expenseSpinner)
        val etvName: EditText = findViewById(R.id.etvName)
        val etvPrice: EditText = findViewById(R.id.etvPrice)
        val etvDate: EditText = findViewById(R.id.etvDate)
        val btnAdd: Button = findViewById(R.id.btnAddExp)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        val items = listOf(
            "Food", "Groceries", "Transportation", "Utilities", "Savings",
            "Entertainment", "Fitness & Health", "Shopping"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        etvDate.setText(dateFormat.format(calendar.time))

        etvDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, y, m, d ->
                calendar.set(y, m, d)
                etvDate.setText(dateFormat.format(calendar.time))
            }, year, month, day).show()
        }

        btnAdd.setOnClickListener {
            val name = etvName.text.toString()
            val price = etvPrice.text.toString()
            val date = etvDate.text.toString()
            val category = spinner.selectedItem.toString()

            val expense = ExpenseModel(name, date, price, category)
            ExpenseDataHelper.addExpense(expense)

            val intent = Intent(this, ExpenseHistoryActivity::class.java)
            startActivity(intent)
            finish()

        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}
