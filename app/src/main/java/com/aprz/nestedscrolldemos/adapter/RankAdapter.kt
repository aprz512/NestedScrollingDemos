package com.aprz.nestedscrolldemos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aprz.nestedscrolldemos.R

/**
 * @author by liyunlei
 *
 * write on 2019/6/10
 *
 * Class desc:
 */
class RankAdapter : RecyclerView.Adapter<RankViewHolder>() {

    private val datas = mutableListOf<String>()

    init {
        for (i in 0..20) {
            datas.add(i, "index = $i")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sport_rank, parent, false)
        return RankViewHolder(root)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        holder.bind(datas[position], position)
    }
}

class RankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var name: TextView = itemView.findViewById(R.id.name)
    private var steps: TextView = itemView.findViewById(R.id.steps)

    fun bind(s: String, position: Int) {
        name.text = String.format(itemView.resources.getString(R.string.rank_item_text), position)
        steps.text = s
    }


}