package vn.edu.hust.studentman

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val students = mutableListOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Phạm Văn Cường", "SV003"),
        StudentModel("Lê Thị Dung", "SV004"),
        StudentModel("Hoàng Văn Đức", "SV005"),
        StudentModel("Nguyễn Thị Hồng", "SV006"),
        StudentModel("Trần Minh Hải", "SV007"),
        StudentModel("Đỗ Thị Hương", "SV008"),
        StudentModel("Lý Văn Hoàng", "SV009"),
        StudentModel("Vũ Thị Lan", "SV010"),
        StudentModel("Nguyễn Minh Quân", "SV011"),
        StudentModel("Trần Thị Thanh", "SV012"),
        StudentModel("Đinh Văn Phong", "SV013"),
        StudentModel("Bùi Thị Phượng", "SV014"),
        StudentModel("Ngô Văn Tùng", "SV015")
    )

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var listViewStudents: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the toolbar
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayShowTitleEnabled(false)

        listViewStudents = findViewById(R.id.list_view_students)
        val studentNames = students.map { "${it.studentName} (${it.studentId})" }

        // Set up the ListView with an ArrayAdapter
        adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, studentNames.toMutableList())
        listViewStudents.adapter = adapter

        // Handle item click (show a popup menu with options)
        listViewStudents.setOnItemClickListener { _, view, position, _ ->
            showPopupMenu(view, position)
        }
    }

    // Show a popup menu for Edit and Delete options
    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(this, view)

        // Inflate the menu with options
        popupMenu.menuInflater.inflate(R.menu.context_menu, popupMenu.menu)

        // Handle menu item clicks
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.context_edit -> {
                    showEditStudentDialog(position)
                    true
                }
                R.id.context_remove -> {
                    showDeleteDialog(position)
                    true
                }
                else -> false
            }
        }

        // Show the popup menu
        popupMenu.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_new -> {
                showAddStudentDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAddStudentDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_edit_student)

        val studentNameInput = dialog.findViewById<EditText>(R.id.student_name_input)
        val studentIDInput = dialog.findViewById<EditText>(R.id.student_id_input)

        dialog.findViewById<Button>(R.id.button_save).setOnClickListener {
            val name = studentNameInput.text.toString()
            val id = studentIDInput.text.toString()

            if (name.isNotEmpty() && id.isNotEmpty()) {
                val newStudent = StudentModel(name, id)
                students.add(newStudent)
                adapter.add("${newStudent.studentName} (${newStudent.studentId})")
                dialog.dismiss()
            } else {
                studentNameInput.error = "Name is required"
                studentIDInput.error = "ID is required"
            }
        }

        dialog.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditStudentDialog(position: Int) {
        val student = students[position]

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_edit_student)

        val studentNameInput = dialog.findViewById<EditText>(R.id.student_name_input)
        val studentIDInput = dialog.findViewById<EditText>(R.id.student_id_input)

        studentNameInput.setText(student.studentName)
        studentIDInput.setText(student.studentId)

        dialog.findViewById<Button>(R.id.button_save).setOnClickListener {
            val name = studentNameInput.text.toString()
            val id = studentIDInput.text.toString()

            if (name.isNotEmpty() && id.isNotEmpty()) {
                students[position] = StudentModel(name, id)
                adapter.insert("${name} (${id})", position)
                adapter.remove(adapter.getItem(position + 1))
                dialog.dismiss()
            } else {
                studentNameInput.error = "Name is required"
                studentIDInput.error = "ID is required"
            }
        }

        dialog.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this).setMessage("Are you sure you want to delete this student?")
            .setPositiveButton("Yes") { _, _ ->
                students.removeAt(position)
                adapter.remove(adapter.getItem(position))
            }.setNegativeButton("No", null).show()
    }
}
