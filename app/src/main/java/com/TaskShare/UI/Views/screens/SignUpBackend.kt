package com.TaskShare.UI.Views.screens

import android.widget.Toast
import com.TaskShare.Models.TSUser
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpBackend {
    private val firebaseAuth = FirebaseAuth.getInstance()
    fun validateCredentials(email: String, password: String, firstName: String, lastName: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty() && password.length > 6 && firstName.isNotEmpty() && lastName.isNotEmpty()
        //message would be field empty or password too short
    }
    fun performSignUp(email: String, password: String, firstName: String, lastName: String): Task<Boolean> {
        val loginCompletionSource = TaskCompletionSource<Boolean>()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginCompletionSource.setResult(true)
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    if (user != null) {
                        val tsUser = TSUser(user.uid)
                        tsUser.setEmail(hashSetOf(email))
                        tsUser.setPassword(hashSetOf(password))
                        tsUser.setFirstName(hashSetOf(firstName))
                        tsUser.setLastName(hashSetOf(lastName))
                        tsUser.create()
                    }
                }else{
                    loginCompletionSource.setResult(false)
                }
            }

        return loginCompletionSource.task
    }
}
