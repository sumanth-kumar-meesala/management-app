package au.com.management.activites

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatTextView
import au.com.management.R
import au.com.management.models.BaseResponse
import au.com.management.models.QuestionRequest
import au.com.management.retrofit.RetrofitInstance
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnswerActivity : BaseActivity() {

    companion object {
        val QUESTION = "QUESTION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        val value = intent.getStringExtra(QUESTION)
        val question = Gson().fromJson(value, QuestionRequest::class.java)

        val tvQuestion = findViewById<AppCompatTextView>(R.id.tvQuestion)
        tvQuestion.text = question.question

        val llOptions = findViewById<LinearLayout>(R.id.llOptions)
        val llYesNo = findViewById<LinearLayout>(R.id.llYesNo)
        val tilShortAnswer = findViewById<TextInputLayout>(R.id.tilShortAnswer)
        val tilLongAnswer = findViewById<LinearLayout>(R.id.tilLongAnswer)
        val llImage = findViewById<LinearLayout>(R.id.llImage)

        val etShortAnswer = findViewById<TextInputEditText>(R.id.etShortAnswer)
        val etLongAnswer = findViewById<TextInputEditText>(R.id.etLongAnswer)

        val rgYesNo = findViewById<RadioGroup>(R.id.rgYesNo)
        val rgOptions = findViewById<RadioGroup>(R.id.rgOptions)

        val rbOption1 = findViewById<RadioButton>(R.id.rbOption1)
        val rbOption2 = findViewById<RadioButton>(R.id.rbOption2)
        val rbOption3 = findViewById<RadioButton>(R.id.rbOption3)
        val rbOption4 = findViewById<RadioButton>(R.id.rbOption4)

        when (question.type) {
            "Yes or No" -> {
                llYesNo.visibility = View.VISIBLE
                llOptions.visibility = View.GONE
                llImage.visibility = View.GONE
                tilShortAnswer.visibility = View.GONE
                tilLongAnswer.visibility = View.GONE
            }
            "Options" -> {
                llYesNo.visibility = View.GONE
                llOptions.visibility = View.VISIBLE
                llImage.visibility = View.GONE
                tilShortAnswer.visibility = View.GONE
                tilLongAnswer.visibility = View.GONE

                rbOption1.text = question.options!![0]
                rbOption2.text = question.options!![1]
                rbOption3.text = question.options!![2]
                rbOption4.text = question.options!![3]
            }
            "Image" -> {
                llYesNo.visibility = View.GONE
                llOptions.visibility = View.GONE
                llImage.visibility = View.VISIBLE
                tilShortAnswer.visibility = View.GONE
                tilLongAnswer.visibility = View.GONE
            }
            "Short Question" -> {
                llYesNo.visibility = View.GONE
                llOptions.visibility = View.GONE
                llImage.visibility = View.GONE
                tilShortAnswer.visibility = View.VISIBLE
                tilLongAnswer.visibility = View.GONE
            }
            "Long Question" -> {
                llYesNo.visibility = View.GONE
                llOptions.visibility = View.GONE
                llImage.visibility = View.GONE
                tilShortAnswer.visibility = View.GONE
                tilLongAnswer.visibility = View.VISIBLE
            }
        }

        findViewById<MaterialButton>(R.id.btnDone).setOnClickListener {

            question.questionId = question._id

            when (question.type) {
                "Yes or No" -> {
                    val selectedId = rgYesNo.checkedRadioButtonId
                    val radioButton = findViewById<RadioButton>(selectedId)
                    question.answer = radioButton.text.toString()
                }
                "Options" -> {
                    val selectedId = rgOptions.checkedRadioButtonId
                    val radioButton = findViewById<RadioButton>(selectedId)
                    question.answer = radioButton.text.toString()
                }
                "Image" -> {
                    question.answer = "Image"
                }
                "Short Question" -> {
                    question.answer = etShortAnswer.text.toString()
                }
                "Long Question" -> {
                    question.answer = etLongAnswer.text.toString()
                }
            }

            showLoading()

            RetrofitInstance.service.completed(question).enqueue(object : Callback<BaseResponse<Any>> {
                override fun onResponse(call: Call<BaseResponse<Any>>, response: Response<BaseResponse<Any>>) {
                    hideLoading()

                    if (response.isSuccessful && response.body() != null) {
                        finish()
                    } else {
                        showToast(response.message())
                    }
                }

                override fun onFailure(call: Call<BaseResponse<Any>>, t: Throwable) {
                    hideLoading()
                    showToast(t.message!!)
                }
            })
        }
    }
}
