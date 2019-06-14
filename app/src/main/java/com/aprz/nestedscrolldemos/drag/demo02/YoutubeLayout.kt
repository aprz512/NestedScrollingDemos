package com.aprz.nestedscrolldemos.drag.demo02

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper
import com.aprz.nestedscrolldemos.R
import kotlin.math.roundToInt

/**
 * @author by liyunlei
 *
 * write on 2019/6/14
 *
 * Class desc:
 */
class YoutubeLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    init {
        View.inflate(context, R.layout.youtube_view, this)
    }

    var verticalRange = 0
    private lateinit var videoDetailView: View
    private lateinit var videoView: View
    private val videoMiniWidth = (context.resources.displayMetrics.density * 150).roundToInt()
    // 这 100 个距离用来做特殊缩放
    val scaleDistance = 100

    private val viewDragHelper: ViewDragHelper = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            // 限制只有视频控件可以拖拽
            return child.id == R.id.video
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            changeVideo(changedView, top)
            changedViewDetail(top)
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return verticalRange
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            // 不让横向拖动
            return 0
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            // 限制竖向拖动的范围为 【0，verticalRange】
            val min = Math.min(top, verticalRange)
            return Math.max(0, min)
        }

    })

    private fun changedViewDetail(top: Int) {
        videoDetailView.pivotY = videoDetailView.measuredHeight.toFloat()
        // 让 videoDetailView 的 top 与 videoView 的 top 对齐
        videoDetailView.scaleY = 1 - top.toFloat() / verticalRange
//            1 - 1f * (top + (videoView.measuredHeight * (1 - videoView.scaleY))) / videoDetailView.measuredHeight
    }

    private fun changeVideo(changedView: View, top: Int) {
        // 往下拖的时候，y 按照中心点减小
        changedView.pivotY = changedView.measuredHeight.toFloat()
        changedView.pivotX = 0f

        // 将缩放范围限制到 【1，0.3】
        var scaleY = 0.3f + 0.7f * (1 - top.toFloat() / verticalRange)
//        if (scaleY >= 1f) {
//            scaleY = 1f
//        }
        changedView.scaleY = scaleY

        Log.e("d", "t = ${changedView.top}")

        // 当缩放值小于0.6的时候，对x也进行缩放
        // x 范围为 【measuredWidth, videoMiniWidth】
//        if (scaleY <= 0.6f) {
//            var scaleX = 1 - (0.6f - scaleY) / (0.6f - 0.3f) + videoMiniWidth * 1f / measuredWidth
//            if (scaleX > 1f) {
//                scaleX = 1f
//            }
//            changedView.scaleX = scaleX
//        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        videoDetailView = findViewById(R.id.video_detail)
        videoView = findViewById(R.id.video)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        verticalRange = videoDetailView.measuredHeight - videoView.measuredHeight - 100
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        when (ev?.action) {
            // 之前把 ACTION_UP 写成了 ACTION_DOWN，一直拖不动...
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                viewDragHelper.cancel()
            }
        }

        return viewDragHelper.shouldInterceptTouchEvent(ev!!)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        viewDragHelper.processTouchEvent(event!!)

        // 处理下 down 事件按，让后续事件都传过来
        if (event.action == MotionEvent.ACTION_DOWN) {
            return true
        }

        return super.onTouchEvent(event)
    }

}


