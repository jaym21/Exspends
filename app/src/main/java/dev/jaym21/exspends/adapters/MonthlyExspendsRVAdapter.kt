package dev.jaym21.exspends.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.jaym21.exspends.R
import dev.jaym21.exspends.data.models.MonthlyExpense

class MonthlyExspendsRVAdapter: ListAdapter<MonthlyExpense, MonthlyExspendsRVAdapter.MonthlyExspendsViewHolder>(MonthlyExspendsDiffUtil()) {

    class MonthlyExspendsDiffUtil: DiffUtil.ItemCallback<MonthlyExpense>() {
        override fun areItemsTheSame(oldItem: MonthlyExpense, newItem: MonthlyExpense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MonthlyExpense, newItem: MonthlyExpense): Boolean {
            return oldItem == newItem
        }

    }

    inner class MonthlyExspendsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val month: TextView = itemView.findViewById(R.id.tvMonth)
        val year: TextView = itemView.findViewById(R.id.tvYear)
        val totalExspends: TextView = itemView.findViewById(R.id.tvTotalMonthExspends)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyExspendsViewHolder {
        return MonthlyExspendsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_monthly_exspend_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MonthlyExspendsViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.month.text = currentItem.month
        holder.year.text = currentItem.year
        holder.totalExspends.text = "₹${currentItem.totalAmount}"
    }
}