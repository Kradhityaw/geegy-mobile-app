package com.example.mobile_ii_10

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_ii_10.databinding.ActivityCalendarBinding
import com.example.mobile_ii_10.databinding.ActivityMainBinding
import com.example.mobile_ii_10.databinding.VhCardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var bind: ActivityMainBinding
    var tanggal = 0
    var bulan = 0
    var tahun = 0
    var format = "2024-4-10"
    var cancel = 0
    var success = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.homeTanggal.text = format

        bind.searchEt.addTextChangedListener {
            GlobalScope.launch(Dispatchers.IO) {
                var conn = URL("http://10.0.2.2:5000/api/appointments?date=$format&search=$it").openStream().bufferedReader().readText()
                var array = JSONArray(conn)
                runOnUiThread {
                    var adapter = object : RecyclerView.Adapter<CardVh>() {
                        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardVh {
                            var inflate = VhCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                            return CardVh(inflate)
                        }

                        override fun getItemCount(): Int {
                            return array.length()
                        }

                        override fun onBindViewHolder(holder: CardVh, position: Int) {
                            var data = array.getJSONObject(position)
                            holder.binding.cardPasien.text = data.getString("patient_name")
                            holder.binding.cardDob.text = "DOB: ${data.getString("patient_dob")} (${data.getString("patient_age")} years old)"
                            holder.binding.cardCavity.text = data.getString("problems")
                            holder.binding.cardDate.text = data.getString("appointment_date")

                            holder.itemView.setOnClickListener {
                                startActivity(Intent(this@MainActivity, DetailActivity::class.java).apply {
                                    putExtra("id", data.getString("id"))
                                })
                            }

                            if (data.getBoolean("iscancelled")) {
                                holder.binding.btnCancel.visibility = View.VISIBLE
                            }
                            else {
                                holder.binding.btnCancel.visibility = View.GONE
                            }
                        }
                    }
                    bind.homeRv.adapter = adapter
                    bind.homeRv.layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }
        }


        bind.homeLinear.setOnClickListener {
            var date = DatePickerDialog(this@MainActivity)
            date.setOnDateSetListener(object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    tanggal = dayOfMonth
                    bulan = month + 1
                    tahun = year

                    format = "${tahun}-${bulan}-${tanggal}"
                    bind.homeTanggal.text = format

                    cancel = 0
                    success = 0

                    GlobalScope.launch(Dispatchers.IO) {
                        var conn = URL("http://10.0.2.2:5000/api/appointments?date=$format").openStream().bufferedReader().readText()
                        var array = JSONArray(conn)
                        runOnUiThread {
                            for (data in 0 until array.length()) {
                                var jsonobj = array.getJSONObject(data)
                                if (jsonobj.getBoolean("iscancelled") == true) {
                                    var json = JSONArray().put(jsonobj)
                                    cancel += json.length()
                                }
                                else {
                                    var json = JSONArray().put(jsonobj)
                                    success += json.length()
                                }
                            }

                            bind.homeCancel.text = "Cancelled Appointment: $cancel"
                            bind.homeSuccess.text = "Success Appointment: $success"

                            var adapter = object : RecyclerView.Adapter<CardVh>() {
                                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardVh {
                                    var inflate = VhCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                                    return CardVh(inflate)
                                }

                                override fun getItemCount(): Int {
                                    return array.length()
                                }

                                override fun onBindViewHolder(holder: CardVh, position: Int) {
                                    var data = array.getJSONObject(position)
                                    holder.binding.cardPasien.text = data.getString("patient_name")
                                    holder.binding.cardDob.text = "DOB: ${data.getString("patient_dob")} (${data.getString("patient_age")} years old)"
                                    holder.binding.cardCavity.text = data.getString("problems")
                                    holder.binding.cardDate.text = data.getString("appointment_date")

                                    holder.itemView.setOnClickListener {
                                        startActivity(Intent(this@MainActivity, DetailActivity::class.java).apply {
                                            putExtra("id", data.getString("id"))
                                        })
                                    }

                                    if (data.getBoolean("iscancelled")) {
                                        holder.binding.btnCancel.visibility = View.VISIBLE
                                    }
                                    else {
                                        holder.binding.btnCancel.visibility = View.GONE
                                    }
                                }
                            }
                            bind.homeRv.adapter = adapter
                            bind.homeRv.layoutManager = LinearLayoutManager(this@MainActivity)
                        }
                    }
                }
            })
            date.show()
        }

        GlobalScope.launch(Dispatchers.IO) {
            var conn = URL("http://10.0.2.2:5000/api/appointments?date=$format").openStream().bufferedReader().readText()
            var array = JSONArray(conn)
            runOnUiThread {
                for (data in 0 until array.length()) {
                    var jsonobj = array.getJSONObject(data)
                    if (jsonobj.getBoolean("iscancelled") == true) {
                        var json = JSONArray().put(jsonobj)
                        cancel += json.length()
                    }
                    else {
                        var json = JSONArray().put(jsonobj)
                        success += json.length()
                    }
                }
                bind.homeCancel.text = "Cancelled Appointment: $cancel"
                bind.homeSuccess.text = "Success Appointment: $success"

                var adapter = object : RecyclerView.Adapter<CardVh>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardVh {
                        var inflate = VhCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                        return CardVh(inflate)
                    }

                    override fun getItemCount(): Int {
                        return array.length()
                    }

                    override fun onBindViewHolder(holder: CardVh, position: Int) {
                        var data = array.getJSONObject(position)
                        holder.binding.cardPasien.text = data.getString("patient_name")
                        holder.binding.cardDob.text = "DOB: ${data.getString("patient_dob")} (${data.getString("patient_age")} years old)"
                        holder.binding.cardCavity.text = data.getString("problems")
                        holder.binding.cardDate.text = data.getString("appointment_date")

                        holder.itemView.setOnClickListener {
                            startActivity(Intent(this@MainActivity, DetailActivity::class.java).apply {
                                putExtra("id", data.getString("id"))
                            })
                        }

                        if (data.getBoolean("iscancelled")) {
                            holder.binding.btnCancel.visibility = View.VISIBLE
                        }
                        else {
                            holder.binding.btnCancel.visibility = View.GONE
                        }
                    }
                }
                bind.homeRv.adapter = adapter
                bind.homeRv.layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }
    }
    class CardVh(var binding: VhCardBinding) : RecyclerView.ViewHolder(binding.root)
}