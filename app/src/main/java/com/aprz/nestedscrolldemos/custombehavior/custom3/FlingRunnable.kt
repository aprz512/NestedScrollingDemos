package com.aprz.nestedscrolldemos.custombehavior.custom3

import android.view.View
import android.widget.OverScroller
import androidx.core.view.ViewCompat
import kotlin.math.roundToInt

/**
 * @author by liyunlei
 *
 * write on 2019/6/11
 *
 * Class desc:
 */
class FlingRunnable internal constructor(
    private val view: View,
    lastFlingRunnable: FlingRunnable?
) : Runnable {

    init {
        view.removeCallbacks(lastFlingRunnable)
    }

    private val overScroller = OverScroller(view.context)

    fun scrollToClosed(duration: Int) {
        val curTranslationY = view.translationY
        val dy = -getImageScrollRange(view.context) - curTranslationY
        overScroller.startScroll(0, curTranslationY.roundToInt(), 0, dy.roundToInt(), duration)
        start()
    }

    fun scrollToOpen(duration: Int) {
        val curTranslationY = view.translationY
        val dy = 0 - curTranslationY
        overScroller.startScroll(0, curTranslationY.roundToInt(), 0, dy.roundToInt(), duration)
        start()
    }

    private fun start() {
        if (overScroller.computeScrollOffset()) {
            ViewCompat.postOnAnimation(view, this)
        }
    }


    override fun run() {
        if (overScroller.computeScrollOffset()) {
            view.translationY = overScroller.currY.toFloat()
            ViewCompat.postOnAnimation(view, this)
        }
    }

}