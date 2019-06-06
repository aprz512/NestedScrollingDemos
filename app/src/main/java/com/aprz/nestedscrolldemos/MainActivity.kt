package com.aprz.nestedscrolldemos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aprz.nestedscrolldemos.wxsportrank.SportRankActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_wx_sport.setOnClickListener {
            startActivity(Intent(this, SportRankActivity::class.java))
        }
    }
}
