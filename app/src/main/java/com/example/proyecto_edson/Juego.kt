package com.example.proyecto_edson

import android.util.Log

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class Juego : AppCompatActivity() {

    private lateinit var buttonContainer: LinearLayout
    private lateinit var questionTextView: TextView
    private lateinit var topicImageView: ImageView
    private lateinit var questionNumberTextView: TextView
    private lateinit var hintTextView: TextView
    private lateinit var hintButton: Button

    private lateinit var topics: Array<Topics>
    private var currentQuestionIndex: Int = 0
    private lateinit var questions: List<Question>

    private var questionOptionsMap: MutableMap<Int, List<String>> = mutableMapOf()
    private var questionAnsweredMap: MutableMap<Int, Boolean> = mutableMapOf()
    private var userAnswersMap: MutableMap<Int, String?> = mutableMapOf()

    private var hintCount: Int = 5
    private var consecutiveCorrectAnswers: Int = 0

    private var wrongAnswerIndexes = mutableListOf<Int>()
    private var correctAnswerIndex = -1

    private val difficulty: String? by lazy {
        intent.getStringExtra("difficulty")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)

        buttonContainer = findViewById(R.id.buttonContainer)
        questionTextView = findViewById(R.id.questionTextView)
        topicImageView = findViewById(R.id.topicImageView)
        questionNumberTextView = findViewById(R.id.questionNumberTextView)
        hintTextView = findViewById(R.id.hintTextView)
        hintButton = findViewById(R.id.hintButton)

        hintTextView.text = hintCount.toString()

        topics = Topics.values()

        selectRandomQuestions()
        updateQuestion()

        findViewById<Button>(R.id.nextButton).setOnClickListener {
            nextQuestion()
        }

        findViewById<Button>(R.id.prevButton).setOnClickListener {
            previousQuestion()
        }

        hintButton.setOnClickListener {
            useHint()
        }

        questionOptionsMap = mutableMapOf()
        questionAnsweredMap = mutableMapOf()
        userAnswersMap = mutableMapOf()

        for (i in 0 until questions.size) {
            questionAnsweredMap[i] = false
            userAnswersMap[i] = null
        }
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
        }

        createChoices(questionOptionsMap[currentQuestionIndex]!!)
    }

    private fun generateQuestionsOptions(question: Question): List<String> {
        val options = mutableListOf<String>()
        options.add(question.correctAnswer)

        val numWrongAnswers = when (difficulty) {
            "Fácil" -> 1
            "Normal" -> 2
            "Difícil" -> 3
            else -> 1
        }

        options.addAll(question.wrongAnswers.shuffled().take(numWrongAnswers))
        return options.shuffled()
    }

    private fun createChoices(options: List<String>) {
        buttonContainer.removeAllViews()
        val isAnswered = questionAnsweredMap[currentQuestionIndex] ?: false

        for (option in options) {
            val button = Button(this)
            button.text = option
            val isSelected = userAnswersMap[currentQuestionIndex] == option

            if (isAnswered) {
                val correctAnswer = questions[currentQuestionIndex].correctAnswer
                if (option == correctAnswer) {
                    button.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
                } else if (isSelected) {
                    button.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
                }
            }

            button.setOnClickListener {
                if (!isAnswered) {
                    val correctAnswer = questions[currentQuestionIndex].correctAnswer
                    if (option == correctAnswer) {
                        button.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
                        consecutiveCorrectAnswers++
                        if (consecutiveCorrectAnswers % 2 == 0) {
                            addHint()
                        }
                    } else {
                        button.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
                        for (i in 0 until buttonContainer.childCount) {
                            val child = buttonContainer.getChildAt(i)
                            if (child is Button && child.text == correctAnswer) {
                                child.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
                                break
                            }
                        }
                        consecutiveCorrectAnswers = 0
                    }

                    questionAnsweredMap[currentQuestionIndex] = true
                    userAnswersMap[currentQuestionIndex] = option
                    disableButtons()
                }
            }
            button.isEnabled = !isAnswered
            buttonContainer.addView(button)
        }
    }

    private fun disableButtons() {
        for (i in 0 until buttonContainer.childCount) {
            val child = buttonContainer.getChildAt(i)
            if (child is Button) {
                child.isEnabled = false
            }
        }
    }

    private fun useHint() {
        if (hintCount > 0) {
            hintCount--
            hintTextView.text = hintCount.toString()
            val currentQuestion = questions[currentQuestionIndex]

            // Get indexes of wrong answers and correct answer
            if (wrongAnswerIndexes.isEmpty()) {
                questionOptionsMap[currentQuestionIndex]?.forEachIndexed { index, option ->
                    if (option == currentQuestion.correctAnswer) {
                        correctAnswerIndex = index
                    } else {
                        wrongAnswerIndexes.add(index)
                    }
                }
            }

            // If there's only one wrong answer left, turn it blue and the correct answer green
            if (wrongAnswerIndexes.size == 1) {
                val buttonToTurnBlue = buttonContainer.getChildAt(wrongAnswerIndexes[0]) as? Button
                buttonToTurnBlue?.setBackgroundColor(resources.getColor(android.R.color.holo_blue_light))
                userAnswersMap[currentQuestionIndex] = buttonToTurnBlue?.text.toString()

                val buttonToTurnGreen = buttonContainer.getChildAt(correctAnswerIndex) as? Button
                buttonToTurnGreen?.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
                buttonToTurnGreen?.isEnabled = false
                userAnswersMap[currentQuestionIndex] = buttonToTurnGreen?.text.toString()

                disableButtons()
            } else {
                // Pick a random wrong answer index to turn blue
                val randomIndex = (0 until wrongAnswerIndexes.size).random()
                val indexToTurnBlue = wrongAnswerIndexes[randomIndex]
                val buttonToTurnBlue = buttonContainer.getChildAt(indexToTurnBlue) as? Button
                buttonToTurnBlue?.setBackgroundColor(resources.getColor(android.R.color.holo_blue_light))
                buttonToTurnBlue?.isEnabled = false
                userAnswersMap[currentQuestionIndex] = buttonToTurnBlue?.text.toString()

                // Remove the index from the list to avoid turning it blue again
                wrongAnswerIndexes.removeAt(randomIndex)
            }
        }
    }

    private fun addHint() {
        hintCount++
        hintTextView.text = hintCount.toString()
    }

    private fun nextQuestion() {
        //
        Log.d("NextQuestion", "currentQuestionIndex: $currentQuestionIndex")
        if(currentQuestionIndex == 9)
        {
            Log.d("NextQuestion", "currentQuestionIndex is 10")
            viewResults()
        }
        //
        currentQuestionIndex = (currentQuestionIndex + 1) % questions.size
        updateQuestion()
    }

    private fun previousQuestion() {
        currentQuestionIndex = (currentQuestionIndex - 1 + questions.size) % questions.size
        updateQuestion()

    }


    //Prueba para ver resultados
    //
    private fun viewResults()
    {
        val intent = Intent(this, FinPartida::class.java)
        //Datos de Prueba

        intent.putExtra("valueQuestion", 15)
        intent.putExtra("valueBonf", 2)
        intent.putExtra("valueHis", -5)
        //Datos de Prueba
        startActivity(intent)

    }

    //
    //Prueba para ver resultados
}

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
