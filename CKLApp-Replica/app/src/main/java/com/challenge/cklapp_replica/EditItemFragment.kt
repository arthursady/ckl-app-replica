package com.challenge.cklapp_replica

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import kotlinx.android.synthetic.main.edit_item.view.*
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.SpannableStringBuilder
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.edit_item.*
import java.util.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.app.Activity.RESULT_OK
import java.io.FileNotFoundException
import android.util.DisplayMetrics
import java.io.ByteArrayOutputStream


/**
 * Created by Arthur on 16/01/2017.
 */
class EditItemFragment():Fragment() {

    var mItem : Item? = null
    var mRemoveItem : Boolean = false
    var mByteImage : ByteArray? = null

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

            if(mItem!!.imageByteArray != null){
               val bitmap = BitmapFactory.decodeByteArray(mItem!!.imageByteArray,
                        0,
                        mItem?.imageByteArray!!.size)

                editView.details_image.setImageBitmap(bitmap)
                editView.remove_picture.visibility = View.VISIBLE
                editView.remove_picture.isEnabled = true
                editView.add_img.visibility = View.INVISIBLE
                editView.add_img.isEnabled = false
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
                Toast.makeText(context,"Field \"Item\" is mandatory",Toast.LENGTH_SHORT).show()
            }
            else{
                //if the item already exists check if the title was altered and if not proceeds
                //normally
                if(mItem!=null){

                    if(editView.edit_item_title.text.toString()== mItem!!.name){
                        realm.beginTransaction()
                        mItem!!.state=getStatus(editView)
                        mItem!!.description=editView.edit_item_description.text.toString()
                        mItem!!.imageByteArray = mByteImage
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
                            newItem.imageByteArray = mByteImage
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
                        newItem.imageByteArray = mByteImage

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
                activity.onBackPressed()
            }
        }

        editView.add_img.setOnClickListener {

            editView.remove_picture.visibility = View.VISIBLE
            editView.remove_picture.isEnabled = true
            val intent : Intent = Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,0)
        }

        editView.remove_picture.setOnClickListener {
            add_img.visibility = View.VISIBLE
            add_img.isEnabled = true

            remove_picture.visibility = View.INVISIBLE
            remove_picture.isEnabled = false

            details_image.setImageResource(0)
            mByteImage = null
        }

        return editView
    }

    //Gets the image chosen in the phone gallery and draws it in the correct image view.
    //It also re-scales the image size to fit the device screen correctly
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode === RESULT_OK) {
            val targetUri = data?.data
            val bitmap: Bitmap
            try {
                bitmap = BitmapFactory.decodeStream(activity.contentResolver
                        .openInputStream(targetUri))

                val metrics : DisplayMetrics = DisplayMetrics()
                val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                wm.defaultDisplay.getMetrics(metrics)

                val scaleFactor = metrics.widthPixels/bitmap.width
                val bitmapScaled = Bitmap.createScaledBitmap(bitmap,metrics.widthPixels,
                        bitmap.height*scaleFactor, true)

                mByteImage = bitmapToByte(bitmapScaled)
                 //BitmapFactory.decodeByteArray(byte,0,byte.size)

                details_image.setImageBitmap(bitmapScaled)
                add_img.visibility = View.INVISIBLE
                add_img.isEnabled = false

            } catch (e: FileNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
    }

    //Realm can't edit primary keys therefore if the item title is edited the old Item has to
    //be deleted from the database.
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

    //Sets the mItem variable in case the fragment has to open an existing item for editing
    fun showExistingItem(item:Item){
       mItem = item
    }

    //Closes the softKeyboard
    fun closeKeyboard(eView: View){
        val inputManager = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(eView?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }

    //Gets which radio button is selected and returns their meaning, "false" -> buy and
    // "true"-> purchased
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

    fun bitmapToByte(bitmap: Bitmap):ByteArray{
        var stream : ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
        return stream.toByteArray()
    }

}