package com.aprz.nestedscrolldemos.custombehavior.custom3

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import kotlin.math.roundToInt

/**
 * @author by liyunlei
 *
 * write on 2019/6/11
 *
 * Class desc:
 */
class ImageBehavior : CoordinatorLayout.Behavior<View> {

    private var imageScrollRange = 0
    private var flingRunnable: FlingRunnable? = null

    constructor()

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    /**
     *  需要在这里处理滚动事件，其他的3个控件都依赖于这个货
     */
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        // 在 pre 中，我们只处理向上滚动
        // 向下滚动在 onNestedScroll 中处理
        if (dy < 0)
            return

        if (child.translationY.roundToInt() - dy < -getImageScrollRange(child.context)) {
            consumed[1] = child.translationY.roundToInt() + getImageScrollRange(child.context)
        } else {
            consumed[1] = dy
        }

        child.translationY -= consumed[1]
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {

        // 在这里处理向下滚动，因为RecyclerView 先滚动完，然后还有未消耗的就给 ImageView 处理
        // 这里让 ImageView 下移就好了
        // 感觉这里写逻辑比之前全部在 onNestedPreScroll 中处理逻辑要清晰，比较符合 NestedScrolling 的思想
        if (dyUnconsumed > 0) {
            return
        }

        if (child.translationY.roundToInt() - dyUnconsumed > 0) {
            consumed[1] = child.translationY.roundToInt()
        } else {
            consumed[1] = dyUnconsumed
        }

        child.translationY -= consumed[1]
    }

    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {

        if (child.translationY == 0f) {
            return false
        }

        return child.translationY != -getImageScrollRange(child.context).toFloat()
    }


    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, type: Int) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        // 这里做一个吸附效果
        autoSettle(child)
    }

    private fun autoSettle(child: View) {
        // View 触发 Action_DOWN的时候，会调用一次 onStopNestedScroll
        // 这里做特殊处理
        if ((child.translationY == 0f) or (child.translationY == -getImageScrollRange(child.context).toFloat())) {
            return
        }
        flingRunnable = FlingRunnable(child, flingRunnable)
        if (child.translationY < -getImageScrollRange(child.context) / 2) {
            flingRunnable?.scrollToClosed(300)
        } else {
            flingRunnable?.scrollToOpen(300)
        }
    }


}