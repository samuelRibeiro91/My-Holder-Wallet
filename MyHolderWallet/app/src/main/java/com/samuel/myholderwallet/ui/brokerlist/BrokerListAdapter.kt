package com.samuel.myholderwallet.ui.brokerlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.entity.BrokerEntity

class BrokerListAdapter(private val brokers: List<BrokerEntity>) : RecyclerView.Adapter<BrokerListAdapter.BrokerListViewHolder>() {
    var onItemClick: ((entity: BrokerEntity) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BrokerListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.broker_item, parent, false)

        return BrokerListViewHolder(view)
    }

    override fun onBindViewHolder(holder: BrokerListViewHolder, position: Int) {
        holder.bindView(brokers[position])
    }

    override fun getItemCount() = brokers.size

    inner class BrokerListViewHolder(item: View): RecyclerView.ViewHolder(item){
        private val textBrokerName: TextView = itemView.findViewById(R.id.text_broker_name)

        fun bindView(broker: BrokerEntity){
            textBrokerName.text = broker.name

            itemView.setOnClickListener{
                onItemClick?.invoke(broker)
            }

        }
    }
}