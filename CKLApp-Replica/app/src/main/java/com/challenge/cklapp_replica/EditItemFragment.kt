package com.challenge.cklapp_replica

import android.content.Context
import android.support.v4.app.Fragment
import android.os.Bundle
import kotlinx.android.synthetic.main.edit_item.view.*
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_groceries.*
import kotlinx.android.synthetic.main.edit_item.*
import kotlinx.android.synthetic.main.list_element.view.*
import java.util.*


/**
 * Created by Arthur on 16/01/2017.
 */
class EditItemFragment():Fragment() {

    var mItem : Item? = null
    var mRemoveItem : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Realm.init(activity)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val editView: View = inflater!!.inflate(R.layout.edit_item,container,false)

        //If the item already exists, set the views apearances and content to match the existing
        //item
        if(mItem!=null){

            //Set the title
            editView.edit_item_title.text = SpannableStringBuilder(mItem!!.name)

            //if there is a description set it too
            if(!(mItem!!.description.isEmpty())){
                editView.edit_item_description?.text = SpannableStringBuilder(mItem!!.description)
            }

            //Defines which radio button is checked and set the according background.
            if (mItem!!.state){
                editView.button_group.check(editView.radio_purchased.id)
                editView.radio_purchased.background=(ContextCompat
                        .getDrawable(activity,R.drawable.small_radius_corner_btn_mblue))
            }
            else{
                editView.button_group.check(editView.radio_buy.id)
                editView.radio_buy.background=(ContextCompat
                        .getDrawable(activity,R.drawable.small_radius_corner_btn_mblue))
            }

        }

        //When the checked item in the radio group changes, changes the background of the button
        editView.button_group.setOnCheckedChangeListener { radioGroup, i ->
             when(radioGroup.checkedRadioButtonId){
                 editView.radio_buy.id -> {
                     editView.radio_buy.background=(ContextCompat
                             .getDrawable(activity,R.drawable.small_radius_corner_btn_mblue))
                     editView.radio_purchased.setBackgroundColor(Color.TRANSPARENT)
                 }

                 editView.radio_purchased.id ->{
                     editView.radio_buy.setBackgroundColor(Color.TRANSPARENT)
                     editView.radio_purchased.background=(ContextCompat
                             .getDrawable(activity,R.drawable.small_radius_corner_btn_mblue))
                     editView.radio_buy.clearAnimation()
                 }
             }
        }

        //Setting back arrow button on toolbar***********************************
        editView.edit_toolbar.navigationIcon= ContextCompat.getDrawable(activity,
                R.drawable.ic_action_arrow_left)

        editView.edit_toolbar.title="Edit Item"
        editView.edit_toolbar.setTitleTextColor(Color.WHITE)
        editView.edit_toolbar.setNavigationOnClickListener {
            activity.onBackPressed()
        }
        //******************************************************************

        //Set the action for the done button in the toolbar
        editView.edit_toolbar.edit_done.setOnClickListener {

            val realm = Realm.getDefaultInstance()
            val newItem = Item()
            //If the title is empty, prevents further action
            if(editView.edit_item_title.text.isEmpty())
            {

            }
            else{
                //if the item already exists check if the title was altered and if not proceeds
                //normally
                if(mItem!=null){

                    if(editView.edit_item_title.text.toString()== mItem!!.name){
                        realm.beginTransaction()
                        mItem!!.state=getStatus(editView)
                        mItem!!.description=editView.edit_item_description.text.toString()
                        realm.copyToRealmOrUpdate(mItem)
                        realm.commitTransaction()
                        realm.close()
                        mRemoveItem=false
                        closeKeyboard(editView)
                    }
                    //if the item name was changed then updates it in the db accordingly
                    else{
                        if(realm.where(Item::class.java).equalTo("name",
                                editView.edit_item_title.text.toString()).findFirst() !=null){
                            Toast.makeText(context,"Item name already on the list, try another"
                            + " name, or edit the correct Item",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            newItem.name = editView.edit_item_title.text.toString()
                            newItem.state = getStatus(editView)
                            newItem.description = editView.edit_item_description.text.toString()
                            val dbList = ArrayList<Item>()
                            dbList.addAll(realm.where(Item::class.java).findAll()
                                    .subList(0,realm.where(Item::class.java).findAll().size))

                            dbList.add(newItem)
                            realm.beginTransaction()
                            realm.copyToRealmOrUpdate(dbList)
                            realm.commitTransaction()
                            realm.close()
                            mRemoveItem = true
                            closeKeyboard(editView)
                        }
                    }
                }
                //if its a new item, checks if the chosen name already exists in the db and acts
                //accordingly
                else {

                    if(realm.where(Item::class.java).equalTo("name",
                            editView.edit_item_title.text.toString()).findFirst()!=null){
                        Toast.makeText(context,"Item name already on the list, try another"
                                + " name, or edit the correct Item",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        newItem.name = editView.edit_item_title.text.toString()
                        newItem.state = getStatus(editView)
                        newItem.description = editView.edit_item_description.text.toString()

                        val dbList = ArrayList<Item>()
                        dbList.addAll(realm.where(Item::class.java).findAll()
                                .subList(0,realm.where(Item::class.java).findAll().size))

                        dbList.add(newItem)
                        realm.beginTransaction()
                        realm.copyToRealmOrUpdate(dbList)
                        realm.commitTransaction()
                        realm.close()
                        mRemoveItem = false
                        closeKeyboard(editView)
                    }
                }
            }
            activity.onBackPressed()
        }
        return editView
    }

    override fun onDestroy() {
        super.onDestroy()
        val realm = Realm.getDefaultInstance()
        if(mRemoveItem){
            realm.beginTransaction()
            realm.where(Item::class.java).equalTo("name",mItem?.name)
                    .findFirst().deleteFromRealm()
            realm.commitTransaction()
        }
    }


    interface Interface{
        fun onDoneClicked(newItem:Item? , oldItem:Item?)
    }

    fun showExistingItem(item:Item){
       mItem = item
    }

    fun closeKeyboard(eView: View){
        val inputManager = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(eView?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun getStatus(eView: View): Boolean{
        when(eView?.button_group.checkedRadioButtonId) {
            eView.radio_purchased.id -> {
                return true
            }
            else ->{
                return false
            }
        }
    }

}