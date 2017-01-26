package com.challenge.cklapp_replica

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_login.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import io.realm.Realm
import android.opengl.ETC1.getHeight
import android.view.ViewTreeObserver




/**
 * Created by Arthur on 12/01/2017.
 */
class LoginFragment() : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //Inflates the login layout for the fragment.
        val loginView:View = inflater!!.inflate(R.layout.activity_login,container,false)

        //Every time the focus on the view changes it checks to see if the user filled the
        //login and password spaces
        loginView.edit_login.setOnFocusChangeListener { view, b ->
            checkSignInStatus(loginView)
        }

        //Performs an initial check to be sure the button is disabled at first.
        checkSignInStatus(loginView)

        //Uses the viewTreeObserver to catch softKeyboard show/hide actions and check the
        //signIn status and enable/disable the button correctly.
        loginView.viewTreeObserver.addOnGlobalLayoutListener {
            checkSignInStatus(loginView)
        }


        //When the done button is pressed forces the focus to be cleared from the password field
        //and hides the softkeyboard.
        loginView.edit_password.setOnEditorActionListener { textView, i, keyEvent ->
            val inputManager = context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            when(i) {

                EditorInfo.IME_ACTION_DONE   -> {
                    loginView.clearFocus()
                    inputManager.hideSoftInputFromWindow(loginView.windowToken,
                            InputMethodManager.HIDE_NOT_ALWAYS)
                }
            }

            true
        }

        //Set the listener fot the login button.
        loginView.sign_in_button.setOnClickListener {
            val realm : Realm = Realm.getDefaultInstance()
            val dbUsers = realm.where(User::class.java).findAll()

            //If there is no register user in the db sets an error message
            if(dbUsers.size>0){

                //Checks if the specific user is in the db, if not sets an error message
                if (realm.where(User::class.java).equalTo("login",
                        loginView.edit_login.text.toString()).findFirst() == null) {

                    loginView.login_underline.setBackgroundColor(ContextCompat
                            .getColor(context, R.color.warm_purple))
                        loginView.login_error_text.text=resources.getString(R.string.wrong_login)
                }

                    //When the user is in the db, checks the password and if its correct login
                else {

                    if(realm.where(User::class.java).equalTo("login", loginView.edit_login.text
                            .toString()).findFirst().getPassword()
                            .equals(loginView.edit_password.text.toString())){

                        val intent = Intent(activity, GroceriesActivity::class.java)
                        startActivity(intent)

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

    //Checks the signin status and changes the button accordingly.
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