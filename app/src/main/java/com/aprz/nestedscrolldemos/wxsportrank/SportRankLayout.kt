package com.aprz.nestedscrolldemos.wxsportrank

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.aprz.nestedscrolldemos.R

/**
 * @author by liyunlei
 *
 * write on 2019/6/5
 *
 * Class desc: 效果未经过生产测试
 */
open class SportRankLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr), NestedScrollingParent2 {

    companion object {
        const val TAG = "SportRankLayout"
    }

    val parentHelper = NestedScrollingParentHelper(this)
    lateinit var cover: ImageView
    lateinit var rankList: RecyclerView
    var coverHeight: Int = 0

    override fun onFinishInflate() {
        super.onFinishInflate()
        cover = findViewById(R.id.cover)
        rankList = findViewById(R.id.rankList)
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        rankList.layout(rankList.left, cover.bottom, rankList.right, cover.bottom + rankList.height)

        coverHeight = cover.measuredHeight
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {




        // onNestedPreScroll 在 nested child 的 ACTION_MOVE 中触发
        // 这里我们可以对滑动事件进行处理
        // 如果还有未处理完的，还需要在 onNestedScroll 中处理，当然也需要 child 主动调用

        // 滑动效果详细说明：
        /*
            页面的初始状态，封面是完全显示的，封面在上，列表在下
            如果 用户向下拉列表的话，不用处理
            如果 用户向上滑动列表的话，这个时候不应该将滑动事件交给列表，而是要自己处理，用来滑动列表的位置，这样就可以隐藏封面了
            当滑动到封面完全隐藏的时候，再将滑动事件传递给列表，列表开始滚动
            同样的，如果封面完全隐藏了，向下滑动，事件需要先交给列表处理，直到列表滑动到顶部（第一个item完全显示出来）
            然后，将后续的滑动事件自己处理，用来滑动列表的位置，这样封面就可以显示出来了
         */

        // 注意，上拉滑动列表，dy 是正值，下拉滑动列表，dy 是负值
        // 上拉时，translationY 为负，下拉时，translateY 为正

        // 向上滚动，即下拉

        val translationY = rankList.translationY.toInt()

        val scrollUp = dy < 0
        val coverIncompleteShow = translationY < 0
        val nestedChildCannotVerticalScrollDown = !target.canScrollVertically(-1)
        val needShowCover = scrollUp && coverIncompleteShow && nestedChildCannotVerticalScrollDown

        // 需要显示封面
        if (needShowCover && translationY < 0) {
            Log.e("test", "needShowCover")
            // 判断能消耗多少移动距离
            consumed[1] = if (Math.abs(translationY) < Math.abs(dy)) {
                // 只能消耗一部分，是因为只需要这么一点，封面就已经全部显示出来了
                translationY
            } else {
                // 可以消耗全部
                dy
            }
        }

        val scrollDown = dy > 0
        val needHideCover = scrollDown && nestedChildCannotVerticalScrollDown

        Log.e("test", "scrollDown = $scrollDown")
        Log.e("test", "nestedChildCannotVerticalScrollDown = $nestedChildCannotVerticalScrollDown")

        // 需要隐藏封面
        if (needHideCover) {
            Log.e("test", "needHideCover")
            // 判断能消耗多少移动距离
            consumed[1] = if (Math.abs(translationY) + dy > coverHeight) {
                // 只能消耗一部分，是因为只需要这么一点，封面就已经全部隐藏了
                coverHeight - Math.abs(translationY)
            } else {
                // 可以消耗全部
                dy
            }
        }

        if (needHideCover || needShowCover) {
            rankList.translationY += -consumed[1].toFloat()
        }

        Log.e(TAG, "onNestedPreScroll, dy = $dy, consumed[1] = ${consumed[1]}")

    }

    override fun onStopNestedScroll(target: View, type: Int) {
        Log.e(TAG, "onStopNestedScroll")
        parentHelper.onStopNestedScroll(target, type)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {

        Log.e(TAG, "onStartNestedScroll")

        // 子控件通知父控件（自己）开始滑动了


        // 该方法返回 true，表示你需要接收嵌套滑动child的滑动数据，让child先不要动，放着让你来
        // 该方法返回 true，才会调用 onNestedScrollAccepted

        // 按照上面的分析，我们需要在两种情况下返回 true
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        Log.e(TAG, "onNestedScrollAccepted")
        parentHelper.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        Log.e(TAG, "onNestedScroll, dyConsumed = $dyConsumed, dyUnconsumed = $dyUnconsumed")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        Log.e(SportRankLayout2.TAG, "onTouchEvent")

        when (event?.action) {

            MotionEvent.ACTION_DOWN -> {

            }

            MotionEvent.ACTION_MOVE -> {

            }

            MotionEvent.ACTION_UP or MotionEvent.ACTION_CANCEL -> {



            }

        }

        return super.onTouchEvent(event)
    }

}