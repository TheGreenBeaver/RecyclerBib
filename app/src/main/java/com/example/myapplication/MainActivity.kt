package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding
import name.ank.lab4.BibDatabase
import name.ank.lab4.BibEntry
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var database: BibDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        database = BibDatabase(InputStreamReader(resources.openRawResource(R.raw.mixed)))

        viewManager = LinearLayoutManager(this)

        val initialDataSet = ArrayList<BibEntry>()
        for (i in 0 until database.size()) {
            initialDataSet.add(database.getEntry(i))
        }

        recyclerView = binding.list
        viewAdapter = ListAdapter(initialDataSet, recyclerView, database)

        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}