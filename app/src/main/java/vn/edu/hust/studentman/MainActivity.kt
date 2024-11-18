package vn.edu.hust.studentman

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    val students = mutableListOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Hoàng Cường", "SV003"),
        StudentModel("Phạm Thị Dung", "SV004"),
        StudentModel("Đỗ Minh Đức", "SV005"),
        StudentModel("Vũ Thị Hoa", "SV006"),
        StudentModel("Hoàng Văn Hải", "SV007"),
        StudentModel("Bùi Thị Hạnh", "SV008"),
        StudentModel("Đinh Văn Hùng", "SV009"),
        StudentModel("Nguyễn Thị Linh", "SV010"),
        StudentModel("Phạm Văn Long", "SV011"),
        StudentModel("Trần Thị Mai", "SV012"),
        StudentModel("Lê Thị Ngọc", "SV013"),
        StudentModel("Vũ Văn Nam", "SV014"),
        StudentModel("Hoàng Thị Phương", "SV015"),
        StudentModel("Đỗ Văn Quân", "SV016"),
        StudentModel("Nguyễn Thị Thu", "SV017"),
        StudentModel("Trần Văn Tài", "SV018"),
        StudentModel("Phạm Thị Tuyết", "SV019"),
        StudentModel("Lê Văn Vũ", "SV020")
    )

    private val studentAdapter = StudentAdapter(students) { position, action ->
        when (action) {
            StudentAdapter.Action.EDIT -> showEditStudentDialog(position)
            StudentAdapter.Action.DELETE -> showDeleteDialog(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.recycler_view_students).run {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        findViewById<Button>(R.id.btn_add_new).setOnClickListener{
            showAddStudentDialog();
        }
    }

    private fun showAddStudentDialog() {
        // Create a Dialog instance
        val dialog = Dialog(this);
        dialog.setContentView(R.layout.dialog_add_edit_student);

        // Find edit text in the dialog
        val studentNameInput = dialog.findViewById<EditText>(R.id.student_name_input);
        val studentIDInput = dialog.findViewById<EditText>(R.id.student_id_input);

        // Handle Add button
        dialog.findViewById<Button>(R.id.button_save).setOnClickListener {
            val name = studentNameInput.text.toString();
            val id = studentIDInput.text.toString();

            // Validate input
            if (name.isNotEmpty() && id.isNotEmpty()) {
                students.add(StudentModel(name, id));
                studentAdapter.notifyItemInserted(students.size - 1);
                dialog.dismiss();
            } else {
                studentNameInput.error = "Name is required";
                studentIDInput.error = "ID is required";
            }
        }

        // Handle Cancel button
        dialog.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            dialog.dismiss();
        }

        // Adjust the dialog's layout and display it
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.show();
    }

    private fun showEditStudentDialog(pos: Int) {
        // Get the student data from pos
        val student = students[pos];

        // Create a Dialog instance
        val dialog = Dialog(this);
        dialog.setContentView(R.layout.dialog_add_edit_student);

        // Find edit text
        val studentNameInput = dialog.findViewById<EditText>(R.id.student_name_input);
        val studentIDInput = dialog.findViewById<EditText>(R.id.student_id_input);

        // Set text data for edit text
        studentNameInput.setText(student.studentName);
        studentIDInput.setText(student.studentId);

        // Handle Save button
        dialog.findViewById<Button>(R.id.button_save).setOnClickListener {
            val name = studentNameInput.text.toString();
            val id = studentIDInput.text.toString();

            // Validate input
            if (name.isNotEmpty() && id.isNotEmpty()) {
                students[pos] = StudentModel(name, id);
                studentAdapter.notifyItemChanged(pos);
                dialog.dismiss();
            } else {
                studentNameInput.error = "Name is required";
                studentIDInput.error = "ID is required";
            }
        }

        // Handle Cancel button
        dialog.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            dialog.dismiss(); // Close Dialog
        }

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.show();
    }

    private fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to delete this student?")
            .setPositiveButton("Yes") { _, _ ->
                val deletedStudent = students[position]
                students.removeAt(position)
                studentAdapter.notifyItemRemoved(position)
                showUndoSnackbar(deletedStudent, position)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showUndoSnackbar(deletedStudent: StudentModel, position: Int) {
        Snackbar.make(findViewById(R.id.main), "Student deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                students.add(position, deletedStudent)
                studentAdapter.notifyItemInserted(position)
            }.show()
    }
}