package com.aprz.nestedscrolldemos.nestedscroll.wxsportrank

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aprz.nestedscrolldemos.R
import com.aprz.nestedscrolldemos.adapter.RankAdapter
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


