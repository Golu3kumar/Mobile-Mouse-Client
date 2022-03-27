package com.selfhelpindia.mobilemouse

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DashboardAdapter(var dashboardList: ArrayList<Pair<Int, String>>, val context: Context) :
    RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.dashboard_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text = dashboardList[position].second
        holder.image.setImageResource(dashboardList[position].first)

        holder.parent.setOnClickListener {
            val intent: Intent = if (dashboardList[position].second == "Mouse"){
                Intent(context, CursorActivity::class.java)
            }else{
                Intent(context, TypingActivity::class.java)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dashboardList.size
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        val parent: LinearLayout = item.findViewById(R.id.llDashboard)
        val title: TextView = item.findViewById(R.id.tvDashboard)
        val image: ImageView = item.findViewById(R.id.ivDashboard)
    }
}