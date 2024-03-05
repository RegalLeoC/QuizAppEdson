package com.example.proyecto_edson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class FinPartida : AppCompatActivity() {


    private lateinit var imageViewLogo : ImageView
    private lateinit var textViewResultFinal : TextView
    private lateinit var textViewQuestion : TextView
    private lateinit var textViewHist : TextView
    private lateinit var textViewBonf : TextView

    private var resultValue = 0
    private var resultQuestion = 0
    private var resultHis = 0
    private var resultBonf = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fin_partida)

        imageViewLogo = findViewById(R.id.logoImageResult)
        textViewResultFinal = findViewById(R.id.textViewResultFin)
        textViewQuestion = findViewById(R.id.textViewQuest)
        textViewHist = findViewById(R.id.textViewHist)
        textViewBonf = findViewById(R.id.textViewBonf)




        validationImg(resultValue)


    }
    //Adal






    private fun validationImg(result : Int)
    {
        if (result in 1..4)
            imageViewLogo.setImageResource(R.drawable.geography_image)
        if (result in 7..9)
            imageViewLogo.setImageResource(R.drawable.mathematics_image)
        if (result in 12..14)
            imageViewLogo.setImageResource(R.drawable.img)
        else
            imageViewLogo.setImageResource(R.drawable.greek_mythology_image)
    }


}
