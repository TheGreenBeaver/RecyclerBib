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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val database = BibDatabase(InputStreamReader(resources.openRawResource(R.raw.mixed)))

        val dataSet = ArrayList<BibEntry>()
        for (i in 0 until database.size()) {
            dataSet.add(database.getEntry(i))
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = ListAdapter(dataSet)

        recyclerView = binding.list

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}