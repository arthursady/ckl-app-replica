package com.challenge.cklapp_replica

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Created by Arthur on 14/01/2017.
 */

@RealmClass
open class Item() : RealmObject() {

    @PrimaryKey
    lateinit var name : String
    var state : Boolean = false
    var description : String = ""
    var imageByteArray : ByteArray? = null

//    open fun setName(itemName : String){
//        name=itemName
//    }
//    open fun setState(inputState : Boolean){
//        state=inputState
//    }
//    open fun setDescription(itemDescription : String){
//        description=itemDescription
//    }

//    open fun getName():String{
//        return name
//    }
//    open fun getState():Boolean{
//        return state
//    }
//    open fun getDescription():String{
//        return name
//    }
}