package com.aprz.nestedscrolldemos.custombehavior.custom2

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

/**
 * @author by liyunlei
 *
 * write on 2019/6/10
 *
 * Class desc:
 */
class TitleBehavior : CoordinatorLayout.Behavior<TextView> {

    constructor()

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: TextView,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: TextView,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)

        if (target !is RecyclerView) {
            return
        }

        // 这里的逻辑与 com.aprz.nestedscrolldemos.nestedscroll.wxsportrank.SportRankLayout.onNestedPreScroll 的差不多


        // 因为在第一个item完全可见的情况下，TextView 需要先处理滚动，其他的情况全部让 RecyclerView 处理
        // 把这行代码注掉，就是让 TextView 先于 RecyclerView 滚动，所以无论什么时候 TextView 都会先滚动，类似于
        // scroll_flag 的 enterAlways

        if (!firstItemCompletelyVisible(target)) {
            return
        }

        // 手指向上滑动
        if (dy > 0) {
            consumed[1] = if (child.height - Math.abs(child.translationY) < dy) {
                child.height - Math.abs(child.translationY.roundToInt())
            } else {
                dy
            }
        }

        // 手指向下滑动
        if (dy < 0) {
            consumed[1] = if (Math.abs(child.translationY) < Math.abs(dy)) {
                child.translationY.roundToInt()
            } else {
                dy
            }
        }

        // consume[1] 是 TextView 能够消耗的
        // 移动 TextView 的位置
        child.translationY -= consumed[1]
    }

    private fun firstItemCompletelyVisible(recyclerView: RecyclerView): Boolean {

        if (recyclerView.layoutManager !is LinearLayoutManager) {
            return false
        }

        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager

        val findFirstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
        if (findFirstCompletelyVisibleItemPosition != 0) {
            return false
        }

        return true
    }

}