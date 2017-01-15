package com.challenge.cklapp_replica

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_login.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import io.realm.Realm


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
            val realm : Realm = Realm.getDefaultInstance()
            val dbUsers = realm.where(User::class.java).findAll()
            if(dbUsers.size>0){
                if (realm.where(User::class.java).equalTo("login",
                        loginView.edit_login.text.toString()).findFirst() == null) {

                    loginView.login_underline.setBackgroundColor(ContextCompat
                            .getColor(context, R.color.warm_purple))
                        loginView.login_error_text.text=resources.getString(R.string.wrong_login)
                }
                else {


                    if(realm.where(User::class.java).equalTo("login", loginView.edit_login.text
                            .toString()).findFirst().getPassword()
                            .equals(loginView.edit_password.text.toString())){

                        Toast.makeText(activity, "QUE BUGADO", Toast.LENGTH_LONG)
                                .show()
                        //login
                    }
                    else{
                        loginView.password_underline.setBackgroundColor(ContextCompat
                                .getColor(context, R.color.warm_purple))
                        loginView.password_error_text.text=resources.getString(R.string.wrong_pass)
                    }

                }
            }
            else{
                loginView.login_error_text.text=resources.getString(R.string.no_user_registered)
                loginView.login_underline.setBackgroundColor(ContextCompat
                        .getColor(context, R.color.warm_purple))
            }
        }

        return loginView
    }

    fun checkSignInStatus(loginView:View){
        if(!(loginView.edit_login.text.isEmpty())&& !(loginView.edit_password.text.isEmpty())) {
            loginView.sign_in_button.background = ContextCompat
                    .getDrawable(activity,R.drawable.round_corner_button)
            loginView.sign_in_button.isEnabled = true
        }
        else{
            loginView.sign_in_button.background = ContextCompat
                    .getDrawable(activity,R.drawable.round_corner_button_midblue)
            loginView.sign_in_button.isEnabled = false
        }
    }
}