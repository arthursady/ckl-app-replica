package com.challenge.cklapp_replica

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager



/**
 * Created by Arthur on 12/01/2017.
 */
class LoginFragment : Fragment() {

    fun newInstance() : LoginFragment{
        return this
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val loginView:View = inflater!!.inflate(R.layout.activity_login,container,false)

        loginView.login_cklLogo.setImageResource(context.resources
                .getIdentifier("ckl_logo","drawable", context.packageName))

        loginView.login_logo.setImageResource(context.resources
                .getIdentifier("login_image","drawable", context.packageName))

        loginView.password_logo.setImageResource(context.resources
                .getIdentifier("password_logo","drawable",context.packageName))

        loginView.edit_login.setOnFocusChangeListener { view, b ->
            checkSignInStatus(loginView)
        }

        checkSignInStatus(loginView)

        loginView.edit_password.setOnEditorActionListener { textView, i, keyEvent ->
            when(i) {
                EditorInfo.IME_ACTION_DONE -> {

                    loginView.clearFocus()
                    val inputManager = context
                            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                    inputManager.hideSoftInputFromWindow(loginView.windowToken,
                            InputMethodManager.HIDE_NOT_ALWAYS)
                }
            }

            true
        }

        loginView.sign_in_button.setOnClickListener {

            Toast.makeText(context, loginView.edit_login.text,
                    Toast.LENGTH_SHORT).show()
        }

        return loginView
    }

    fun checkSignInStatus(loginView:View){
        if(!(loginView.edit_login.text.isEmpty())&& !(loginView.edit_password.text.isEmpty())) {
            loginView.sign_in_button.background = resources
                    .getDrawable(R.drawable.round_corner_button)
            loginView.sign_in_button.isEnabled = true
        }
        else{
            loginView.sign_in_button.background = resources
                    .getDrawable(R.drawable.round_corner_button_midblue)
            loginView.sign_in_button.isEnabled = false
        }
    }
}