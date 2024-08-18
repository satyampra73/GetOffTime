package com.project.OffTime.utills

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.project.OffTime.activities.LoginActivity

class UserSession(context: Context?) {
    var pref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var _context: Context? = null
    var PRIVATE_MODE = 0

    init {
        try {
            _context = context
            pref = _context!!.getSharedPreferences(PREFER_NAME, PRIVATE_MODE)
            editor = pref?.edit()
        } catch (e: Exception) {
        }
    }

    fun getData(id: String?): String? {
        return pref?.getString(id, "")
    }

    fun createUserLoginSession(
        userToken: String?,
        userName: String?,
    ) {
        editor!!.putBoolean(Constents.IS_USER_LOGIN, true)
        editor!!.putString(Constents.userToken, userToken)
        editor!!.putString(Constents.userName, userName)
        editor!!.apply()
    }

    val isUserLoggedIn: Boolean
        get() = pref?.getBoolean(Constents.IS_USER_LOGIN, false) ?: false



    companion object {
        const val PREFER_NAME = "Monitor"
    }
    fun logoutUser(activity: Activity) {
        editor!!.clear()
        editor!!.commit()
        val intent = Intent(activity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("flag", "")
        activity.startActivity(intent)
    }
}