package com.aprz.nestedscrolldemos.custombehavior.custom2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aprz.nestedscrolldemos.R
import com.aprz.nestedscrolldemos.adapter.RankAdapter
import kotlinx.android.synthetic.main.activity_custom_behavior2.*

/**
 * @author by liyunlei
 *
 * write on 2019/6/10
 *
 * Class desc:
 */
class CustomBehaviorActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_custom_behavior2)

        rankList.adapter = RankAdapter()
    }

}