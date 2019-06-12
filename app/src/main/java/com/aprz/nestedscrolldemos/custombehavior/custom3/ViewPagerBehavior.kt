package com.aprz.nestedscrolldemos.custombehavior.custom3

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.aprz.nestedscrolldemos.R
import kotlin.math.roundToInt

/**
 * @author by liyunlei
 *
 * write on 2019/6/11
 *
 * Class desc: ViewPager 的 behavior
 */
class ViewPagerBehavior : CoordinatorLayout.Behavior<View> {

    private var lastX: Float = 0f
    private var lastY: Float = 0f

    constructor()

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency.id == R.id.image_id
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        // viewPager 比 tab 移动的距离要少一个 tab 的高度
        val childOffset =
            Math.abs(dependency.translationY) * getViewPagerScrollRange(child.context) / getImageScrollRange(child.context)
        child.translationY = (getImageScrollStartY(child.context) - childOffset.roundToInt()).toFloat()
        Log.e("d", "${child.translationY}")
        return true
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: View, ev: MotionEvent): Boolean {
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            lastX = ev.x
            lastY = ev.y
        }

        if (ev.actionMasked == MotionEvent.ACTION_MOVE) {
            val currX = ev.x
            val currY = ev.y
            val horizontalScroll = Math.abs(currX - lastX) / Math.abs(currY - lastY) > 1
            if (horizontalScroll &&
                child.translationY == getImageScrollStartY(child.context).toFloat()
            ) {
                return true
            }
        }
        return super.onInterceptTouchEvent(parent, child, ev)
    }


}