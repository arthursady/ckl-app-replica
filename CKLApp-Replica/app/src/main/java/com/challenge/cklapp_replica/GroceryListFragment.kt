package com.challenge.cklapp_replica

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by Arthur on 15/01/2017.
 */
class GroceryListFragment() : Fragment() {
    lateinit private var mGroceryList: ArrayList<Item>

    fun newInstance(items: ArrayList<Item>): GroceryListFragment{
        this.setGroceryList(items)
        return this
    }

    fun setGroceryList(items:ArrayList<Item>){
        mGroceryList=items
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.groceries_list, container, false)
        val recyclerView =view.findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = GroceryListAdapter(activity, mGroceryList)
        return recyclerView
    }


}