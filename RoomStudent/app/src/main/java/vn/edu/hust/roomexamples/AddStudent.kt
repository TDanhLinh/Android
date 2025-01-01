package vn.edu.hust.roomexamples

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class AddStudent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_student)

        val editname = findViewById<EditText>(R.id.name)
        val editmssv = findViewById<EditText>(R.id.MSSV)

        intent.getStringExtra("hoten")?.let { editname.setText(it)}
        intent.getStringExtra("maso")?.let { editmssv.setText(it) }
        val action = intent.getStringExtra("action")

        findViewById<Button>(R.id.OKE).setOnClickListener{
            val name = editname.text.toString()
            val code = editmssv.text.toString()

            intent.putExtra("name",name)
            intent.putExtra("code",code)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        findViewById<Button>(R.id.CANCEL).setOnClickListener{
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        supportActionBar?.title = action
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}