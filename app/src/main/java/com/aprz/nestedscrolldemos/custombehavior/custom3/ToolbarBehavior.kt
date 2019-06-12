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
 * Class desc:
 */
class ToolbarBehavior : CoordinatorLayout.Behavior<View> {

    constructor()

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency.id == R.id.image_id
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        val childOffset =
            Math.abs(dependency.translationY) * getToolbarScrollRange(child.context) / getImageScrollRange(child.context)
        child.translationY = getToolbarScrollStartY(child.context) + childOffset
        return true
    }


}