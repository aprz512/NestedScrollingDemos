package com.aprz.nestedscrolldemos.wxsportrank

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * @author by liyunlei
 *
 * write on 2019/6/5
 *
 * Class desc: 效果未经过生产测试
 * 对 SportRankLayout2 进行增强
 *
 * 添加 overScroll 效果，如果列表上滑到了顶部，继续上滑，则将封面图片放大
 */
class SportRankLayout2 @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SportRankLayout(context, attributeSet, defStyleAttr) {

    companion object {
        const val TAG = "SportRankLayout2"
    }

    private var firstDownY = 0f
    private val viewConfiguration: ViewConfiguration = ViewConfiguration.get(context)
    private var valueAnimator: ValueAnimator? = null
    private var moveRawY = -100f

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        cover.pivotX = (cover.left + cover.width.shr(1)).toFloat()
        cover.pivotY = (cover.top + cover.height.shr(1)).toFloat()
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {

        // 当触发了 overScroll 效果的时候，这个时候如果上拉列表会隐藏封面，这是由于 SportRankLayout 的逻辑导致的
        // 所以这里我们需要修正一下，当触发 overScroll 的时候，上拉列表不隐藏封面，而是还原封面的缩放值，等到封面缩放值为1时，再去隐藏封面

        if (isRankListNotTranslate() && isCoverScaled() && dy > 0 && !target.canScrollVertically(-1)) {
            consumed[1] = if (moveRawY - firstDownY < dy) {
                rankList.translationY += (dy - (moveRawY - firstDownY)).toInt()
                dy
            } else {
                dy
            }
            scaleCover(moveRawY - firstDownY)
        } else {
            super.onNestedPreScroll(target, dx, dy, consumed, type)
        }

    }

    fun isCoverScaled(): Boolean {
        return cover.scaleX != 1f || cover.scaleY != 1f
    }

    fun isRankListNotTranslate() = rankList.translationY == 0f

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        // 既然要有 overScroll 效果，那么就需要有回弹效果
        // overScroll 是列表滑动到了顶部，并且图片也完全展示了，还继续下拉列表，这个就会触发 overScroll 效果，即封面放大
        // 因为 onNestedScroll 是 child 没有处理完的，又传递给 parent 的
        // 注意 onNestedScroll 与 onNestedPreScroll 的不同
        // child.dispatchNestedPreScroll 先询问parent是不是要消耗这个滑动，parent.onNestedPreScroll 会按照需求决定消耗多少
        // 剩余没有消耗的，child自己消耗，然后如果child也没消耗完，child.dispatchNestedScroll 再次询问parent要不要
        // 所以，我们在 onNestedScroll 处理 overScroll 效果，逻辑会显得比较清晰，因为触发 overScroll，肯定是 child 与 parent都不要了的
        // dyConsumed = 0，因为 child 不消耗，dyUnconsumed = dy，因为 parent 也不消耗

        // 由于不好处理回弹效果，所以还是在 dispatchTouchEvent 中处理
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        when (ev?.action) {

            MotionEvent.ACTION_DOWN -> {
                valueAnimator?.cancel()
                firstDownY = ev.rawY
                moveRawY = -100f
            }

            MotionEvent.ACTION_MOVE -> {
                moveRawY = ev.rawY
                val overScrollDy = ev.rawY - firstDownY
                val isOverScroll = rankList.translationY == 0f && overScrollDy >= viewConfiguration.scaledTouchSlop
                if (isOverScroll) {
                    scaleCover(overScrollDy)
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                moveRawY = -100f
                val overScrollDy = ev.rawY - firstDownY
                val isOverScroll = rankList.translationY == 0f && overScrollDy >= viewConfiguration.scaledTouchSlop
                if (isOverScroll) {
                    resetCoverScale(overScrollDy)
                }
            }

            else -> {
                moveRawY = -100f
            }

        }

        return super.dispatchTouchEvent(ev)

    }

    private fun resetCoverScale(dy: Float) {
        valueAnimator = ValueAnimator.ofFloat(dy, 0f)
        valueAnimator?.apply {
            duration = 100
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Float
                scaleCover(value)
            }
            start()
        }
    }

    private fun scaleCover(dy: Float) {
        var scale = Math.abs(dy) / 1000f + 1f
        // 限制缩放范围在 1~1.5 之间
        scale = Math.min(1.5f, scale)
        cover.scaleX = scale
        cover.scaleY = scale
    }

}