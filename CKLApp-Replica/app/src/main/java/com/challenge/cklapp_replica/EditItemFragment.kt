package com.challenge.cklapp_replica

import android.support.v4.app.Fragment
import android.os.Bundle
import kotlinx.android.synthetic.main.edit_item.view.*
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_groceries.*
import kotlinx.android.synthetic.main.edit_item.*


/**
 * Created by Arthur on 16/01/2017.
 */
class EditItemFragment():Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        var editView: View = inflater!!.inflate(R.layout.edit_item,container,false)

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

        //Setting back arrow button on toolbar

        editView.edit_toolbar.navigationIcon= ContextCompat.getDrawable(activity,
                R.drawable.ic_action_arrow_left)

        editView.edit_toolbar.title="Edit Item"
        editView.edit_toolbar.setTitleTextColor(Color.WHITE)
        editView.edit_toolbar.setNavigationOnClickListener {
            activity.onBackPressed()
        }
        val listener = context as Interface

        editView.edit_toolbar.edit_done.setOnClickListener {

            //listener.onDoneClicked()
        }



        return editView
    }

    interface Interface{
        fun onDoneClicked(name:String,status:Boolean)
    }


}