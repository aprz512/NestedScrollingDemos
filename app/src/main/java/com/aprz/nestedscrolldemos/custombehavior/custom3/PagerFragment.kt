package com.aprz.nestedscrolldemos.custombehavior.custom3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.aprz.nestedscrolldemos.R
import com.aprz.nestedscrolldemos.adapter.RankAdapter

/**
 * @author by liyunlei
 *
 * write on 2019/6/11
 *
 * Class desc:
 */
class PagerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_pager, container, false) as ViewGroup
        initView(root)
        return root
    }

    private fun initView(root: ViewGroup) {
        val rankList = root.findViewById<RecyclerView>(R.id.rankList)
        rankList.adapter = RankAdapter()
    }

}