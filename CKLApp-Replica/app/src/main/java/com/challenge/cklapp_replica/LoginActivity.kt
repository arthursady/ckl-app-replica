package com.challenge.cklapp_replica

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewManager
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_login)
        val loginFragment = LoginFragment().newInstance()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_view,loginFragment,"login_fragment")
                .commit()
    }

}
