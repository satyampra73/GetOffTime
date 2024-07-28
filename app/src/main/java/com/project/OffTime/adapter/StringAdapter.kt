package com.project.OffTime.adapter
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.project.OffTime.R
import com.project.OffTime.model.Data
import java.util.ArrayList



class StringAdapter(private val context: Context, private val list: ArrayList<Data>) : RecyclerView.Adapter<StringAdapter.StringViewHolder>() {

    inner class StringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.txtText)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_string, parent, false)
        return StringViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
        holder.textView.text = list[position].time
        holder.txtDate.text=list[position].date
    }


}