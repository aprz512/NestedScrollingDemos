package com.aprz.nestedscrolldemos.wxsportrank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.aprz.nestedscrolldemos.R
import kotlinx.android.synthetic.main.activity_sport_rank.*

/**
 * @author by liyunlei
 *
 * write on 2019/6/5
 *
 * Class desc:
 */
class SportRankActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sport_rank)

        rankList.adapter = RankAdapter()
    }

}

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
        name.text = "aprz = ${position}"
        steps.text = s
    }


}
