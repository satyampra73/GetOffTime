package com.satyam.OffTime.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.satyam.OffTime.R
import java.util.ArrayList



class StringAdapter(private val context: Context, private val list: ArrayList<String>) : RecyclerView.Adapter<StringAdapter.StringViewHolder>() {

    inner class StringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.txtText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_string, parent, false)
        return StringViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
        holder.textView.text = list[position]
    }

}