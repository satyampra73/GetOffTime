package com.project.OffTime.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.project.OffTime.R
import com.project.OffTime.db.DBHelper
import com.project.OffTime.model.EmergencyData
import java.util.ArrayList

class ContactInfoAdapter(private val context: Context, private val list: ArrayList<EmergencyData>) :
    RecyclerView.Adapter<ContactInfoAdapter.ContactInfoViewHolder>() {
    val dbHelper = DBHelper(context)

    inner class ContactInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtMobile: TextView = itemView.findViewById(R.id.txtMobile)
        val txtRelation: TextView = itemView.findViewById(R.id.txtRelation)
        val btnUpdate: AppCompatButton = itemView.findViewById(R.id.btnUpdate)
        val btnDelete: AppCompatButton = itemView.findViewById(R.id.btnDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactInfoViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.contact_info_view, parent, false)
        return ContactInfoViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ContactInfoViewHolder, position: Int) {
        holder.txtName.text = list[position].name
        holder.txtMobile.text = list[position].mobile
        holder.txtRelation.text = list[position].relation
        holder.btnUpdate.setOnClickListener {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.ask_for_number_view)
            dialog.window
                ?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            dialog.setCancelable(true)
            dialog.window!!.attributes.windowAnimations = R.style.animation
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val etMobile = dialog.findViewById<EditText>(R.id.etEnterMobile)
            val etName = dialog.findViewById<EditText>(R.id.etEnterName)
            val etRelation = dialog.findViewById<EditText>(R.id.etRelation)
            val btnSave = dialog.findViewById<AppCompatButton>(R.id.btnSave)
            val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btnCancel)

            etMobile.setText(list[position].mobile)
            etName.setText(list[position].name)
            etRelation.setText(list[position].relation)

            btnSave.setOnClickListener(View.OnClickListener {
                if (etMobile.text.toString().isEmpty()) {
                    Toast.makeText(context, "Please Enter Mobile No. ", Toast.LENGTH_SHORT)
                        .show()
                } else if (etMobile.text.toString().length != 10) {
                    Toast.makeText(
                        context,
                        "Please Enter Valid Mobile No. ",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (etName.text.toString().isEmpty()) {
                    Toast.makeText(context, "Please Enter Name ", Toast.LENGTH_SHORT)
                        .show()
                } else if (etRelation.text.toString().isEmpty()) {
                    Toast.makeText(context, "Please Enter Relation ", Toast.LENGTH_SHORT)
                        .show()
                } else {

                    dialog.dismiss()
                    dbHelper.updateData(
                        list[position].id,
                        etMobile.text.toString(),
                        etName.text.toString(),
                        etRelation.text.toString()
                    )
                    holder.txtMobile.text = etMobile.text.toString()
                    holder.txtName.text = etName.text.toString()
                    holder.txtRelation.text = etRelation.text.toString()
                    Toast.makeText(context, "Details Updated Successfully.", Toast.LENGTH_SHORT)
                        .show()
                }
            })

            btnCancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })

            dialog.show()
        }

        holder.btnDelete.setOnClickListener {
            dbHelper.deleteData(list[position].id)
            removeItem(position)
        }
    }

    private fun removeItem(pos: Int) {
        list.removeAt(pos)
        notifyItemRemoved(pos)
        notifyItemRangeChanged(pos, list.size)
    }


}