package com.example.emailfe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabCompose: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewEmails)
        fabCompose = findViewById(R.id.fabCompose)

        val emailList = listOf(
            Email("Alice", "Don't forget our meeting at 10 AM tomorrow.", "10:00 AM", false),
            Email("Bob", "Please find attached the invoice for last month.", "11:00 AM", false),
            Email("Carol", "Let's plan for the holiday next week!", "12:00 PM", true),
            Email("David", "Your appointment is confirmed for Thursday.", "1:00 PM", false),
            Email("Eve", "Great job on the project presentation!", "2:00 PM", true),
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = EmailAdapter(emailList)
    }
}

