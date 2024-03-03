package com.example.proyecto_edson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

class Juego : AppCompatActivity() {

    private val difficulty: String? by lazy {
        intent.getStringExtra("difficulty")
    }

    private val numOptions: Int
        get() {
            return when (difficulty) {
                "Fácil" -> 2
                "Normal" -> 3
                "Difícil" -> 4
                else -> 2
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)

        val buttonContainer = findViewById<LinearLayout>(R.id.buttonContainer)

        createChoices(buttonContainer)
    }

    private fun createChoices(container : LinearLayout) {
        for (i in 0 until numOptions) {
            val button = Button(this)
            button.text = "Button ${i + 1}"
            button.setOnClickListener {
                Toast.makeText(this, "Button ${i + 1} clicked!", Toast.LENGTH_SHORT).show()
            }
            container.addView(button)
        }
    }



}