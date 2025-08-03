package com.mobicom.s16.presas.shanette.mco

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobicom.s16.presas.shanette.mco.adapter.ExpenseAdapter
import com.mobicom.s16.presas.shanette.mco.util.IconMapper

class ViewExpenseActivity : AppCompatActivity() {

    private var index = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_exp)

        val name = intent.getStringExtra(ExpenseAdapter.KEY_NAME) ?: ""
        val price = intent.getStringExtra(ExpenseAdapter.KEY_PRICE) ?: ""
        val date = intent.getStringExtra(ExpenseAdapter.KEY_DATE) ?: ""
        val category = intent.getStringExtra(ExpenseAdapter.KEY_CATEGORY) ?: ""
        index = intent.getIntExtra(ExpenseAdapter.KEY_INDEX, -1)
        val iconRes = IconMapper.getIconResource(category)

        findViewById<ImageView>(R.id.iconImageView).setImageResource(iconRes)
        findViewById<TextView>(R.id.exp_categ).text = category.uppercase()
        findViewById<TextView>(R.id.exp_name).text = name
        findViewById<TextView>(R.id.exp_price).text = "₱$price"
        findViewById<TextView>(R.id.exp_date).text = date

        findViewById<ImageView>(R.id.editBtn).setOnClickListener {
            val intent = Intent(this, EditExpenseActivity::class.java).apply {
                putExtra(ExpenseAdapter.KEY_NAME, name)
                putExtra(ExpenseAdapter.KEY_PRICE, price)
                putExtra(ExpenseAdapter.KEY_DATE, date)
                putExtra(ExpenseAdapter.KEY_CATEGORY, category)
                putExtra(ExpenseAdapter.KEY_INDEX, index)
            }
            startActivity(intent)
            finish()
        }

        findViewById<ImageView>(R.id.backBtn).setOnClickListener {
            startActivity(Intent(this, ExpenseHistoryActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.deleteBtn).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this expense?")
                .setPositiveButton("Delete") { _, _ ->
                    if (index != -1) {
                        ExpenseDataHelper.getExpenses().removeAt(index)
                        startActivity(Intent(this, ExpenseHistoryActivity::class.java))
                        finish()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
