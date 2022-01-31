package dev.jaym21.exspends.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.jaym21.exspends.R
import dev.jaym21.exspends.data.models.Expense
import dev.jaym21.exspends.utils.DateConverterUtils

class ExpensesRVAdapter(private val listener: IExpensesRVAdapter): ListAdapter<Expense, ExpensesRVAdapter.ExpenseViewHolder>(ExpensesDiffUtil()) {

    class ExpensesDiffUtil: DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }

    inner class ExpenseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val root: RelativeLayout = itemView.findViewById(R.id.rlExpenseRoot)
        val categoryCard: LinearLayout = itemView.findViewById(R.id.llCategoryIcon)
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

        holder.root.setOnClickListener {
            listener.onExpenseClick(currentItem)
        }

        when(currentItem.category) {
            "groceries" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_groceries).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.green))
                holder.categoryCard.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.groceries_icon_bg)
            }
            "shopping" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_shopping).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.blue))
                holder.categoryCard.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.shopping_icon_bg)
            }
            "subscription" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_subscriptions).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.orange))
                holder.categoryCard.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.subscriptions_icon_bg)
            }
            "entertainment" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_entertainment).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.red))
                holder.categoryCard.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.entertainment_icon_bg)
            }
            "restaurant" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_restaurant).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.purple))
                holder.categoryCard.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.restaurant_icon_bg)
            }
            "travel" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_travel).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.yellow))
                holder.categoryCard.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.travel_icon_bg)
            }
            "bills" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_bill).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.turquoise))
                holder.categoryCard.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bills_icon_bg)
            }
            "investments" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_investments).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.pink))
                holder.categoryCard.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.investments_icon_bg)
            }
            "others" -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_others).into(holder.categoryIcon)
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.grey))
                holder.categoryCard.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.others_icon_bg)
            }
        }
    }
}

interface IExpensesRVAdapter {
    fun onExpenseClick(expense: Expense)
}