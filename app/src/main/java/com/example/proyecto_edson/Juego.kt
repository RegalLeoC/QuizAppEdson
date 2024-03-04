package com.example.proyecto_edson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class Juego : AppCompatActivity() {

    private lateinit var buttonContainer: LinearLayout
    private lateinit var questionTextView: TextView
    private lateinit var topicImageView: ImageView
    private lateinit var questionNumberTextView: TextView

    private lateinit var topics: Array<Topics>
    private var currentQuestionIndex: Int = 0
    private lateinit var questions: List<Question>

    private val questionOptionsMap: MutableMap<Int, List<String>> = mutableMapOf()

    // Information from the first activity
    private val difficulty: String? by lazy {
        intent.getStringExtra("difficulty")
    }

    // Main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)

        buttonContainer = findViewById<LinearLayout>(R.id.buttonContainer)
        questionTextView = findViewById<TextView>(R.id.questionTextView)
        topicImageView = findViewById<ImageView>(R.id.topicImageView)
        questionNumberTextView = findViewById<TextView>(R.id.questionNumberTextView)

        topics = Topics.values()

        selectRandomQuestions()
        updateQuestion()

        findViewById<Button>(R.id.nextButton).setOnClickListener {
            nextQuestion()
        }

        findViewById<Button>(R.id.prevButton).setOnClickListener {
            previousQuestion()
        }

        //createChoices(buttonContainer, questionTextView, topicImageView)
    }


    private fun selectRandomQuestions() {
        val allQuestions = topics.flatMap { it.questions }.toMutableList()
        questions = mutableListOf()

        repeat(10) {
            val randomQuestion = allQuestions.random()
            (questions as MutableList<Question>).add(randomQuestion)
            allQuestions.removeAll { it == randomQuestion }
        }
    }

    private fun updateQuestion() {
        val currentQuestion = questions[currentQuestionIndex]
        val currentTopic = topics.find { it.questions.contains(currentQuestion) } ?: Topics.MATHEMATICS
        topicImageView.setImageResource(currentTopic.imageResourceId)
        questionTextView.text = currentQuestion.text

        val questionNumberText = "${currentQuestionIndex + 1}/${questions.size}"
        questionNumberTextView.text = questionNumberText

        if (questionOptionsMap[currentQuestionIndex] == null) {
            val options = generateQuestionsOptions(currentQuestion)
            questionOptionsMap[currentQuestionIndex] = options
            createChoices(options)
        } else {
            createChoices(questionOptionsMap[currentQuestionIndex]!!)
        }
    }

    private fun generateQuestionsOptions(question: Question): List<String> {
        val options = mutableListOf<String>()
        options.add(question.correctAnswer)

        // Adjust the number of wrong answers based on the difficulty level
        val numWrongAnswers = when (difficulty) {
            "Fácil" -> 1
            "Normal" -> 2
            "Difícil" -> 3
            else -> 1
        }

        options.addAll(question.wrongAnswers.shuffled().take(numWrongAnswers))
        return options.shuffled()

    }

    // Buttons
    private fun createChoices(options: List<String>) {
        // Select a random question from a random topic

        buttonContainer.removeAllViews()
        for (option in options) {
            val button = Button(this)
            button.text = option
            button.setOnClickListener {
                // Here you can handle the click event, such as checking the answer
                checkAnswer(option, questions[currentQuestionIndex].correctAnswer)
            }

            buttonContainer.addView(button)

        }
    }

    // Check answer function
    private fun checkAnswer(selectedAnswer: String, correctAnswer: String) {
        // Here you can implement the logic to check the selected answer against the correct answer
        // For example:
        if (selectedAnswer == correctAnswer) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this,
                "Incorrect. The correct answer is: $correctAnswer",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun nextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questions.size
        updateQuestion()
    }

    // Function to move to the previous question
    private fun previousQuestion() {
        currentQuestionIndex = (currentQuestionIndex - 1 + questions.size) % questions.size
        updateQuestion()
    }
}


// Enum to represent different topics
enum class Topics(val questions: List<Question>, val imageResourceId: Int) {
    MATHEMATICS(listOf(
        Question("2 + 2 = ?", "4", listOf("3", "5", "6")),
        Question("Square root of 9", "3", listOf("4", "2", "5")),
        Question("Solve for X: X + 1 = 3", "2", listOf("1", "4", "5")),
        Question("20 / 4", "5", listOf("4", "6", "8")),
        Question("7 x 8", "56", listOf("42", "49", "64"))
    ), R.drawable.mathematics_image),
    GREEK_MYTHOLOGY(listOf(
        Question("Who's the god of thunder?", "Zeus", listOf("Poseidon", "Hades", "Apollo")),
        Question("Who's the goddess of beauty?", "Aphrodite", listOf("Hera", "Athena", "Artemis")),
        Question("What was Odysseus' Island?", "Ithaca", listOf("Crete", "Sicily", "Troy")),
        Question("Greek Hero Weak in the heel?", "Achilles", listOf("Hercules", "Perseus", "Theseus")),
        Question("What animal did the Achaeans use to trick the Trojans?", "Horse", listOf("Dog", "Cat", "Cow"))
    ), R.drawable.greek_mythology_image),
    HISTORY(listOf(
        Question("Mexican independence year", "1810", listOf("1821", "1910", "1800")),
        Question("Grito de Dolores year", "1810", listOf("1821", "1910", "1800")),
        Question("Year that WWII ended", "1945", listOf("1939", "1941", "1943")),
        Question("Which country participated in the Cake Wars?", "Mexico", listOf("France", "USA", "Germany")),
        Question("Famous French figure which threatened Europe", "Napoleon", listOf("Louis XIV", "Joan of Arc", "Marie Antoinette"))
    ), R.drawable.history_image),
    GEOGRAPHY(listOf(
        Question("Which country is the Amazon forest in?", "Brazil", listOf("Peru", "Colombia", "Venezuela")),
        Question("Which is the highest mountain?", "Mount Everest", listOf("K2", "Kangchenjunga", "Lhotse")),
        Question("Which state doesn't exist in Mexico?", "Tlaxcala", listOf("Yucatan", "Quintana Roo", "Sonora")),
        Question("Which country does the Nile River exist in?", "Egypt", listOf("Sudan", "Ethiopia", "Uganda")),
        Question("Which state is the Popocatepetl in?", "Puebla", listOf("Mexico City", "Morelos", "Tlaxcala"))
    ), R.drawable.geography_image)
}
