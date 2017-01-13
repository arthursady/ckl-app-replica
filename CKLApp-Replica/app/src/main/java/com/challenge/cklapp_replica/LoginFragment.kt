package com.challenge.cklapp_replica

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.view.*

/**
 * Created by Arthur on 12/01/2017.
 */
class LoginFragment() : Fragment() {

    fun newInstance() : LoginFragment{
        return this
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view:View = inflater!!.inflate(R.layout.activity_login,container,false)
        view.login_cklLogo.setImageResource(context.resources.getIdentifier("ckl_logo","drawable",
                context.packageName))
        view.login_logo.setImageResource(context.resources.getIdentifier("login_image","drawable",
                context.packageName))
        view.password_logo.setImageResource(context.resources.getIdentifier("password_logo",
                "drawable",context.packageName))

        view.sign_in_button.setOnClickListener {
            Toast.makeText(context, "You Tried to Sign In",
                    Toast.LENGTH_SHORT).show()
        }

        return view
    }



}