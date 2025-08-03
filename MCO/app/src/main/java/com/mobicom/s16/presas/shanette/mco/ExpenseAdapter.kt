package com.mobicom.s16.presas.shanette.mco.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.mobicom.s16.presas.shanette.mco.databinding.ExpItemLayoutBinding
import com.mobicom.s16.presas.shanette.mco.model.ExpenseModel
import com.mobicom.s16.presas.shanette.mco.ViewExpenseActivity
import com.mobicom.s16.presas.shanette.mco.util.IconMapper

class ExpenseAdapter(
    private val fullList: ArrayList<ExpenseModel>,
    private val viewExpenseLauncher: ActivityResultLauncher<Intent>
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    companion object {
        const val KEY_NAME = "KEY_NAME"
        const val KEY_PRICE = "KEY_PRICE"
        const val KEY_DATE = "KEY_DATE"
        const val KEY_CATEGORY = "KEY_CATEGORY"
        const val KEY_INDEX = "KEY_INDEX"
    }

    // Displayed list is a copy of full list, modified by search/filter
    private var displayedList: List<ExpenseModel> = ArrayList(fullList)

    inner class ExpenseViewHolder(val binding: ExpItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: ExpenseModel) {
            binding.expenseName.text = expense.name
            binding.expenseDate.text = expense.date
            binding.expenseAmount.text = "₱${expense.price}"
            binding.iconImageView.setImageResource(IconMapper.getIconResource(expense.category))

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, ViewExpenseActivity::class.java).apply {
                    putExtra(KEY_NAME, expense.name)
                    putExtra(KEY_PRICE, expense.price)
                    putExtra(KEY_DATE, expense.date)
                    putExtra(KEY_CATEGORY, expense.category)
                    putExtra(KEY_INDEX, fullList.indexOf(expense)) // Keep index based on full list
                }
                viewExpenseLauncher.launch(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ExpItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(displayedList[position])
    }

    override fun getItemCount(): Int = displayedList.size

    // Used for search/filter functionality
    fun setExpenseList(newList: List<ExpenseModel>) {
        displayedList = newList
        notifyDataSetChanged()
    }

    fun resetToFullList() {
        displayedList = fullList
        notifyDataSetChanged()
    }

    fun getFullList(): List<ExpenseModel> = fullList
}
