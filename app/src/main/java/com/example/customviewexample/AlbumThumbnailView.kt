package com.example.customviewexample

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes

//Coordinator Compute
typealias CC = Pair<Float, Float>

class AlbumThumbnailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val TAG = "AlbumThumbnailView"

    private var radius = 0.0f
    private var length = 0

    private val thumbnailList = listOf(
        listOf(CC(0.5F, 0.5F)),
        listOf(CC(0.35F, 0.35F), CC(0.65F, 0.65F)),
        listOf(CC(0.5F, 0.28F), CC(0.27F, 0.70F), CC(0.73F, 0.70F)),
        listOf(CC(0.25F, 0.25F), CC(0.75F, 0.25F), CC(0.25F, 0.75F), CC(0.75F, 0.75F))
    )

    private val pointPosition: PointF = PointF(0.0f, 0.0f)

    private var participants = 0


    private var bitmap: Bitmap? = null
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }


    init {
        context.withStyledAttributes(attrs, R.styleable.AlbumThumbnailView) {
            participants = getInteger(R.styleable.AlbumThumbnailView_participants, 1) - 1
        }
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.poka)

    }

    private fun PointF.computeXYForThumbnail(cc: CC) {
        x = width * cc.first
        y = height * cc.second
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        length = (w / 2)
        radius = (StrictMath.min(w, h) / 4).toFloat() * when (participants) {
            0 -> 1.5f
            1 -> 1.3f
            2 -> 1.1f
            else -> 1f
        }
        try {
            bitmap = Bitmap.createScaledBitmap(bitmap!!, w, h, true)
        } catch (error:Exception) {

        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        try {

            paint.color = Color.RED
            //canvas!!.drawCircle((width/2).toFloat(), (height/2).toFloat(), radius, paint)
            canvas!!.drawRect(
                0.toFloat(),
                0.toFloat(),
                (width / 2 + length).toFloat(),
                (width / 2 + length).toFloat(),
                paint
            )



//            canvas!!.drawRect(
//                (width * 0.25 - length/2).toFloat(),
//                (width * 0.25 - length/2).toFloat(),
//                (width * 0.25 + length/2).toFloat(),
//                (width * 0.25 + length/2).toFloat(),
//                paint
//            )

            paint.color = Color.TRANSPARENT
            for (i in 0..participants) {
                pointPosition.computeXYForThumbnail(thumbnailList[participants][i])

                val rect = Rect(
                    (pointPosition.x - length/3 ).toInt(),
                    (pointPosition.y - length/3 ).toInt(),
                    (pointPosition.x + length/3 ).toInt(),
                    (pointPosition.y + length/3 ).toInt()
                )

                //canvas!!.drawRect(rect,paint)

                //canvas!!.drawBitmap(bitmap!!, null, rect, paint)
                Log.i(TAG, "width: ${width}, height : ${height}")
                canvas!!.drawBitmap(bitmap!!.getCircledBitmap(pointPosition.x,pointPosition.y, radius), null ,rect, paint)
            }


        } catch (error: Exception) {

        }
    }

    fun Bitmap.getCircledBitmap(_x:Float, _y:Float, radius:Float): Bitmap {
        Log.i(TAG, "Bitmap.getCircledBitmap() = width: ${this.width}, height : ${this.height}")
        val output = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
//        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val rect = Rect(0, 0, this.width, this.height)
        canvas.drawARGB(0x00, 0xFF, 0xFF, 0xFF)
        paint.color = Color.BLACK
//
//        canvas.drawCircle(_x, _y, radius, paint)
//        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(this, rect, rect, paint)
        return output
    }


}