package au.com.management

import android.app.Application
import au.com.management.retrofit.RetrofitInstance
import au.com.management.utils.PreferenceUtils

class ManagementApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitInstance.initialize()

        val token = PreferenceUtils.getToken(applicationContext)

        if (token != null) {
            RetrofitInstance.setToken(token)
        }
    }
}
