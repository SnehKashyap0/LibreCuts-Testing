package com.tharunbirla.librecuts.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View

class VignetteOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var strength: Float = 0f
        set(value) {
            field = value.coerceIn(-1f, 1f)
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (strength == 0f || width == 0 || height == 0) return

        val cx = width / 2f
        val cy = height / 2f
        val radius = kotlin.math.sqrt(
            (cx * cx + cy * cy).toDouble()
        ).toFloat()

        val alpha = (220 * kotlin.math.abs(strength)).toInt()

        val edgeColor = if (strength > 0f) {
            Color.argb(alpha, 0, 0, 0)       // +100 = black
        } else {
            Color.argb(alpha, 255, 255, 255) // -100 = white
        }

        paint.shader = RadialGradient(
            cx,
            cy,
            radius,
            intArrayOf(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
                edgeColor
            ),
            floatArrayOf(
                0f,
                0.45f,
                1f
            ),
            Shader.TileMode.CLAMP
        )

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }
}