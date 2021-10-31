package com.dktechnology.firebase


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DataAdapter(private val Messages : ArrayList<Data>): RecyclerView.Adapter<DataAdapter.DataViewHolder>() {




//OVERRIDE METHODS
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {

        //GETTING MAIN ITEM VIEW WHICH WILL APPEAR AS CHAT
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return DataViewHolder(itemview)

    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        //MSG LIST SPECIFIC POS
        val currentItem = Messages[position]

        //USER NAME , USER MESSAGE , MSG TIMESTAMP
        holder.userName.text = currentItem.userName
        holder.userMessage.text =  currentItem.userMessage
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        holder.timeStamp.text = sdf.format(currentItem.timestamp)

    }


    override fun getItemCount(): Int {
        //SIZE OF MSG LIST
       return Messages.size
    }


    // WILL HOLD THE DATA
    class DataViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){

        //COMPONENT WHICH USED IN RECYCLE VIEW
        val userMessage : TextView = itemview.findViewById(R.id.message)
        val userName : TextView = itemview.findViewById(R.id.username)
       val timeStamp : TextView = itemview.findViewById(R.id.ts)

    }
}



