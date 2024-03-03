package com.example.proyecto_edson

data class Question (
    val text: String,
    val correctAnswer: String,
    val wrongAnswers: List<String>
){
   var answered: Boolean = false
}