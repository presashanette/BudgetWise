package com.mobicom.budgetwise

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ExpenseHistoryActivity : BaseActivity() {

    private lateinit var adapter: ExpenseAdapter
    private lateinit var recyclerView: RecyclerView
    private val data = ExpenseDataHelper.getExpenses()

    private val viewExpenseLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        adapter.notifyDataSetChanged() // refresh on return
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exp_history)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExpenseAdapter(data, viewExpenseLauncher)
        recyclerView.adapter = adapter

        val searchView = findViewById<SearchView>(R.id.searchView)
        val filterSpinner = findViewById<Spinner>(R.id.filterSpinner)

// Populate Spinner with "All", "Food", "Groceries", etc.
        val categories = listOf("All", "Food", "Groceries", "Transportation", "Utilities", "Savings", "Entertainment", "Fitness & Health", "Shopping")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSpinner.adapter = spinnerAdapter

// Setup listeners
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterExpenses(newText, filterSpinner.selectedItem.toString())
                return true
            }
        })

        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                filterExpenses(searchView.query.toString(), categories[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }

    private fun filterExpenses(searchQuery: String?, selectedCategory: String) {
        val originalList = ExpenseDataHelper.getExpenses()
        val filtered = originalList.filter {
            val matchesQuery = searchQuery.isNullOrBlank() || it.name.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == "All" || it.category.equals(selectedCategory, ignoreCase = true)
            matchesQuery && matchesCategory
        }
        adapter.setExpenseList(filtered)
    }


    override fun getCurrentNavItemId(): Int = R.id.nav_expenses
}
