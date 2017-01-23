package com.challenge.cklapp_replica

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.Realm
import kotlinx.android.synthetic.main.content_groceries.view.*
import kotlinx.android.synthetic.main.groceries_list.*
import kotlinx.android.synthetic.main.groceries_list.view.*
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
        //val recyclerView =view.findViewById(R.id.recycler_view) as RecyclerView

        view?.fab?.setOnClickListener {
            var inter = context as Interface
            inter.onFloatingButtonClicked()
        }

        view.recycler_view.layoutManager = LinearLayoutManager(activity)
        view.recycler_view.adapter = GroceryListAdapter(activity, mGroceryList)
        return view
    }

    override fun onResume() {
        super.onResume()
        Realm.init(activity)
        var realm = Realm.getDefaultInstance()
        val dbList = realm.where(Item::class.java).findAll()
        if(dbList.size != mGroceryList.size){
            mGroceryList.clear()
            mGroceryList.addAll(realm.where(Item::class.java).findAll()
                    .subList(0,realm.where(Item::class.java).findAll().size))
        }
        recycler_view.adapter.notifyDataSetChanged()
        realm.close()
    }

    interface Interface {
        fun onFloatingButtonClicked()
    }

}