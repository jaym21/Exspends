package dev.jaym21.exspends.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import dev.jaym21.exspends.R
import dev.jaym21.exspends.data.models.Expense
import dev.jaym21.exspends.utils.DateConverterUtils

class ExpensesRVAdapter: ListAdapter<Expense, ExpensesRVAdapter.ExpenseViewHolder>(ExpensesDiffUtil()) {

    class ExpensesDiffUtil: DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }

    inner class ExpenseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val categoryCard: MaterialCardView = itemView.findViewById(R.id.cvCategoryIcon)
        val categoryIcon: ImageView = itemView.findViewById(R.id.ivCategoryIcon)
        val title: TextView = itemView.findViewById(R.id.tvExpenseTitle)
        val amount: TextView = itemView.findViewById(R.id.tvExpenseAmount)
        val category: TextView = itemView.findViewById(R.id.tvExpenseCategory)
        val date: TextView = itemView.findViewById(R.id.tvExpenseDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        return ExpenseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_expense_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.title.text = currentItem.title
        holder.amount.text = "\u20B9${currentItem.amount}"
        holder.category.text = "${currentItem.category[0].uppercase()}${currentItem.category.substring(1)}"
        val dateTimestamp = DateConverterUtils.getTimestamp(currentItem.date)
        holder.date.text = DateConverterUtils.convertDateFormat(dateTimestamp)


        when(currentItem.category) {
            "groceries" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_groceries).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.green))
                holder.categoryCard.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.green_transparent))
            }
            "shopping" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_shopping).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.blue))
                holder.categoryCard.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.blue_transparent))
            }
            "subscription" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_subscriptions).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.orange))
                holder.categoryCard.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.orange_transparent))
            }
            "restaurant" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_eatingout).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.purple))
                holder.categoryCard.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.purple_transparent))
            }
            "travel" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_travel).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.yellow))
                holder.categoryCard.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.yellow_transparent))
            }
            "bills" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_personal).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.turquoise))
                holder.categoryCard.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.turquoise_transparent))
            }
            "others" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_others).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.grey))
                holder.categoryCard.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.grey_transparent))
            }
        }
    }
}