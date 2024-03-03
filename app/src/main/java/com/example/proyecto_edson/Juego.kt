package com.example.proyecto_edson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class Juego : AppCompatActivity() {

    // Information from the first activity
    private val difficulty: String? by lazy {
        intent.getStringExtra("difficulty")
    }

    // Main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)

        val buttonContainer = findViewById<LinearLayout>(R.id.buttonContainer)

        createChoices(buttonContainer)
    }

    // Buttons
    private fun createChoices(container: LinearLayout) {
        // Select a random question from a random topic
        val randomTopicIndex = (0 until Topics.values().size).random()
        val randomTopic = Topics.values()[randomTopicIndex]

        val randomQuestionIndex = (0 until randomTopic.questions.size).random()
        val question = randomTopic.questions[randomQuestionIndex]

        // Adjust the number of wrong answers based on the difficulty level
        val numWrongAnswers = when (difficulty) {
            "Fácil" -> 1
            "Normal" -> 2
            "Difícil" -> 3
            else -> 1
        }

        val questionTextView = TextView(this)
        questionTextView.text = question.text
        container.addView(questionTextView)

        // Creation of buttons
        val options = mutableListOf<String>()
        options.add(question.correctAnswer)
        options.addAll(question.wrongAnswers.shuffled().take(numWrongAnswers))

        options.shuffle()
        for (option in options) {
            val button = Button(this)
            button.text = option
            button.setOnClickListener {
                // Here you can handle the click event, such as checking the answer
                checkAnswer(option, question.correctAnswer)
            }
            container.addView(button)
        }
    }

    // Check answer function
    private fun checkAnswer(selectedAnswer: String, correctAnswer: String) {
        // Here you can implement the logic to check the selected answer against the correct answer
        // For example:
        if (selectedAnswer == correctAnswer) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Incorrect. The correct answer is: $correctAnswer", Toast.LENGTH_SHORT).show()
        }
    }
}

// Enum to represent different topics
enum class Topics(val questions: List<Question>) {
    MATHEMATICS(listOf(
        Question("2 + 2 = ?", "4", listOf("3", "5", "6")),
        Question("Square root of 9", "3", listOf("4", "2", "5")),
        Question("Solve for X: X + 1 = 3", "2", listOf("1", "4", "5")),
        Question("20 / 4", "5", listOf("4", "6", "8")),
        Question("7 x 8", "56", listOf("42", "49", "64"))
    )),
    GREEK_MYTHOLOGY(listOf(
        Question("Who's the god of thunder?", "Zeus", listOf("Poseidon", "Hades", "Apollo")),
        Question("Who's the goddess of beauty?", "Aphrodite", listOf("Hera", "Athena", "Artemis")),
        Question("What was Odysseus' Island?", "Ithaca", listOf("Crete", "Sicily", "Troy")),
        Question("Greek Hero Weak in the heel?", "Achilles", listOf("Hercules", "Perseus", "Theseus")),
        Question("What animal did the Achaeans use to trick the Trojans?", "Horse", listOf("Dog", "Cat", "Cow"))
    )),
    HISTORY(listOf(
        Question("Mexican independence year", "1810", listOf("1821", "1910", "1800")),
        Question("Grito de Dolores year", "1810", listOf("1821", "1910", "1800")),
        Question("Year that WWII ended", "1945", listOf("1939", "1941", "1943")),
        Question("Which country participated in the Cake Wars?", "Mexico", listOf("France", "USA", "Germany")),
        Question("Famous French figure which threatened Europe", "Napoleon", listOf("Louis XIV", "Joan of Arc", "Marie Antoinette"))
    )),
    GEOGRAPHY(listOf(
        Question("Which country is the Amazon forest in?", "Brazil", listOf("Peru", "Colombia", "Venezuela")),
        Question("Which is the highest mountain?", "Mount Everest", listOf("K2", "Kangchenjunga", "Lhotse")),
        Question("Which state doesn't exist in Mexico?", "Baja California", listOf("Yucatan", "Quintana Roo", "Sonora")),
        Question("Which country does the Nile River exist in?", "Egypt", listOf("Sudan", "Ethiopia", "Uganda")),
        Question("Which state is the Popocatepetl in?", "Puebla", listOf("Mexico City", "Morelos", "Tlaxcala"))
    ))
}
