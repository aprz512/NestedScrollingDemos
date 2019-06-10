package com.aprz.nestedscrolldemos.custombehavior.custom1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aprz.nestedscrolldemos.R
import com.aprz.nestedscrolldemos.adapter.RankAdapter
import kotlinx.android.synthetic.main.activity_custom_behavior1.*

/**
 * @author by liyunlei
 *
 * write on 2019/6/10
 *
 * Class desc:
 */
class CustomBehaviorActivity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_custom_behavior1)

        rankList.adapter = RankAdapter()
    }

}