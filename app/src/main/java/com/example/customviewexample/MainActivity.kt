package com.example.customviewexample

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.customviewexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.albumView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                p0: View?,
                p1: Int,
                p2: Int,
                p3: Int,
                p4: Int,
                p5: Int,
                p6: Int,
                p7: Int,
                p8: Int
            ) {
                if (p0 != null) {

                    val imageBitmap = getBitmapFromView(p0)
                    binding.ivThumbnailCopy.setImageBitmap(imageBitmap)
                }

            }

        })

//        binding.btnCreate.setOnClickListener {
//            val imageBitmap = getBitmapFromView(albumThumbnail)
//            binding.ivThumbnailCopy.setImageBitmap(imageBitmap)
//        }
    }


    fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}