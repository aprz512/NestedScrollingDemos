package com.aprz.nestedscrolldemos.custombehavior.custom3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aprz.nestedscrolldemos.R
import kotlinx.android.synthetic.main.activity_custom_behavior3.*
import java.util.*

/**
 * @author by liyunlei
 *
 * write on 2019/6/11
 *
 * Class desc: 在看代码之前，请确保已经对UI效果了如了如指掌
 * 为了简单，将Toolbar、TabLayout、ImageView 的高度写成了固定值
 * 当然，有实际需要的可以用别的方式来获取，而不用写固定值
 */
class CustomBehaviorActivity3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_custom_behavior3)

        initView()
    }

    private fun initView() {
        val fragments = ArrayList<PagerFragment>()
        for (i in 0..3) {
            fragments.add(PagerFragment())
        }
        pager.adapter = FragmentPagerAdapter(fragments, supportFragmentManager)
        tab.setupWithViewPager(pager)
        for (i in 0..3) {
            tab.getTabAt(i)?.text = "tab$i"
        }
    }

}

class FragmentPagerAdapter(private var fragments: List<Fragment>, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

}