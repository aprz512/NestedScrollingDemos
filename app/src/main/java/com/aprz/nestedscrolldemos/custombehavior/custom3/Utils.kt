package com.aprz.nestedscrolldemos.custombehavior.custom3

import android.content.Context
import com.aprz.nestedscrolldemos.R

/**
 * @author by liyunlei
 *
 * write on 2019/6/12
 *
 * Class desc:
 */
fun getTabLayoutScrollRange(context: Context): Int {
    return context.resources.getDimensionPixelOffset(R.dimen.image_height) -
            context.resources.getDimensionPixelOffset(R.dimen.toolbar_height)
}

fun getViewPagerScrollRange(context: Context): Int {
    return context.resources.getDimensionPixelOffset(R.dimen.image_height) -
            context.resources.getDimensionPixelOffset(R.dimen.toolbar_height) -
            context.resources.getDimensionPixelOffset(R.dimen.tab_height)
}

// Image 滚动范围设置为 Tab 可滚动范围的 0.5 倍
fun getImageScrollRange(context: Context): Int {
    return getTabLayoutScrollRange(context) shr 1
}

fun getToolbarScrollRange(context: Context): Int {
    return context.resources.getDimensionPixelOffset(R.dimen.toolbar_height)
}

fun getImageScrollStartY(context: Context): Int {
    return context.resources.getDimensionPixelOffset(R.dimen.image_height)
}

fun getToolbarScrollStartY(context: Context): Int {
    return -context.resources.getDimensionPixelOffset(R.dimen.toolbar_height)
}