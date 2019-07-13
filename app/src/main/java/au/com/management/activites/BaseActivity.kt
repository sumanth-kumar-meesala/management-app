package au.com.management.activites

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import au.com.management.R
import au.com.management.views.Loading

abstract class BaseActivity : AppCompatActivity() {

    private var loading: Loading? = null

    fun showToast(resourceId: Int) {
        Toast.makeText(this, resourceId, Toast.LENGTH_SHORT).show()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun errorToast() {
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show()
    }

    fun showLoading() {
        if (loading == null) {
            loading = Loading(this)
        }

        loading?.show()
    }

    fun hideLoading() {
        loading?.dismiss()
    }
}