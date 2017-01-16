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
    lateinit private var name : String
    private var state : Boolean = false

    open fun setName(itemName : String){
        name=itemName
    }
    open fun setState(inputState : Boolean){
        state=inputState
    }
    open fun getName():String{
        return name
    }
    open fun getState():Boolean{
        return state
    }
}