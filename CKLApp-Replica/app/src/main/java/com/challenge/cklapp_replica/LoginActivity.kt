package com.challenge.cklapp_replica


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm


class LoginActivity : AppCompatActivity() {

    lateinit var loginFragment : LoginFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_login)

        Realm.init(this)
        var realm : Realm = Realm.getDefaultInstance()


        if(!(realm.where(User::class.java).findAll().size>0)){
            var admin : User= User()
            admin.setLogin("Jones")
            admin.setPassword("12345678")
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(admin)
            realm.commitTransaction()
        }

        loginFragment = LoginFragment()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_view,loginFragment,"login_fragment")
                .commit()

        realm.close()
    }

}
