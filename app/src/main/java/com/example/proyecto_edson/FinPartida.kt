package com.example.proyecto_edson

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView


class FinPartida : AppCompatActivity() {


    private lateinit var imageViewLogo : ImageView
    private lateinit var textViewResultFinal : TextView
    private lateinit var textViewQuestion : TextView
    private lateinit var textViewHist : TextView
    private lateinit var textViewBonf : TextView
    private lateinit var buttonHome : Button

    private var resultValue: Int = 0
    private var resultQuestion: Int = 0
    private var resultHis: Int = 0
    private var resultBonf: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fin_partida)

        imageViewLogo = findViewById(R.id.logoImageResult)
        textViewResultFinal = findViewById(R.id.textViewResultFin)
        textViewQuestion = findViewById(R.id.textViewQuest)
        textViewHist = findViewById(R.id.textViewHist)
        textViewBonf = findViewById(R.id.textViewBonf)
        buttonHome = findViewById(R.id.regresarInicioButton)

        /*
        if (savedInstanceState  != null)
        {
            resultValue = savedInstanceState.getInt("resultValue")
            resultQuestion = savedInstanceState.getInt("resultQuestion")
            resultHis = savedInstanceState.getInt("resultHis")
            resultBonf = savedInstanceState.getInt("resultBonf")
        }
        */


        resultQuestion = intent.getIntExtra("valueQuestion", 0)
        resultHis = intent.getIntExtra("valueBonf", 0)
        resultBonf = intent.getIntExtra("valueHis", 0)
        resultValue = resultBonf + resultHis + resultQuestion

        validationImgAndResult(resultValue, resultQuestion, resultHis,resultBonf)

        buttonHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
    //Adal
    private fun validationImgAndResult(result : Int, resultQuestion : Int, resultHis : Int, resultBonf : Int) {
        if (result in 1..4)
            imageViewLogo.setImageResource(R.drawable.perritollorn)
        if (result in 7..9)
            imageViewLogo.setImageResource(R.drawable.knucles)
        if (result in 12..14)
            imageViewLogo.setImageResource(R.drawable.nah_id_win)
        else
            imageViewLogo.setImageResource(R.drawable.mirada100)


        textViewResultFinal.text = "${result}"
        textViewQuestion.text = "${resultQuestion}"
        textViewHist.text = "${resultHis}"
        textViewBonf.text = "${resultBonf}"
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.d("ProyectoQuizApp", "onSaveInstanceState()....")

        outState.putInt("resultValue", resultValue)
        outState.putInt("resultQuestion", resultQuestion)
        outState.putInt("resultHis", resultHis)
        outState.putInt("resultBonf", resultBonf)
    }







}
