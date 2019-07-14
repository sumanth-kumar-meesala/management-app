package au.com.management.activites

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import au.com.management.R
import au.com.management.models.BaseResponse
import au.com.management.models.LoginResponse
import au.com.management.models.RegisterRequest
import au.com.management.retrofit.RetrofitInstance
import au.com.management.utils.PreferenceUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)

        findViewById<MaterialButton>(R.id.btnLogin).setOnClickListener(this)
        findViewById<MaterialButton>(R.id.btnRegister).setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnRegister -> {
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }

            R.id.btnLogin -> {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                tilEmail.error = null
                tilPassword.error = null

                if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    tilEmail.error = getString(R.string.enter_valid_email)
                } else if (password.isEmpty()) {
                    tilPassword.error = getString(R.string.enter_valid_password)
                } else {
                    showLoading()

                    val registerRequest = RegisterRequest()
                    registerRequest.email = email
                    registerRequest.password = password

                    RetrofitInstance.service.login(registerRequest)
                        .enqueue(object : Callback<BaseResponse<LoginResponse>> {
                            override fun onFailure(call: Call<BaseResponse<LoginResponse>>, t: Throwable) {
                                hideLoading()
                                showToast(t.message!!)
                            }

                            override fun onResponse(
                                call: Call<BaseResponse<LoginResponse>>,
                                response: Response<BaseResponse<LoginResponse>>
                            ) {
                                hideLoading()

                                if (response.isSuccessful && response.body() != null) {
                                    val data = response.body()?.data
                                    RetrofitInstance.initialize()
                                    RetrofitInstance.setToken(data?.token)

                                    PreferenceUtils.setToken(data?.token, this@LoginActivity)
                                    PreferenceUtils.setType(data?.type, this@LoginActivity)

                                    showHome()
                                } else {
                                    showToast(response.message())
                                }
                            }
                        })
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        showHome()
    }

    private fun showHome() {
        if (PreferenceUtils.getToken(this) != null) {
            val type = PreferenceUtils.getType(this)
            startActivity(Intent(this, ManagementActivity::class.java))

//            when (type) {
//                "Student" -> {
//                    startActivity(Intent(this, StudentActivity::class.java))
//                }
//                "Teacher" -> {
//                    startActivity(Intent(this, TeacherActivity::class.java))
//                }
//                "Management" -> {
//                    startActivity(Intent(this, ManagementActivity::class.java))
//                }
//                "Admin" -> {
//                    startActivity(Intent(this, AdminActivity::class.java))
//                }
//            }
            finish()
        }
    }
}
