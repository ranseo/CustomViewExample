package com.example.customviewexample

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.StrictMath.min

private enum class FanSpeed(val label: Int) {
    OFF(R.string.fan_off),
    LOW(R.string.fan_low),
    MEDIUM(R.string.fan_medium),
    HIGH(R.string.fan_high);

    fun next() = when(this) {
        OFF -> LOW
        LOW -> MEDIUM
        MEDIUM -> HIGH
        HIGH -> OFF
    }

}


/*
* dial indicators와 labels을 그리기 위한 부분으로써 사용.
* */
private const val RADIUS_OFFSET_LABEL = 30
private const val RADIUS_OFFSET_INDICATOR = -35

class DialView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val TAG = "DialView"

    private var radius = 0.0f // 원의 반지름
    private var fanSpeed = FanSpeed.OFF // active 선택
    // position 변수는 label과 indicator circle position을 그리기 위해 사용 된다.
    private val pointPosition: PointF = PointF(0.0f,0.0f)


    //Attributes 값을 캐싱할 로컬 변수.
    private var fanSpeedLowColor = 0
    private var fanSpeedMediumColor = 0
    private var fanSpeedMaxColor = 0

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    init {
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.DialView) {
            fanSpeedLowColor = getColor(R.styleable.DialView_fanColor1, 0)
            fanSpeedMediumColor = getColor(R.styleable.DialView_fanColor2, 0)
            fanSpeedMaxColor = getColor(R.styleable.DialView_fanColor3, 0)
        }

    }

    override fun performClick(): Boolean {
        if(super.performClick()) return true

        fanSpeed = fanSpeed.next()
        contentDescription = resources.getString(fanSpeed.label)

        invalidate()
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = (min(w,h) / 2.0 * 0.8).toFloat()
    }

    /**
     * 현재 FanSpeed 의 위치 및 dial의 반지름을 전달받아
     * text label과 current indicator를 위한 x,y 좌표를 계산한다.
     * */
    private fun PointF.computeXYForSpeed(pos:FanSpeed, radius:Float) {
        //Angle(각도)는 라디안 단위. https://ko.wikipedia.org/wiki/%EB%9D%BC%EB%94%94%EC%95%88
        val startAngle = Math.PI * (9/8.0)
        val angle = startAngle + pos.ordinal * (Math.PI/4)
        x = (radius * cos(angle)).toFloat() + width / 2
        y = (radius * sin(angle)).toFloat() + height / 2
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = when(fanSpeed) {
            FanSpeed.OFF -> Color.GRAY
            FanSpeed.LOW -> fanSpeedLowColor
            FanSpeed.MEDIUM -> fanSpeedMediumColor
            FanSpeed.HIGH -> fanSpeedMaxColor
        }

        try {
            //dial의 background color 정하기.
            canvas!!.drawCircle((width/2).toFloat(), (height/2).toFloat(), radius, paint)


            //dial 그리기
            val markerRaidus = radius + RADIUS_OFFSET_INDICATOR
            pointPosition.computeXYForSpeed(fanSpeed, markerRaidus)
            paint.color = Color.BLACK
            canvas.drawCircle(pointPosition.x, pointPosition.y, radius/12, paint)


            //Text Label 그리기
            val labelRadius = radius + RADIUS_OFFSET_LABEL
            for( i in FanSpeed.values()) {
                pointPosition.computeXYForSpeed(i,labelRadius)
                val label = resources.getString(i.label)
                canvas.drawText(label, pointPosition.x, pointPosition.y, paint)
            }
        } catch (error:Exception) {
            Log.d(TAG, "onDraw canvas error : ${error.message}")
        }

    }


}