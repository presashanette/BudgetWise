package com.mobicom.budgetwise

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class EditExpenseActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var etvName: EditText
    private lateinit var etvPrice: EditText
    private lateinit var etvDate: EditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private var index: Int = -1
    private val items = listOf(
        "Food", "Groceries", "Transportation", "Utilities", "Savings",
        "Entertainment", "Fitness & Health", "Shopping"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_exp)

        spinner = findViewById(R.id.expenseSpinner)
        etvName = findViewById(R.id.etvName)
        etvPrice = findViewById(R.id.etvPrice)
        etvDate = findViewById(R.id.etvDate)
        btnSave = findViewById(R.id.btnSaveExp)
        btnCancel = findViewById(R.id.btnCancel)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()

        etvDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, y, m, d ->
                calendar.set(y, m, d)
                etvDate.setText(dateFormat.format(calendar.time))
            }, year, month, day).show()
        }

        // Get passed data
        val name = intent.getStringExtra(ExpenseAdapter.KEY_NAME) ?: ""
        val price = intent.getStringExtra(ExpenseAdapter.KEY_PRICE) ?: ""
        val date = intent.getStringExtra(ExpenseAdapter.KEY_DATE) ?: ""
        val category = intent.getStringExtra(ExpenseAdapter.KEY_CATEGORY) ?: ""
        index = intent.getIntExtra(ExpenseAdapter.KEY_INDEX, -1)

        // Pre-fill fields
        etvName.setText(name)
        etvPrice.setText(price)
        etvDate.setText(date)
        val categoryIndex = items.indexOf(category)
        if (categoryIndex >= 0) spinner.setSelection(categoryIndex)

        btnSave.setOnClickListener {
            val updatedExpense = ExpenseModel(
                etvName.text.toString(),
                etvDate.text.toString(),
                etvPrice.text.toString(),
                spinner.selectedItem.toString()
            )
            if (index >= 0) {
                ExpenseDataHelper.updateExpense(index, updatedExpense)
            }
            finish()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}
