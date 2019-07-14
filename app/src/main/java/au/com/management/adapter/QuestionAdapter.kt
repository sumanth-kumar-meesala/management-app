package au.com.management.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import au.com.management.R
import au.com.management.models.QuestionRequest


class QuestionAdapter(private var questions: List<QuestionRequest>) :
    RecyclerView.Adapter<QuestionAdapter.QuestionVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return QuestionVH(view)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    override fun onBindViewHolder(holder: QuestionVH, position: Int) {
        val question = questions.get(position)
        holder.bind(question, position)
    }

    class QuestionVH(view: View) : RecyclerView.ViewHolder(view) {

        var tvQuestion: TextView = view.findViewById(R.id.tvQuestion)

        fun bind(question: QuestionRequest, position: Int) {
            tvQuestion.text = String.format("%s. %s", position + 1, question.question)
        }
    }
}