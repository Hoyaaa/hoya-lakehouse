package kr.ac.nsu.hakbokgs.main.map

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class RouteLineView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.RED
        strokeWidth = 10f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val path = Path()
    private var pathMeasure: PathMeasure? = null
    private var pathLength = 0f
    private var progress = 0f

    fun drawRoute(points: List<PointF>) {
        path.reset()
        path.moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) {
            path.lineTo(points[i].x, points[i].y)
        }

        pathMeasure = PathMeasure(path, false)
        pathLength = pathMeasure?.length ?: 0f

        invalidate() // 바로 초기화 (빈 선)
    }

    fun updateProgress(fraction: Float) {
        progress = fraction
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        pathMeasure?.let {
            val animatedPath = Path()
            it.getSegment(0f, pathLength * progress, animatedPath, true)
            canvas.drawPath(animatedPath, paint)
        }
    }
}
