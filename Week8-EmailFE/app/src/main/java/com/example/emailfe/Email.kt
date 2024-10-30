package com.example.emailfe

data class Email(
    val sender: String,
    val preview: String,
    val timeSent: String,
    val isStarred: Boolean
)
