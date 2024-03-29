package au.com.management.utils

import android.content.Context
import android.preference.PreferenceManager

class PreferenceUtils {
    companion object {
        fun setToken(token: String?, context: Context) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("token", token).apply()
        }

        fun getToken(context: Context): String? {
            return PreferenceManager.getDefaultSharedPreferences(context).getString("token", null)
        }

        fun setType(token: String?, context: Context) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("type", token).apply()
        }

        fun getType(context: Context): String? {
            return PreferenceManager.getDefaultSharedPreferences(context).getString("type", null)
        }
    }
}