package com.TaskShare.UI.Views.screens
import com.TaskShare.Models.Repositories.TSUser
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    if (user != null) {
                        TSUser.globalUser = TSUser.getFromId(user.uid)
                    }
                } else {
                    loginCompletionSource.setResult(false)
                }
            }
        return loginCompletionSource.task
    }
}