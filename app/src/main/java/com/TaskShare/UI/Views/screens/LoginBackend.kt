package com.TaskShare.UI.Views.screens
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.FirebaseAuth

class LoginBackend {
    private val firebaseAuth = FirebaseAuth.getInstance()
    fun validateCredentials(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    fun performLogin(email: String, password: String): Task<Boolean> {
        val loginCompletionSource = TaskCompletionSource<Boolean>()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginCompletionSource.setResult(true)
                } else {
                    loginCompletionSource.setResult(false)
                }
            }
        return loginCompletionSource.task
    }
}