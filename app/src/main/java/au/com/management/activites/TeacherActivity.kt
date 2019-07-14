package au.com.management.activites

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatSpinner
import au.com.management.R
import au.com.management.models.BaseResponse
import au.com.management.models.QuestionRequest
import au.com.management.retrofit.RetrofitInstance
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_teacher.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherActivity : BaseActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var llYesNo: LinearLayout
    private lateinit var llOptions: LinearLayout
    private lateinit var etQuestion: TextInputEditText
    private lateinit var tilQuestion: TextInputLayout
    private lateinit var etOptionsAnswer: TextInputEditText
    private lateinit var tilOptionsAnswer: TextInputLayout
    private lateinit var etOption1: TextInputEditText
    private lateinit var tilOption1: TextInputLayout
    private lateinit var etOption2: TextInputEditText
    private lateinit var tilOption2: TextInputLayout
    private lateinit var etOption3: TextInputEditText
    private lateinit var tilOption3: TextInputLayout
    private lateinit var etOption4: TextInputEditText
    private lateinit var tilOption4: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        llYesNo = findViewById(R.id.llYesNo)
        llOptions = findViewById(R.id.llOptions)

        etQuestion = findViewById(R.id.etQuestion)
        tilQuestion = findViewById(R.id.tilQuestion)

        etOptionsAnswer = findViewById(R.id.etOptionsAnswer)
        tilOptionsAnswer = findViewById(R.id.tilOptionsAnswer)

        etOption1 = findViewById(R.id.etOption1)
        tilOption1 = findViewById(R.id.tilOption1)

        etOption2 = findViewById(R.id.etOption2)
        tilOption2 = findViewById(R.id.tilOption2)

        etOption3 = findViewById(R.id.etOption3)
        tilOption3 = findViewById(R.id.tilOption3)

        etOption4 = findViewById(R.id.etOption4)
        tilOption4 = findViewById(R.id.tilOption4)

        val spinnerQuestionType = findViewById<AppCompatSpinner>(R.id.spinnerQuestionType)
        spinnerQuestionType.onItemSelectedListener = this

        findViewById<MaterialButton>(R.id.btnDone).setOnClickListener {
            tilQuestion.error = null
            tilOption1.error = null
            tilOption2.error = null
            tilOption3.error = null
            tilOption4.error = null
            tilOptionsAnswer.error = null

            val question = etQuestion.text.toString().trim()

            if (question.isEmpty()) {
                tilQuestion.error = getString(R.string.enter_valid_question)
            } else {
                val position = spinnerQuestionType.selectedItemPosition
                val questionRequest = QuestionRequest()
                questionRequest.question = question
                questionRequest.type = spinnerQuestionType.selectedItem.toString()

                when (position) {
                    0 -> {
                        questionRequest.answer = spinnerAnswer.selectedItem.toString()
                    }
                    1 -> {
                        val optionAnswer = etOptionsAnswer.text.toString().trim()
                        val option1 = etOption1.text.toString().trim()
                        val option2 = etOption2.text.toString().trim()
                        val option3 = etOption3.text.toString().trim()
                        val option4 = etOption4.text.toString().trim()

                        if (optionAnswer.isEmpty()) {
                            tilOptionsAnswer.error = getString(R.string.answer_is_empty)
                        } else if (option1.isEmpty()) {
                            tilOption1.error = getString(R.string.option_is_empty)
                        } else if (option2.isEmpty()) {
                            tilOption2.error = getString(R.string.option_is_empty)
                        } else if (option3.isEmpty()) {
                            tilOption3.error = getString(R.string.option_is_empty)
                        } else if (option4.isEmpty()) {
                            tilOption4.error = getString(R.string.option_is_empty)
                        } else if (optionAnswer != option1 && optionAnswer != option2 && optionAnswer != option3 && optionAnswer != option4) {
                            tilOptionsAnswer.error = getString(R.string.answer_must_match_options)
                        } else {
                            val options: MutableList<String> = arrayListOf()
                            options.add(option1)
                            options.add(option2)
                            options.add(option3)
                            options.add(option4)

                            questionRequest.options = options
                        }
                    }
                }

                showLoading()

                RetrofitInstance.service.addQuestion(questionRequest).enqueue(object : Callback<BaseResponse<Any>> {
                    override fun onResponse(call: Call<BaseResponse<Any>>, response: Response<BaseResponse<Any>>) {
                        hideLoading()

                        if (response.isSuccessful && response.body() != null) {
                            showToast(response.body()?.message!!)
                            spinnerQuestionType.setSelection(0)
                            spinnerAnswer.setSelection(0)
                            etQuestion.text = null
                            etOption1.text = null
                            etOption2.text = null
                            etOption3.text = null
                            etOption4.text = null
                            etOptionsAnswer.text = null
                        } else {
                            showToast(response.body()?.message!!)
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

    override fun onItemSelected(adapter: AdapterView<*>?, view: View?, positon: Int, p3: Long) {
        llOptions.visibility = View.GONE
        llYesNo.visibility = View.GONE

        when (positon) {
            0 -> {
                llYesNo.visibility = View.VISIBLE
                llOptions.visibility = View.GONE
            }
            1 -> {
                llOptions.visibility = View.VISIBLE
                llYesNo.visibility = View.GONE
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}
