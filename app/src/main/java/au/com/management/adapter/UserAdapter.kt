package au.com.management.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import au.com.management.R
import au.com.management.activites.BaseActivity
import au.com.management.models.BaseResponse
import au.com.management.models.UpdatePasswordRequest
import au.com.management.models.User
import au.com.management.retrofit.RetrofitInstance
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAdapter(private var user: List<User>, val activity: Activity) :
    RecyclerView.Adapter<UserAdapter.UserVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserVH(view)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        val user = user[position]
        holder.bind(user)

        holder.itemView.setOnClickListener {
            startAnswer(user)
        }

        holder.tvEmail.setOnClickListener {
            startAnswer(user)
        }

        holder.tvName.setOnClickListener {
            startAnswer(user)
        }
    }

    private fun startAnswer(user: User) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Update Password")
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_udpate_password, null)

        val etPassword = view.findViewById<TextInputEditText>(R.id.etPassword)
        val tilPassword = view.findViewById<TextInputLayout>(R.id.tilPassword)

        builder.setView(view)

        builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
            val password = etPassword.text.toString().trim()
            var isValid = true
            tilPassword.error = null
            val baseActivity = activity as BaseActivity

            if (password.isEmpty()) {
                tilPassword.error = activity.getString(R.string.enter_valid_password)
                baseActivity.showToast(R.string.enter_valid_password)
                isValid = false
            }

            if (isValid) {
                val updatePasswordRequest = UpdatePasswordRequest()
                updatePasswordRequest.email = user.email
                updatePasswordRequest.password = password
                baseActivity.showLoading()

                RetrofitInstance.service.updatePassword(updatePasswordRequest)
                    .enqueue(object : Callback<BaseResponse<Any>> {
                        override fun onResponse(call: Call<BaseResponse<Any>>, response: Response<BaseResponse<Any>>) {
                            baseActivity.hideLoading()

                            if (response.isSuccessful && response.body() != null) {
                                baseActivity.showToast(R.string.password_updated_successfully)
                            } else {
                                baseActivity.showToast(response.body()?.message!!)
                            }

                            dialog.dismiss()
                        }

                        override fun onFailure(call: Call<BaseResponse<Any>>, t: Throwable) {
                            baseActivity.hideLoading()
                            baseActivity.showToast(t.message!!)
                            dialog.dismiss()
                        }
                    })
            }

            if (isValid) {
                dialog.dismiss()
            }
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
        }

        builder.show()
    }

    class UserVH(view: View) : RecyclerView.ViewHolder(view) {
        var tvName: TextView = view.findViewById(R.id.tvName)
        var tvEmail: TextView = view.findViewById(R.id.tvEmail)

        fun bind(user: User) {
            tvName.text = user.name
            tvEmail.text = user.email
        }
    }
}