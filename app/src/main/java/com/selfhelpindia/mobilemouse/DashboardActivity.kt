package com.selfhelpindia.mobilemouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.selfhelpindia.mobilemouse.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    lateinit var adapter: DashboardAdapter
    lateinit var binding: ActivityDashboardBinding
    lateinit var data:ArrayList<Pair<Int,String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareData()
        adapter = DashboardAdapter(data,this)
        binding.rvDashboard.adapter = adapter
        binding.rvDashboard.itemAnimator = DefaultItemAnimator()
        binding.rvDashboard.layoutManager = GridLayoutManager(this,2)

    }

    private fun prepareData() {
        data = arrayListOf(
            R.drawable.mouse to "Mouse"
        )
    }
}