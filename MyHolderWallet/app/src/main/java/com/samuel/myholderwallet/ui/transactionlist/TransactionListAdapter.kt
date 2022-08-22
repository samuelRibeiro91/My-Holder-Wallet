package com.samuel.myholderwallet.ui.transactionlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.entity.TransactionEntity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow

class TransactionListAdapter(private val transactions: List<TransactionEntity>): RecyclerView.Adapter<TransactionListAdapter.TransactionListViewHolder>() {
    var onItemClick: ((entity: TransactionEntity) -> Unit)? = null
    var onDeleteClick: ((entity: TransactionEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_item, parent, false)

        return TransactionListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionListViewHolder, position: Int) {
        holder.bindView(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size


    inner class TransactionListViewHolder(item: View): RecyclerView.ViewHolder(item){
        private val transactionDate: TextView = itemView.findViewById(R.id.text_transaction_date)
        private val transactionType: TextView = itemView.findViewById(R.id.text_transaction_type)
        private val transactionValue: TextView = itemView.findViewById(R.id.text_transaction_value)
        private val buttonTransactionDelete: ImageButton = itemView.findViewById(R.id.transaction_delete)

        fun bindView(transactionEntity: TransactionEntity){
            transactionDate.text   = SimpleDateFormat("dd/MM/yyyy").format(Date(transactionEntity.date.toLong()))
            transactionType.text   = transactionEntity.type.toString()

            val finalValue = transactionEntity.value * transactionEntity.quantity


            transactionValue.text  = "R$ ${String.format("%.2f",finalValue)}"

            itemView.setOnClickListener{
                onItemClick?.invoke(transactionEntity)
            }

            buttonTransactionDelete.setOnClickListener {
                onDeleteClick?.invoke(transactionEntity)
            }
        }
    }
}