package com.aprz.nestedscrolldemos.drag.demo01

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.customview.widget.ViewDragHelper
import com.aprz.nestedscrolldemos.R

/**
 * Created by jacob-wj on 2015/4/14.
 *
 * 仿头部放大效果
 */
class DragLayout1 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {
    private var mViewDragHelper: ViewDragHelper? = null
    private var mBottom: View? = null
    private var mTop: View? = null
    private var mBottomTop: Int = 0
    private var mBottomH: Int = 0
    private val mPoint = Point()

    init {
        init()
    }


    private fun init() {
        mViewDragHelper = ViewDragHelper.create(this, 1f, ViewDragCallBack())
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        mBottomTop = mBottom!!.top
        mBottomH = mBottom!!.measuredHeight
        mPoint.set(mBottom!!.left, mBottom!!.top)
    }

    override fun computeScroll() {
        if (mViewDragHelper!!.continueSettling(true)) {
            invalidate()
        }
    }

    private inner class ViewDragCallBack : ViewDragHelper.Callback() {

        override fun tryCaptureView(@NonNull child: View, pointerId: Int): Boolean {
            return child.id == R.id.bottom
        }

        /**
         * 处理水平方向上的拖动
         *
         * @param child 拖动的View
         * @param left  移动到x轴的距离
         * @param dx    建议的移动的x距离
         */
        override fun clampViewPositionHorizontal(@NonNull child: View, left: Int, dx: Int): Int {
            //两个if主要是让view在ViewGroup中
            if (left < paddingLeft) {
                return paddingLeft
            }

            return if (left > width - child.measuredWidth) {
                width - child.measuredWidth
            } else 0
        }

        override fun clampViewPositionVertical(@NonNull child: View, top: Int, dy: Int): Int {
            Log.e(TAG, "top:$top++++dy:$dy")
            val max = Math.max(mBottomTop, top)
            return Math.min(mBottomTop + mBottomH, max)
        }

        override fun onViewReleased(@NonNull releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            if (releasedChild.id == R.id.bottom) {
                mViewDragHelper!!.settleCapturedViewAt(mPoint.x, mPoint.y)
                invalidate()
            }
        }

        override fun onViewPositionChanged(@NonNull changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            if (changedView.id == R.id.bottom) {
                val offset = top - mBottomTop
                val scale = 1.0f * (offset + mTop!!.measuredHeight) / mTop!!.measuredHeight
                mTop!!.pivotX = mTop!!.width / 2.0f
                mTop!!.pivotY = 0f
                mTop!!.scaleX = scale
                mTop!!.scaleY = scale
            }
        }

        override fun onViewDragStateChanged(state: Int) {
            when (state) {
                //正在拖动过程中
                ViewDragHelper.STATE_DRAGGING,
                    //view没有被拖动，或者正在进行fling
                ViewDragHelper.STATE_IDLE -> {
                }
                //fling完毕后被放置到一个位置
                ViewDragHelper.STATE_SETTLING -> {
                }
                else -> {
                }
            }
            super.onViewDragStateChanged(state)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> mViewDragHelper!!.cancel()
            else -> {
            }
        }
        return mViewDragHelper!!.shouldInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mViewDragHelper!!.processTouchEvent(event)
        return true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mTop = findViewById(R.id.top)
        mBottom = findViewById(R.id.bottom)
    }

    companion object {
        private val TAG = "DragLayoutOne"
    }
}
