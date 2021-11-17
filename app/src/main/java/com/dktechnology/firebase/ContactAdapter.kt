package com.dktechnology.firebase
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import androidx.cardview.widget.CardView

//Model Class
class ContactAdapter(val items: MutableList<UserData>, ctx: Context,/*ContactClickInterface : OnUserClick*/) : RecyclerView.Adapter<ContactAdapter.ViewHolder>(){

    private var list = items
    private var context = ctx
    //private var onClick : OnUserClick = ContactClickInterface;



    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(h: ContactAdapter.ViewHolder, position: Int) {

        val currentItem = items[position]
        //h.userName.text = currentItem.number
        h.userNumber.text = currentItem.mobileNumber

        h.card.setOnClickListener {

           //onClick.OnUserClickName(currentItem.mobileNumber)

        }



       /* if(currentItem.image != null)
        h.profile.setImageBitmap(list[position].image)
        else
         h.profile.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.ic_launcher_round))*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_child,parent,false))
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
       // val userName : TextView = v.findViewById(R.id.tv_name)
        val userNumber : TextView = v.findViewById(R.id.tv_number)
        var card : CardView = v.findViewById(R.id.card);
       // val profile : ImageView = v.findViewById(R.id.iv_profile)


    }
}