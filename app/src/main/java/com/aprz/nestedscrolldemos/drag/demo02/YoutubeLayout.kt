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
    private var verticalRangeOffset = 0
    private lateinit var videoDetailView: View
    private lateinit var videoView: View
    private lateinit var videoDetailContainerView: View
    private val videoMiniWidth = (context.resources.displayMetrics.density * 150).roundToInt()


    private val viewDragHelper: ViewDragHelper = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            // 限制只有视频控件可以拖拽
            return child.id == R.id.video
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            changeVideo(changedView, top)
            changeVideoDetail(top)
            changeVideoTitle(top)
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

    private fun changeVideoTitle(top: Int) {
        val scaleY = 0.3f + 0.7f * (1 - top.toFloat() / verticalRange)
        if (scaleY <= 0.4f) {

        }
    }

    private fun changeVideoDetail(top: Int) {
        videoDetailView.pivotY = videoDetailView.measuredHeight.toFloat()
        // 让 videoDetailView 的 top 与 videoView 的 top 对齐
        videoDetailView.scaleY =
            1 - 1f * (top + videoView.translationY + (videoView.measuredHeight * (1 - videoView.scaleY))) / videoDetailView.measuredHeight
    }

    private fun changeVideo(changedView: View, top: Int) {
        // 往下拖的时候，y 按照中心点减小
        changedView.pivotY = changedView.measuredHeight.toFloat()
        changedView.pivotX = 0f

        // 将缩放范围限制到 【1，0.3】
        val scaleY = 0.3f + 0.7f * (1 - top.toFloat() / verticalRange)

        // 在 【1，0.4】 的时候，让它走到底部
        if (scaleY >= 0.4f) {
            changedView.translationY = 10f * verticalRangeOffset / 6f * (1f - scaleY)
        } else {
            // 在 【0.4， 0.3】 的时候，让它停在底部
            changedView.translationY = (1f - ((1f - scaleY) * 10f - 6f)) * verticalRangeOffset
        }

        if (changedView.translationY > verticalRangeOffset) {
            changedView.translationY = verticalRangeOffset.toFloat()
        }

        changedView.scaleY = scaleY

        // 当缩放值小于0.6的时候，对x也进行缩放
        // x 范围为 【measuredWidth, videoMiniWidth】
        if (scaleY <= 0.4f) {
            var scaleX = 1 - (0.4f - scaleY) / (0.4f - 0.3f) + videoMiniWidth * 1f / measuredWidth
            if (scaleX > 1f) {
                scaleX = 1f
            }
            changedView.scaleX = scaleX
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        videoDetailView = findViewById(R.id.video_detail)
        videoView = findViewById(R.id.video)
        videoDetailContainerView = findViewById(R.id.video_detail_container)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        verticalRange = videoDetailView.measuredHeight - videoView.measuredHeight
        verticalRangeOffset = (verticalRange / 7f).roundToInt()
        val vdcvLeft = videoDetailContainerView.left
        val vdcvRight = videoDetailContainerView.right
        videoDetailContainerView.layout(vdcvLeft, bottom - (videoView.height * 0.3f).roundToInt(), vdcvRight, bottom)
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


