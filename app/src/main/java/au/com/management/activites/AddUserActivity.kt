package au.com.management.activites

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatSpinner
import au.com.management.R
import au.com.management.models.BaseResponse
import au.com.management.models.RegisterRequest
import au.com.management.retrofit.RetrofitInstance
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddUserActivity : BaseActivity(), View.OnClickListener {

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var tilName: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var spinnerType: AppCompatSpinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        spinnerType = findViewById(R.id.spinnerType)

        tilName = findViewById(R.id.tilName)
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)

        findViewById<MaterialButton>(R.id.btnAddUser).setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnAddUser -> {
                val name = etName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                tilName.error = null
                tilEmail.error = null
                tilPassword.error = null

                if (name.isEmpty()) {
                    tilName.error = getString(R.string.enter_valid_name)
                } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    tilEmail.error = getString(R.string.enter_valid_email)
                } else if (password.isEmpty()) {
                    tilPassword.error = getString(R.string.enter_valid_password)
                } else {
                    showLoading()

                    val registerRequest = RegisterRequest()
                    registerRequest.name = name
                    registerRequest.email = email
                    registerRequest.password = password
                    registerRequest.type = spinnerType.selectedItem.toString()

                    RetrofitInstance.service.addUser(registerRequest).enqueue(object : Callback<BaseResponse<Any>> {
                        override fun onFailure(call: Call<BaseResponse<Any>>, t: Throwable) {
                            hideLoading()
                            showToast(t.message!!)
                        }

                        override fun onResponse(call: Call<BaseResponse<Any>>, response: Response<BaseResponse<Any>>) {
                            hideLoading()

                            if (response.isSuccessful && response.body() != null) {
                                showToast(response.body()?.message!!)
                                val returnIntent = Intent()
                                setResult(Activity.RESULT_OK, returnIntent)
                                finish()
                            } else {
                                showToast(response.message())
                            }
                        }
                    })
                }
            }
        }
    }
}