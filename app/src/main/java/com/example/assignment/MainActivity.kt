package com.example.assignment

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var activityName: EditText
    private lateinit var duration: EditText
    private lateinit var calendarView: CalendarView
    private lateinit var btnAdd: Button
    private lateinit var btnFilter: Button
    private lateinit var btnShowAll: Button
    private lateinit var listView: ListView

    private var selectedDate: String = ""
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private var activities: List<Fitness> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        activityName = findViewById(R.id.activityName)
        duration = findViewById(R.id.duration)
        calendarView = findViewById(R.id.calendarView)
        btnAdd = findViewById(R.id.Add)
        btnFilter = findViewById(R.id.Filter)
        btnShowAll = findViewById(R.id.showAll)
        listView = findViewById(R.id.activityList)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        selectedDate = sdf.format(Date())


        calendarView.setOnDateChangeListener { _, year, month, day ->
            val cal = Calendar.getInstance()
            cal.set(year, month, day)
            selectedDate = sdf.format(cal.time)
        }


        btnAdd.setOnClickListener { addActivity() }

        btnFilter.setOnClickListener { filterByDate() }


        btnShowAll.setOnClickListener { loadAllActivities() }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedActivity = activities[position]
            val intent = Intent(this, SummaryActivity::class.java)
            intent.putExtra("date", selectedActivity.date)
            startActivity(intent)
        }


        loadAllActivities()
    }

    private fun addActivity() {
        val name = activityName.text.toString().trim()
        val durationText = duration.text.toString().trim()

        if (name.isEmpty() || durationText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val d = durationText.toIntOrNull()
        if (d == null || d <= 0) {
            Toast.makeText(this, "Duration must be a positive number", Toast.LENGTH_SHORT).show()
            return
        }

        val activity = Fitness(
            activityName = name,
            duration = d,
            date = selectedDate
        )

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                FitnessDatabase.getDatabase(this@MainActivity)
                    .fitnessDao()
                    .insertActivity(activity)
            }
            loadAllActivities()
        }

        activityName.text.clear()
        duration.text.clear()
    }

    private fun loadAllActivities() {
        lifecycleScope.launch {
            activities = withContext(Dispatchers.IO) {
                FitnessDatabase.getDatabase(this@MainActivity)
                    .fitnessDao()
                    .getAllActivities()
            }
            updateListView()
        }
    }

    private fun filterByDate() {
        lifecycleScope.launch {
            activities = withContext(Dispatchers.IO) {
                FitnessDatabase.getDatabase(this@MainActivity)
                    .fitnessDao()
                    .getActivityByDate(selectedDate)
            }
            updateListView()
        }
    }

    private fun updateListView() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            activities.map {
                "${it.activityName} - ${it.duration} min - ${it.date}"
            }
        )
        listView.adapter = adapter
    }
}
