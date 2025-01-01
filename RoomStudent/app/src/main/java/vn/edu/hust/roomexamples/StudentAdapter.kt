package vn.edu.hust.roomexamples


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class  StudentAdapter(val students: Array<Student>): BaseAdapter() {

    override fun getCount(): Int = students.size

    override fun getItem(position: Int): Any =students[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.layout_student_item, parent, false)
            viewHolder = ViewHolder(itemView)
            itemView.tag = viewHolder
        } else {
            itemView = convertView
            viewHolder = itemView.tag as ViewHolder
        }

        val student = students[position]
        viewHolder.name.text = student.hoten
        viewHolder.code.text = student.mssv

        return itemView
    }

    class ViewHolder(itemView: View) {
        val name : TextView
        val code : TextView
        init {
            name = itemView.findViewById(R.id.text_student_name)
            code = itemView.findViewById(R.id.text_student_id)
        }
    }
}