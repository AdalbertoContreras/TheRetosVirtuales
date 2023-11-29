package com.josejordan.bolas

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.josejordan.bolas.databinding.ActivityMainBinding
import com.thereto.vistas.Puntaje
import com.theretos.helpers.DateHelpers
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private lateinit var gameView: MyGameView
    private lateinit var exitButton: Button
    //private lateinit var pauseButton: ImageButton
    private var isPaused = false
    lateinit var highScoreTextView: TextView
    lateinit var appCompatActivity: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appCompatActivity = ActivityMainBinding.inflate(layoutInflater)

        //hide the status bar and navigation buttons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if (controller != null) {
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                controller.hide(WindowInsets.Type.systemBars())
            }
        } else {
            // Use the deprecated method for older Android versions
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }

        gameView = appCompatActivity.root.findViewById(R.id.miGameView)
        gameView.requestFocus()
        exitButton = appCompatActivity.salirButton
        //pauseButton = findViewById(R.id.pauseButton)
        highScoreTextView = appCompatActivity.root.findViewById(R.id.puntajeAltoTextView)

        val prefs = getSharedPreferences(MY_GAME_PREFS, Context.MODE_PRIVATE)
        if (prefs.contains(HIGH_SCORE_KEY)) {
            val highScore = prefs.getInt(HIGH_SCORE_KEY, 0)
            highScoreTextView.text = getString(R.string.high_score, highScore)
        } else {
            highScoreTextView.text = getString(R.string.high_score, 0)
        }
        gameView.setOnTouchListener { _, event ->
            if (!isPaused) {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    if (gameView.getGameState() == MyGameView.GameState.Waiting) {
                        gameView.setGameState(MyGameView.GameState.Playing)
                    } else if (gameView.getGameState() == MyGameView.GameState.GameOver) {
                        gameView.resetGame()
                        updateExitButtonVisibility()
                    } else {
                        gameView.performClick()
                        gameView.moveTo(event.x, event.y)
                    }
                }
            }
            true
        }

        exitButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle(getString(R.string.exit_dialog))
                    .setMessage(getString(R.string.sure))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        finishAffinity()
                        exitProcess(0) // This will close the app and all its activities
                    }
                    .setNegativeButton(getString(R.string.no), null)
                    .show()
            }
        })

/*
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(this@MainActivity)
                    .setTitle(getString(R.string.exit_dialog))
                    .setMessage(getString(R.string.sure))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        finishAffinity()
                        exitProcess(0) // This will close the app and all its activities
                    }
                    .setNegativeButton(getString(R.string.no), null)

                val alertDialog = builder.create()
                alertDialog.show()

                // Set the color of the "yes" button text to black
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)

                // Set the color of the "no" button text to black
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            }
        })

*/

        /*pauseButton.setOnClickListener {
            if (gameView.getGameState() == MyGameView.GameState.Playing) {
                gameView.setGameState(MyGameView.GameState.Paused).also {
                    gameView.pauseMediaPlayer()
                    gameView.setOnTouchListener(null) // Remove the onTouchListener when game is paused
                    pauseButton.setImageResource(R.drawable.ic_play)
                }
            } else if (gameView.getGameState() == MyGameView.GameState.Paused) {
                gameView.setGameState(MyGameView.GameState.Playing).also {
                    gameView.resumeMediaPlayer()
                    gameView.setOnTouchListener(onTouchListener) // Set the onTouchListener back when game is resumed
                    pauseButton.setImageResource(R.drawable.ic_pause)
                }
            }
            updateExitButtonVisibility()
        }*/
        gameView.onGameOver = {
            runOnUiThread {
                updateExitButtonVisibility()
            }
        }
        updateExitButtonVisibility()
    }

    // Define the onTouchListener outside of onCreate method so it can be reused
    private val onTouchListener = View.OnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (gameView.getGameState() == MyGameView.GameState.Waiting) {
                gameView.setGameState(MyGameView.GameState.Playing)
            } else if (gameView.getGameState() == MyGameView.GameState.GameOver) {
                gameView.resetGame()
                updateExitButtonVisibility()
            } else {
                gameView.performClick()
                gameView.moveTo(event.x, event.y)
            }
        }
        true
    }

    private fun updateExitButtonVisibility() {
        if (gameView.getGameState() == MyGameView.GameState.GameOver) {
            val mAuth = FirebaseAuth.getInstance()
            val currentUser = mAuth.currentUser
            if (currentUser != null) {
                registrarPuntaje(
                    currentUser.uid,
                    gameView.getScore()
                )
            } else {
                registrarPuntaje("none",
                    gameView.getScore())
            }
        } else if (gameView.getGameState() == MyGameView.GameState.Paused) {
            exitButton.visibility = View.VISIBLE
        } else {
            exitButton.visibility = View.GONE
        }

    }

    fun registrarPuntaje(userId: String, puntaje: Int) {
        // Obtén la instancia de Firestore
        val db: com.google.firebase.firestore.FirebaseFirestore =
            com.google.firebase.firestore.FirebaseFirestore.getInstance()

        // Crea un mapa con los datos que deseas almacenar
        val puntajeData: MutableMap<String, Any> = HashMap()
        puntajeData["userId"] = userId
        puntajeData["score"] = puntaje
        puntajeData["game"] = "JUEGO 3"
        puntajeData["dateCreated"] = DateHelpers.dateNow()

        // Genera una referencia única para el documento
        val puntajeRef: com.google.firebase.firestore.DocumentReference =
            db.collection("puntaje").document()

        // Agrega los datos a Firestore
        puntajeRef.set(puntajeData, com.google.firebase.firestore.SetOptions.merge())
            .addOnCompleteListener { p0 ->
                if (p0.isSuccessful()) {
                    // La operación se completó con éxito
                    // Puedes realizar acciones adicionales aquí si es necesario
                    //Application_Base.getInstance().getCurrentActivity().finish();
                    finishAffinity();
                    System.exit(0); // Esto cerrará la aplicación y todas sus actividades
                    finishAffinity()
                    exitProcess(0) // This will close the app and all its activities
                } else {
                    // Ocurrió un error al escribir en Firestore
                    val e: Exception? = p0.getException()
                    if (e != null) {
                        e.printStackTrace()
                    }
                }
            }
    }

    override fun onPause() {
        super.onPause()
        if (gameView.getGameState() == MyGameView.GameState.Playing) {
            gameView.pauseMediaPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (gameView.getGameState() == MyGameView.GameState.Playing) {
            gameView.resumeMediaPlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gameView.pauseMediaPlayer()
        gameView.releaseMediaPlayer()
    }
}

