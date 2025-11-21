package com.example.assignment

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SummaryActivity : AppCompatActivity() {

    private lateinit var tvDate: TextView
    private lateinit var tvTotalDuration: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        tvDate = findViewById(R.id.Date)
        tvTotalDuration = findViewById(R.id.Total)

        val date = intent.getStringExtra("date") ?: return

        tvDate.text = "Date: $date"

        lifecycleScope.launch {
            val totalDuration = withContext(Dispatchers.IO) {
                FitnessDatabase.getDatabase(this@SummaryActivity)
                    .fitnessDao()
                    .getTotalDurationByDate(date) ?: 0
            }
            tvTotalDuration.text = "Total Duration: $totalDuration min"
        }
    }
}
