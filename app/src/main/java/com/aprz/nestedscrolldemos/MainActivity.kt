package com.aprz.nestedscrolldemos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.aprz.nestedscrolldemos.custombehavior.custom1.CustomBehaviorActivity1
import com.aprz.nestedscrolldemos.custombehavior.custom2.CustomBehaviorActivity2
import com.aprz.nestedscrolldemos.custombehavior.custom3.CustomBehaviorActivity3
import com.aprz.nestedscrolldemos.drag.demo01.DragActivity1
import com.aprz.nestedscrolldemos.nestedscroll.wxsportrank.SportRankActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val texts: List<String> =
        listOf(
            "微信运动排行榜",
            "自定义behavior01",
            "自定义behavior02",
            "自定义behavior03",
            "ViewDragHelper使用01"
        )

    private val activityClass: List<Class<out AppCompatActivity>> =
        listOf(
            SportRankActivity::class.java,
            CustomBehaviorActivity1::class.java,
            CustomBehaviorActivity2::class.java,
            CustomBehaviorActivity3::class.java,
            DragActivity1::class.java
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        texts.forEach { text ->
            val indexOf = texts.indexOf(text)
            val createButton = createButton(indexOf, text)
            createButton.setOnClickListener {
                startActivity(Intent(this, activityClass[indexOf]))
            }
            content.addView(createButton)
        }

    }

    private fun createButton(index: Int, text: String): Button {
        val button = Button(this)
        button.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val padding = (16 * resources.displayMetrics.density + 0.5f).toInt()
        val layoutParams: LinearLayout.LayoutParams = button.layoutParams as LinearLayout.LayoutParams
        if (index == 0) {
            layoutParams.topMargin = 0
        } else {
            layoutParams.topMargin = padding
        }
        button.setPadding(padding, padding, padding, padding)
        button.id = ViewCompat.generateViewId()
        button.text = text
        return button
    }

}
