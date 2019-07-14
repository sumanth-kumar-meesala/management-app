package au.com.management.activites

import android.os.Bundle
import androidx.core.content.ContextCompat
import au.com.management.R
import au.com.management.models.BaseResponse
import au.com.management.models.CompleteQuestion
import au.com.management.models.QuestionRequest
import au.com.management.retrofit.RetrofitInstance
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ManagementActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management)

        val pieChart = findViewById<PieChart>(R.id.pieChart)
        val pieChartAll = findViewById<PieChart>(R.id.pieChartAll)

        showLoading()

        RetrofitInstance.service.listQuestion().enqueue(object : Callback<BaseResponse<List<QuestionRequest>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<QuestionRequest>>>,
                response: Response<BaseResponse<List<QuestionRequest>>>
            ) {

                if (response.isSuccessful && response.body() != null) {
                    val totalQuestions = response.body()?.data!!.size
                    val entries: MutableList<PieEntry> = arrayListOf()
                    var yesNoCount = 0
                    var imageCount = 0
                    var optionsCount = 0
                    var shortCount = 0
                    var longCount = 0

                    for (question in response.body()?.data!!) {
                        when (question.type) {
                            "Yes or No" -> {
                                yesNoCount++
                            }
                            "Options" -> {
                                optionsCount++
                            }
                            "Image" -> {
                                imageCount++
                            }
                            "Short Question" -> {
                                shortCount++
                            }
                            "Long Question" -> {
                                longCount++
                            }
                        }
                    }

                    entries.add(PieEntry(yesNoCount.toFloat(), "Yes or No"))
                    entries.add(PieEntry(optionsCount.toFloat(), "Options"))
                    entries.add(PieEntry(imageCount.toFloat(), "Image"))
                    entries.add(PieEntry(shortCount.toFloat(), "Short Question"))
                    entries.add(PieEntry(longCount.toFloat(), "Long Question"))

                    val dataSet = PieDataSet(entries, "\nNo of questions by type")
                    dataSet.setDrawIcons(false)

                    dataSet.sliceSpace = 3f
                    dataSet.iconsOffset = MPPointF(0f, 40f)
                    dataSet.selectionShift = 5f

                    val colors: MutableList<Int> = arrayListOf()

                    for (c in ColorTemplate.VORDIPLOM_COLORS)
                        colors.add(c)

                    for (c in ColorTemplate.JOYFUL_COLORS)
                        colors.add(c)

                    for (c in ColorTemplate.COLORFUL_COLORS)
                        colors.add(c)

                    for (c in ColorTemplate.LIBERTY_COLORS)
                        colors.add(c)

                    for (c in ColorTemplate.PASTEL_COLORS)
                        colors.add(c)

                    colors.add(ColorTemplate.getHoloBlue())

                    dataSet.colors = colors
                    val data = PieData(dataSet)
                    data.setValueFormatter(PercentFormatter(pieChart))
                    data.setValueTextSize(11f)
                    data.setValueTextColor(ContextCompat.getColor(this@ManagementActivity, R.color.colorPrimary))

                    pieChart.data = data
                    pieChart.invalidate()

                    RetrofitInstance.service.listCompletedQuestion()
                        .enqueue(object : Callback<BaseResponse<List<CompleteQuestion>>> {
                            override fun onResponse(
                                call: Call<BaseResponse<List<CompleteQuestion>>>,
                                response: Response<BaseResponse<List<CompleteQuestion>>>
                            ) {
                                if (response.isSuccessful && response.body() != null) {
                                    val entries: MutableList<PieEntry> = arrayListOf()
                                    entries.add(PieEntry(totalQuestions.toFloat(), "All Questions"))
                                    entries.add(PieEntry(response.body()?.data!!.size.toFloat(), "No of attempts"))

                                    val dataSet = PieDataSet(entries, "All Questions Vs No of attempts")
                                    dataSet.setDrawIcons(false)

                                    dataSet.sliceSpace = 3f
                                    dataSet.iconsOffset = MPPointF(0f, 40f)
                                    dataSet.selectionShift = 5f

                                    val colors: MutableList<Int> = arrayListOf()

                                    for (c in ColorTemplate.VORDIPLOM_COLORS)
                                        colors.add(c)

                                    for (c in ColorTemplate.JOYFUL_COLORS)
                                        colors.add(c)

                                    for (c in ColorTemplate.COLORFUL_COLORS)
                                        colors.add(c)

                                    for (c in ColorTemplate.LIBERTY_COLORS)
                                        colors.add(c)

                                    for (c in ColorTemplate.PASTEL_COLORS)
                                        colors.add(c)

                                    colors.add(ColorTemplate.getHoloBlue())

                                    dataSet.colors = colors
                                    val data = PieData(dataSet)
                                    data.setValueFormatter(PercentFormatter(pieChart))
                                    data.setValueTextSize(11f)
                                    data.setValueTextColor(
                                        ContextCompat.getColor(
                                            this@ManagementActivity,
                                            R.color.colorPrimary
                                        )
                                    )

                                    pieChartAll.data = data
                                    pieChartAll.invalidate()
                                    hideLoading()
                                } else {
                                    hideLoading()
                                    showToast(response.message())
                                }
                            }

                            override fun onFailure(call: Call<BaseResponse<List<CompleteQuestion>>>, t: Throwable) {
                                showToast(response.body()?.message!!)
                                hideLoading()
                            }
                        })
                } else {
                    showToast(response.body()?.message!!)
                    hideLoading()
                }
            }

            override fun onFailure(call: Call<BaseResponse<List<QuestionRequest>>>, t: Throwable) {
                hideLoading()
                showToast(t.message!!)
            }
        })
    }
}
