package com.samuel.myholderwallet.ui.paperlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.entity.PaperEntity

class PaperListAdapter(private val papers: List<PaperEntity>) : RecyclerView.Adapter<PaperListAdapter.BrokerListViewHolder>() {
    var onItemClick: ((entity: PaperEntity) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BrokerListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.paper_item, parent, false)

        return BrokerListViewHolder(view)
    }

    override fun onBindViewHolder(holder: BrokerListViewHolder, position: Int) {
        holder.bindView(papers[position])
    }

    override fun getItemCount() = papers.size

    inner class BrokerListViewHolder(item: View): RecyclerView.ViewHolder(item){
        private val textBrokerInitial: TextView = itemView.findViewById(R.id.text_paper_initial)
        private val textBrokerDescription: TextView = itemView.findViewById(R.id.text_paper_description)

        fun bindView(paper: PaperEntity){
            textBrokerInitial.text = paper.initial
            textBrokerDescription.text = paper.description

            itemView.setOnClickListener{
                onItemClick?.invoke(paper)
            }

        }
    }
}