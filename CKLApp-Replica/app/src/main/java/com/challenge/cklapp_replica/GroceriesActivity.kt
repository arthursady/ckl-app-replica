package com.challenge.cklapp_replica

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import io.realm.Realm
import java.util.*

class GroceriesActivity : AppCompatActivity(),GroceryListAdapter.Interface {

    lateinit var mListFragment : GroceryListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groceries)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener(View.OnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        })

        Realm.init(this)
        var realm : Realm = Realm.getDefaultInstance()
        if(!(realm.where(Item::class.java).findAll().size>0))
        {
            var itemList : ArrayList<Item> = ArrayList()
            val groceries = resources.getStringArray(R.array.Items)

            for(i in groceries.indices){
                itemList.add(Item())
                itemList[i].setName(groceries[i])
                realm.beginTransaction()
                realm.copyToRealmOrUpdate(itemList[i])
                realm.commitTransaction()
            }

        }

        var dbList = ArrayList<Item>()
        dbList.addAll(realm.where(Item::class.java).findAll()
                .subList(0,realm.where(Item::class.java).findAll().size))

        showListFragment(dbList)
        realm.close()
    }


    fun showListFragment(items: ArrayList<Item>){
        mListFragment = GroceryListFragment().newInstance(items)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.content_groceries,mListFragment,"groceryList")
            .commit()

    }

    /*Implements the onClick method of the ArticleAdapter interface*/
    override fun onElementMenuClicked(item: Item,view: View, context: Context) {
        var popup = PopupMenu(context, view)
        popup.menuInflater.inflate(R.menu.list_menu_popup,popup.menu)
        popup.show()

        var realm = Realm.getDefaultInstance()



        popup.setOnMenuItemClickListener { i  ->
            when(i.itemId){
                R.id.change_to_buy -> {
                    Toast.makeText(context, "You clicked: "+i.title, Toast.LENGTH_SHORT ).show()
                    realm.beginTransaction()
                    item.setState(false)
                    realm.commitTransaction()
                }


                R.id.change_to_purchased -> {
                    Toast.makeText(context, "You clicked: "+i.title, Toast.LENGTH_SHORT ).show()
                    realm.beginTransaction()
                    item.setState(true)
                    realm.commitTransaction()
                }

                R.id.remove_item -> {
                    Toast.makeText(context, "You clicked: "+i.title, Toast.LENGTH_SHORT ).show()
                }
            }

            supportFragmentManager.beginTransaction().detach(mListFragment).commit()
            supportFragmentManager.beginTransaction().attach(mListFragment).commit()

            true
        }



        realm.close()
    }

    /*Implements the Long click listener from the ArticleAdapter interface*/
    override fun onArticleSelected(item: Item, view: View, context: Context) {

    }

}
