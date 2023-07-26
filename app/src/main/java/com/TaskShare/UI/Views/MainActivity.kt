package com.example.greetingcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val functions = FirebaseFunctions.getInstance()
//        functions.useEmulator("10.0.2.2", 5001)
//        val firestore = FirebaseFirestore.getInstance()
//        firestore.useEmulator("10.0.2.2", 8080)
//        firestore.clearPersistence()

        setContent {
           MainScreen(this)
        }

    }
}
