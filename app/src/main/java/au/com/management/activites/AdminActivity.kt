package au.com.management.activites

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.com.management.R
import au.com.management.adapter.UserAdapter
import au.com.management.models.BaseResponse
import au.com.management.models.User
import au.com.management.retrofit.RetrofitInstance
import au.com.management.utils.PreferenceUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdminActivity : BaseActivity() {

    lateinit var rvUsers: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        rvUsers = findViewById(R.id.rvUsers)
        rvUsers.layoutManager = LinearLayoutManager(this)

        showLoading()

        fetchUsers()

        findViewById<FloatingActionButton>(R.id.fabAddUser).setOnClickListener {
            val i = Intent(this, AddUserActivity::class.java)
            startActivityForResult(i, 4685)
        }
    }

    private fun fetchUsers() {
        RetrofitInstance.service.listUsers().enqueue(object : Callback<BaseResponse<List<User>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<User>>>,
                response: Response<BaseResponse<List<User>>>
            ) {
                hideLoading()

                if (response.isSuccessful && response.body() != null) {
                    val userAdapter = UserAdapter(response.body()?.data!!, this@AdminActivity)
                    rvUsers.adapter = userAdapter
                } else {
                    showToast(response.body()?.message!!)
                }

            }

            override fun onFailure(call: Call<BaseResponse<List<User>>>, t: Throwable) {
                hideLoading()
                showToast(t.message!!)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 4685) {
            if (resultCode == Activity.RESULT_OK) {
                fetchUsers()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_logout -> {
                PreferenceUtils.setToken(null, this)
                PreferenceUtils.setType(null, this)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        return true
    }
}
