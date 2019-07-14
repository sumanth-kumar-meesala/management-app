package au.com.management.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import au.com.management.R
import au.com.management.models.User

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