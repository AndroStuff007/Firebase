package com.dktechnology.firebase
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.provider.MediaStore
import android.net.Uri
import android.widget.ImageView

//Model Class
class ContactAdapter(val items : List<ContactData>,ctx: Context) : RecyclerView.Adapter<ContactAdapter.ViewHolder>(){

    private var list = items
    private var context = ctx

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(h: ContactAdapter.ViewHolder, position: Int) {

        val currentItem = items[position]
        h.userName.text = currentItem.name
        h.userNumber.text = currentItem.number



             if(list[position].image != null)
                 h.profile.setImageBitmap(list[position].image)
             else
                 h.profile.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.ic_launcher_round))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_child,parent,false))
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val userName : TextView = v.findViewById(R.id.tv_name)
        val userNumber : TextView = v.findViewById(R.id.tv_number)
        val profile : ImageView = v.findViewById(R.id.iv_profile)


    }
}