package com.challenge.cklapp_replica

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Created by Arthur on 14/01/2017.
 */

@RealmClass
open class User() : RealmObject() {



    @PrimaryKey
    lateinit private var login : String
    lateinit private var password : String

    open fun setLogin(userLogin : String){
        login=userLogin
    }
    open fun setPassword(userPassword : String){
        password=userPassword
    }
    open fun getLogin():String{
        return login
    }
    open fun getPassword():String{
        return password
    }
}