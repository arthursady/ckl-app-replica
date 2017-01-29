package com.challenge.cklapp_replica

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.Toolbar
import android.text.SpannableStringBuilder
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.edit_item.*
import kotlinx.android.synthetic.main.groceries_list.*
import kotlinx.android.synthetic.main.main_login.*
import java.util.*

class GroceriesActivity : AppCompatActivity(),GroceryListAdapter.Interface,GroceryListFragment.Interface {

    lateinit var mListFragment : GroceryListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groceries)
//        val toolbar = findViewById(R.id.toolbar_groceries_act) as Toolbar
//        setSupportActionBar(toolbar)

//        val fab = findViewById(R.id.fab) as FloatingActionButton
//        fab.setOnClickListener(View.OnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//            val editItemFragment = EditItemFragment()
//
//            supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.content_groceries,editItemFragment,"articleDetails")
//                    .addToBackStack(null)
//                    .commit()
//        })

        Realm.init(this)
        var realm : Realm = Realm.getDefaultInstance()
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
                    item.state=false
                    realm.commitTransaction()
                }


                R.id.change_to_purchased -> {
                    Toast.makeText(context, "You clicked: "+i.title, Toast.LENGTH_SHORT ).show()
                    realm.beginTransaction()
                    item.state = true
                    realm.commitTransaction()
                }

                R.id.remove_item -> {
                    Toast.makeText(context, "You clicked: "+i.title, Toast.LENGTH_SHORT ).show()
                    realm.beginTransaction()
                    var results = realm.where(Item::class.java).equalTo("name",item.name)
                            .findFirst()
                    results.deleteFromRealm()
                    realm.commitTransaction()
                }
            }

            supportFragmentManager.beginTransaction().detach(mListFragment).commit()
            supportFragmentManager.beginTransaction().attach(mListFragment).commit()

            true
        }

        realm.close()
    }

    /*Implements the Long click listener from the ArticleAdapter interface*/
    override fun onItemSelected(item: Item, view: View, context: Context) {

    }

    override fun onFloatingButtonClicked() {
        val editItemFragment = EditItemFragment()

            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content_groceries,editItemFragment,"articleDetails")
                    .addToBackStack(null)
                    .commit()
    }

    override fun onItemClicked(item: Item) {
        val editItemFragment = EditItemFragment()

        //Creates a copy of the item in order to be possible to operate with the db removal
        //operations in the activity and fragment

        editItemFragment.showExistingItem(item)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_groceries,editItemFragment,"editItem")
                .addToBackStack(null)
                .commit()


    }
}
