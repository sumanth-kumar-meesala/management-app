package au.com.management.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import au.com.management.R
import au.com.management.activites.AnswerActivity
import au.com.management.models.QuestionRequest
import com.google.gson.Gson


class QuestionAdapter(private var questions: List<QuestionRequest>, val activity: Activity) :
    RecyclerView.Adapter<QuestionAdapter.QuestionVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return QuestionVH(view)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    override fun onBindViewHolder(holder: QuestionVH, position: Int) {
        val question = questions[position]
        holder.bind(question, position)

        holder.itemView.setOnClickListener {
            startAnswer(question)
        }

        holder.tvQuestion.setOnClickListener {
            startAnswer(question)
        }
    }

    private fun startAnswer(question: QuestionRequest) {
        val intent = Intent(activity, AnswerActivity::class.java)
        intent.putExtra(AnswerActivity.QUESTION, Gson().toJson(question))
        activity.startActivity(intent)
    }

    class QuestionVH(view: View) : RecyclerView.ViewHolder(view) {
        var tvQuestion: TextView = view.findViewById(R.id.tvQuestion)

        fun bind(question: QuestionRequest, position: Int) {
            tvQuestion.text = String.format("%s. %s", position + 1, question.question)
        }
    }
}