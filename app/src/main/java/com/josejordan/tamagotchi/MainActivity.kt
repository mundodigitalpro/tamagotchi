package com.josejordan.tamagotchi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import java.util.Timer
import java.util.TimerTask

class Tamagotchi {
    var hunger: Int = 0
    var happiness: Int = 0
    var sleepiness: Int = 0
    var health: Int = 100
    var cleanliness: Int = 100
    var state: String = "satisfecho"

    fun feed() {
        if (hunger > 70) {
            // Si el Tamagotchi está muy hambriento cuando lo alimentas, su salud disminuirá
            health = Math.max(0, health - 10)
        }
        hunger = Math.max(0, hunger - 10)
        updateState()
    }


    fun play() {
        happiness = Math.min(100, happiness + 10)
        sleepiness = Math.min(100, sleepiness + 10)
        cleanliness = Math.max(0, cleanliness - 10)
        updateState()
    }

    fun sleep() {
        sleepiness = Math.max(0, sleepiness - 10)
        updateState()
    }

    fun clean() {
        // Limpiar al Tamagotchi aumenta su felicidad, salud y limpieza
        happiness = Math.min(100, happiness + 10)
        health = Math.min(100, health + 10)
        cleanliness = Math.min(100, cleanliness + 20)
        updateState()
    }


    fun heal() {
        // Curar al Tamagotchi podría restaurar su salud al máximo
        health = 100
        updateState()
    }

    fun timePasses() {
        hunger = Math.min(100, hunger + 10)
        happiness = Math.max(0, happiness - 10)
        cleanliness = Math.max(0, cleanliness - 10)
        if(sleepiness > 50 || hunger > 70 || cleanliness < 30) {
            happiness = Math.max(0, happiness - 10)
            health = Math.max(0, health - 10)
        }
        updateState()
    }

    private fun updateState() {
        state = when {
            cleanliness < 30 -> "sucio"
            hunger > 50 -> "hambriento"
            happiness > 50 -> "feliz"
            sleepiness > 50 -> "cansado"
            health < 50 -> "enfermo"
            else -> "satisfecho"
        }
    }
}



class MainActivity : AppCompatActivity() {

    private lateinit var tamagotchi: Tamagotchi
    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tamagotchi = Tamagotchi()

        val feedButton: Button = findViewById(R.id.feedButton)
        val playButton: Button = findViewById(R.id.playButton)
        val sleepButton: Button = findViewById(R.id.sleepButton)
        val cleanButton: Button = findViewById(R.id.cleanButton)
        val healButton: Button = findViewById(R.id.healButton)

        feedButton.setOnClickListener {
            tamagotchi.feed()
            updateUI()
        }

        playButton.setOnClickListener {
            tamagotchi.play()
            updateUI()
        }

        sleepButton.setOnClickListener {
            tamagotchi.sleep()
            updateUI()
        }

        cleanButton.setOnClickListener {
            tamagotchi.clean()
            updateUI()
        }

        healButton.setOnClickListener {
            tamagotchi.heal()
            updateUI()
        }

        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    tamagotchi.timePasses()
                    updateUI()
                }
            }
        }, 0, 60000) // 60000 ms = 1 minuto
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    private fun updateUI() {
        val hungerBar: ProgressBar = findViewById(R.id.hungerBar)
        val happinessBar: ProgressBar = findViewById(R.id.happinessBar)
        val sleepinessBar: ProgressBar = findViewById(R.id.sleepinessBar)
        val cleanlinessBar: ProgressBar = findViewById(R.id.cleanlinessBar)
        val stateText: TextView = findViewById(R.id.stateText)
        val tamagotchiImage: ImageView = findViewById(R.id.tamagotchiImage)
        val healthBar: ProgressBar = findViewById(R.id.healthBar)

        hungerBar.progress = tamagotchi.hunger
        happinessBar.progress = tamagotchi.happiness
        sleepinessBar.progress = tamagotchi.sleepiness
        cleanlinessBar.progress = tamagotchi.cleanliness
        healthBar.progress = tamagotchi.health

        stateText.text = "Estado: ${tamagotchi.state}"

        val imageResource = when (tamagotchi.state) {

            "sucio" -> R.drawable.ic_dirty
            "hambriento" -> R.drawable.ic_hungry
            "feliz" -> R.drawable.ic_happy
            "cansado" -> R.drawable.ic_dissatisfied
            "enfermo" -> R.drawable.ic_sick
            else -> R.drawable.ic_satisfied
        }
        tamagotchiImage.setImageResource(imageResource)
    }

}
