package au.com.management.activites

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.com.management.R
import au.com.management.adapter.QuestionAdapter
import au.com.management.models.BaseResponse
import au.com.management.models.QuestionRequest
import au.com.management.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

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
                    val questionAdapter = QuestionAdapter(response.body()?.data!!)
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
}
