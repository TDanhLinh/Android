package vn.edu.hust.roomexamples

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
  private lateinit var launcher1: ActivityResultLauncher<Intent>
  private lateinit var launcher2: ActivityResultLauncher<Intent>
  private lateinit var studentAdapter : StudentAdapter
  private  lateinit var studentDao : StudentDao
  var pos = -1
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    studentDao = StudentDatabase.getInstance(this).studentDao()


    lifecycleScope.launch(Dispatchers.IO) {

    }


    val listview =findViewById<ListView>(R.id.list_view_students)
    lifecycleScope.launch(Dispatchers.IO) {
      studentAdapter = StudentAdapter(studentDao.getAllStudents())
      listview.adapter = studentAdapter
    }


    launcher1 = registerForActivityResult(
      ActivityResultContracts.StartActivityForResult()
    ) { it: ActivityResult ->
      if (it.resultCode == RESULT_OK) {
        val hoten = it.data?.getStringExtra("name")
        val mssv = it.data?.getStringExtra("code")
        if (!hoten.isNullOrEmpty() && !mssv.isNullOrEmpty()) {
          lifecycleScope.launch(Dispatchers.IO) {
            studentDao.insertStudent(Student(hoten=hoten, mssv=mssv))
            val intent: Intent = getIntent();
            finish();  // Kết thúc activity hiện tại
            startActivity(intent);
          }
        }
        studentAdapter.notifyDataSetChanged()
        Toast.makeText(this,"Add a student",Toast.LENGTH_LONG).show()
      } else {
        Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
      }
    }

    launcher2 = registerForActivityResult(
      ActivityResultContracts.StartActivityForResult()
    ) { it: ActivityResult ->
      if (it.resultCode == RESULT_OK) {
        val hoten = it.data?.getStringExtra("name")
        val mssv = it.data?.getStringExtra("code")
        if (!hoten.isNullOrEmpty() && !mssv.isNullOrEmpty()) {
          lifecycleScope.launch(Dispatchers.IO) {
            val student = studentDao.getStudentsByName(hoten)[0]
            studentDao.updateStudent(Student(_id = student._id,hoten=hoten, mssv=mssv))
            val intent: Intent = getIntent();
            finish();  // Kết thúc activity hiện tại
            startActivity(intent);
          }
        }
        studentAdapter.notifyDataSetChanged()
        Toast.makeText(this,"Edit a student",Toast.LENGTH_SHORT).show()
      } else {
        Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
      }
    }

    registerForContextMenu(listview)

    supportActionBar?.title = "List student"
  }

  override fun onCreateContextMenu(
    menu: ContextMenu?,
    v: View?,
    menuInfo: ContextMenu.ContextMenuInfo?
  ) {
    menuInflater.inflate(R.menu.student_delete_adit,menu)
    super.onCreateContextMenu(menu, v, menuInfo)
  }

  override fun onContextItemSelected(item: MenuItem): Boolean {
    pos = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
    when(item.itemId){
      R.id.edit_student ->{
        val student : Student = studentAdapter.students[pos]
        val intent = Intent(this, AddStudent::class.java)
        intent.putExtra("hoten",student.hoten)
        intent.putExtra("maso",student.mssv)
        intent.putExtra("action","Edit")
        launcher2.launch(intent)
      }
      R.id.delete_student ->{
        AlertDialog.Builder(this)
          .setTitle("Are you sure to delete ?")
          .setMessage(studentAdapter.students[pos].hoten)
          .setPositiveButton("YES"){
              dialogInterface: DialogInterface, i: Int ->
            lifecycleScope.launch(Dispatchers.IO) {
              studentDao.deleteByMssv(studentAdapter.students[pos].mssv)
              val intent: Intent = getIntent();
              finish();  // Kết thúc activity hiện tại
              startActivity(intent);
            }


          }
          .setNegativeButton("CANCEL",null)
          .show()
      }
    }
    return super.onContextItemSelected(item)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.student_menu_bar,menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId){
      R.id.add_student -> {
        val intent = Intent(this, AddStudent::class.java)
        intent.putExtra("action","Add")
        launcher1.launch(intent)
      }
    }
    return super.onOptionsItemSelected(item)
  }
}