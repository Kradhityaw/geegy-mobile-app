package com.example.mobile_ii_10

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobile_ii_10.databinding.ActivityDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

class DetailActivity : AppCompatActivity() {
    lateinit var bind: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.toolbar.setNavigationOnClickListener {
            finish()
        }

        GlobalScope.launch(Dispatchers.IO) {
            var conn = URL("http://10.0.2.2:5000/api/appointments/${intent.getStringExtra("id")}").openStream().bufferedReader().readText()
            var obj = JSONObject(conn)
            runOnUiThread {
                bind.detailDate.text = obj.getString("appointment_date")
                bind.detailName.text = "Name: ${obj.getString("patient_name")}"
                bind.detailDob.text = "DOB: ${obj.getString("patient_dob")} (${obj.getString("patient_age")} years old)"
                bind.detailAddress.text = "Address: ${obj.getString("patient_address")}"
                bind.detailNameDoc.text = "Name: ${obj.getString("doctor_name")}"
                bind.detailSpecDoc.text = "Specialist: ${obj.getString("doctor_specialist")}"
                bind.detailProblems.text = obj.getString("problems")
                bind.detailSymptoms.text = obj.getString("symptoms")
                bind.detailActions.text = obj.getString("actions")


                GlobalScope.launch(Dispatchers.IO) {
                    var img = BitmapFactory.decodeStream(URL("http://10.0.2.2:5000/images/${obj.getString("teeth_photo")}").openStream())
                    runOnUiThread {
                        bind.detailPhoto.setImageBitmap(img)
                        bind.detailPhoto.setOnClickListener {
                            startActivity(Intent(this@DetailActivity, CalendarActivity::class.java).apply {
                                putExtra("img", obj.getString("teeth_photo"))
                            })
                        }
                    }
                }
            }
        }
    }
}