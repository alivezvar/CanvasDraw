package com.vezvar.canvasexample

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class MyView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val path = Path()
    private val paint = Paint()
    private var pen: Bitmap? = null
    private val size = context.dp(60)
    private var penRect = Rect()


    init {
        isFocusable = true
        isFocusableInTouchMode = true
        paintSettings()
        createPen()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawPath(path, paint)

        pen?.let {
            canvas?.drawBitmap(it, null, penRect, paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return true

        val pointX = event.x
        val pointY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN ->
                path.moveTo(pointX - size, pointY - size)
            MotionEvent.ACTION_MOVE -> {
                penRect = Rect(
                    pointX.toInt() - size,
                    pointY.toInt() - size,
                    pointX.toInt(),
                    pointY.toInt()
                )
                path.lineTo(pointX - size, pointY - size)
            }
            else -> return false
        }
        invalidate()
        return true
    }


    private fun paintSettings() {
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLUE
        paint.strokeWidth = 10F
    }


    private fun createPen() {
        val drawable = ContextCompat.getDrawable(context , R.drawable.ic_pen)


        val matrix = Matrix()
        matrix.postRotate(90F)


        val bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable?.setBounds(0, 0, 50, 50)
        drawable?.draw(canvas)
        pen = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}