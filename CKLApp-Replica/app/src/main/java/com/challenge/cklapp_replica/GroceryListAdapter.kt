package com.challenge.cklapp_replica

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.list_element.view.*
import java.util.*

/**
 * Created by Arthur on 15/01/2017.
 */
class GroceryListAdapter() : RecyclerView.Adapter<GroceryListAdapter.ViewHolder>() {

    private val PURCHASED = 1        //Defining constants to use as state identifiers
    private val BUY = 0

    lateinit private var mGroceryList: ArrayList<Item>
    lateinit var mContext:Context
    lateinit var mListener : Interface
    lateinit var mLayoutInflater: LayoutInflater

    constructor(context: Context, list: ArrayList<Item>):this(){
        mContext=context
        mGroceryList=list
        mLayoutInflater= LayoutInflater.from(mContext)
        mListener = mContext as Interface
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        /*Set the data inside the views of the list fragment*/
        fun setData(item:Item){
            itemView.name.text=item.getName()
//            itemView.list_element_menu.setOnClickListener {
//                Toast.makeText(mContext, "QUE BUGADO", Toast.LENGTH_LONG)
//                        .show()
//            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var itemView : View

        if (viewType == PURCHASED){
            itemView = mLayoutInflater.inflate(R.layout.list_element,parent,false)
        }
        else{
            itemView = mLayoutInflater.inflate(R.layout.list_element_buy,parent,false)
        }

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.setData(mGroceryList[position])

        holder?.itemView?.list_element_menu?.setOnClickListener { v->
            mListener.onElementMenuClicked(mGroceryList[holder.adapterPosition],v,mContext)
        }
        /*
        holder?.itemView?.setOnLongClickListener{ v ->
            mListener.onArticleSelected(mArticleList[holder.adapterPosition], v, mContext)
            true
        }*/
    }

    override fun getItemViewType(position: Int): Int {
        if(mGroceryList[position].getState()){
            return PURCHASED
        }
        else{
            return BUY
        }
    }

    override fun getItemCount(): Int {
        return mGroceryList.size
    }

    interface Interface {
        fun onElementMenuClicked(item: Item,view: View,context: Context)
        fun onArticleSelected(item: Item,view: View,context: Context)
    }
}