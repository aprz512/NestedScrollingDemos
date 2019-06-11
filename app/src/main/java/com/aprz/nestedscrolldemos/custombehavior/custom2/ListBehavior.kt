package com.aprz.nestedscrolldemos.custombehavior.custom2

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt


/**
 * @author by liyunlei
 *
 * write on 2019/6/10
 *
 * Class desc:
 */
class ListBehavior : CoordinatorLayout.Behavior<RecyclerView> {

    constructor()

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun layoutDependsOn(parent: CoordinatorLayout, child: RecyclerView, dependency: View): Boolean {
        return dependency is TextView
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: RecyclerView, dependency: View): Boolean {
        // 让 child 跟随 dependency
        val bottom = dependency.y.roundToInt() + dependency.height
        if (bottom < 0) {
            return false
        }
        child.top = bottom
        return true
    }
}