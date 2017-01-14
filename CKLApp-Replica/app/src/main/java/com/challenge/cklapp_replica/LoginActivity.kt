package com.challenge.cklapp_replica

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewManager
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.main_login.*


class LoginActivity : AppCompatActivity() {

    lateinit var loginFragment : LoginFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_login)
        loginFragment = LoginFragment().newInstance()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_view,loginFragment,"login_fragment")
                .commit()
    }

}
