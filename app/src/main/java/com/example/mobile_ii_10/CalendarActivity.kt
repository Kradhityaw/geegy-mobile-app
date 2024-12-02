package com.example.mobile_ii_10

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobile_ii_10.databinding.ActivityCalendarBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

class CalendarActivity : AppCompatActivity() {
    lateinit var bind : ActivityCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.tl.setNavigationOnClickListener {
            finish()
        }

        GlobalScope.launch(Dispatchers.IO) {
            var img = BitmapFactory.decodeStream(URL("http://10.0.2.2:5000/images/${intent.getStringExtra("img")}").openStream())
            runOnUiThread {
                bind.img.setImageBitmap(img)
            }
        }
    }
}