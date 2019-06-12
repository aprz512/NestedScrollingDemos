package com.aprz.nestedscrolldemos.custombehavior.custom3

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.aprz.nestedscrolldemos.R

/**
 * @author by liyunlei
 *
 * write on 2019/6/11
 *
 * Class desc: ViewPager 的 behavior
 * 它的行为是从 ImageView的下面 慢慢的移动到 Toolbar的下面
 */
class TabLayoutBehavior : CoordinatorLayout.Behavior<View> {


    private var imageScrollRange = 0
    private var tabLayoutScrollRange = 0

    constructor()

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    // 这里需要处理滚动事件，所以需要复写 onXXXScroll 等方法
    // 想到 ImageView 和 Toolbar 也要滚动，那么就需要处理 3 个控件的滚动了，并且他们之间还有一定的关联
    // 所以这可能不是最好的解决方案
    // 在仔细的看一下效果图，考虑只处理 ImageView 的滚动事件，让 Toolbar 与 TabLayout 依赖它
    // 当 ImageView 的位置发生变化的时候，只需要处理好他们俩的位置就好了
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency.id == R.id.image_id
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {

        // 注意，UI效果滚动的时候，ImageView 是有视差的，也就是说，TabLayout 要比 ImageView 滑动的快
        // 这里将 ImageView 滚动的距离设置为 TabLayout的滚动距离 / 2，你也可以设置别的值

        // child 的 translationY 按照下面的公式即可
        // 很简单的比例公式
        // 设，child.translationY 为 y1, child 的最大滚动距离为 t1
        // dependency.translationY 为 y2, dependency 的最大滚动距离为 t2
        // 所以 y1 / t1 = y2 / t2

        // 因为一开始，要让 toolbar 在 ImageView 的底部，
        // 所以，child 的变化范围【imageHeight， tooBarHeight】
        // 而，dependency 的变化范围为【0，-imageScrollRange】


        val childOffset =
            Math.abs(dependency.translationY) * getTabLayoutScrollRange(child.context) / getImageScrollRange(child.context)
        child.translationY = getImageScrollStartY(child.context) - childOffset

        return false
    }

}