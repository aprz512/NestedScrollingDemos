package com.aprz.nestedscrolldemos.custombehavior.custom1

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView

/**
 * @author by liyunlei
 *
 * write on 2019/6/10
 *
 * Class desc:
 */
class SimpleTitleBehavior : CoordinatorLayout.Behavior<View> {

    companion object {
        const val TAG = "SimpleTitleBehavior"
    }

    private var dependencyInitY = -100f

    constructor() {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {

        // parent 是 CoordinatorLayout
        // child 是使用该 behavior 的 view
        // dependency 是要依赖的 view
        // layoutDependsOn 该方法会在 for 循环中调用，所以会遍历 parent 的所有 child

        return dependency is RecyclerView
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        // 当被监听的View状态变化时会调用该方法，参数和 layoutDependsOn 方法一致。

        // 由于 CollapsingToolbarLayout 中 layout_scrollFlags 的某些属性值会导致 view 的层次变化，所以这里需要将 child 拉到最顶层
        child.bringToFront()

        if (dependencyInitY == -100f) {
            // 记录layout的初始位置
            dependencyInitY = dependency.y
        }

        // 计算百分比，因为 dependency 要走 dependencyInitY - child.height，child 要走 child.height，才能搞好重合
        val percent = 1f * (dependency.y - child.height) / (dependencyInitY - child.height)
        if (percent < 0) {
            return false
        }
        // 这里取负值，表示刚开始的时候看不见，最后才能完全看见
        child.translationY = -child.height * Math.max(0f, percent)
        // 设置透明度
        child.alpha = 1f - percent

        // 返回值 true 表示 child 的位置变化了
        return true
    }

}