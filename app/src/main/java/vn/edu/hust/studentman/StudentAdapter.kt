package vn.edu.hust.studentman

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private val students: List<StudentModel>,
    private val onItemAction: (position: Int, action: Action) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    enum class Action { EDIT, DELETE }

    // Variable to track the context menu position
    var contextMenuPosition: Int = -1

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
        val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
        val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
        val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)

        init {
            // Set context menu listener for the itemView
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            contextMenuPosition = adapterPosition
            menu?.add(adapterPosition, R.id.context_edit, 0, "Edit")
            menu?.add(adapterPosition, R.id.context_remove, 1, "Remove")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_student_item, parent, false
        )
        return StudentViewHolder(itemView)
    }

    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.textStudentName.text = student.studentName
        holder.textStudentId.text = student.studentId

        // Handle click actions
        holder.imageEdit.setOnClickListener { onItemAction(position, Action.EDIT) }
        holder.imageRemove.setOnClickListener { onItemAction(position, Action.DELETE) }
    }
}
