package au.com.management.activites

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.com.management.R
import au.com.management.adapter.QuestionAdapter
import au.com.management.models.BaseResponse
import au.com.management.models.QuestionRequest
import au.com.management.retrofit.RetrofitInstance
import au.com.management.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val rvQuestions = findViewById<RecyclerView>(R.id.rvQuestions)
        rvQuestions.layoutManager = LinearLayoutManager(this)

        showLoading()

        RetrofitInstance.service.listQuestion().enqueue(object : Callback<BaseResponse<List<QuestionRequest>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<QuestionRequest>>>,
                response: Response<BaseResponse<List<QuestionRequest>>>
            ) {
                hideLoading()

                if (response.isSuccessful && response.body() != null) {
                    val questionAdapter = QuestionAdapter(response.body()?.data!!, this@StudentActivity)
                    rvQuestions.adapter = questionAdapter
                } else {
                    showToast(response.body()?.message!!)
                }

            }

            override fun onFailure(call: Call<BaseResponse<List<QuestionRequest>>>, t: Throwable) {
                hideLoading()
                showToast(t.message!!)
            }
        })
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
