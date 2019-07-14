package au.com.management.activites

import android.Manifest.permission
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
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
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AnswerActivity : BaseActivity() {

    companion object {
        val QUESTION = "QUESTION"
        val REQUEST_CAPTURE_IMAGE = 100
        const val RC_CAMERA = 3456
    }

    lateinit var ivImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        val value = intent.getStringExtra(QUESTION)
        val question = Gson().fromJson(value, QuestionRequest::class.java)

        val tvQuestion = findViewById<AppCompatTextView>(R.id.tvQuestion)
        tvQuestion.text = question.question

        ivImage = findViewById(R.id.ivImage)
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
                        showToast(R.string.answer_submitted_successfully)
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

        findViewById<MaterialButton>(R.id.btnCapture).setOnClickListener {
            openCameraIntent()
        }
    }

    @AfterPermissionGranted(RC_CAMERA)
    private fun openCameraIntent() {
        val perms = arrayOf(permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            val pictureIntent = Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
            )
            if (pictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(
                    pictureIntent,
                    REQUEST_CAPTURE_IMAGE
                )
            }
        } else {
            EasyPermissions.requestPermissions(
                this@AnswerActivity, getString(R.string.camera_rationale),
                RC_CAMERA, *perms
            )
        }
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.extras != null) {
                val imageBitmap = data.extras!!.get("data") as Bitmap
                ivImage.setImageBitmap(imageBitmap)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
