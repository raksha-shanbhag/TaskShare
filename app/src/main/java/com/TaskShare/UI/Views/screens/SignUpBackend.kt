package com.TaskShare.UI.Views.screens

import com.TaskShare.Models.DataObjects.User
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

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
                        TSUsersRepository.register(User(
                                userId = user.uid,
                                firstName = firstName,
                                lastName = lastName,
                                email = email
                            ))
                        TSUsersRepository.globalUserId = user.uid
                        TSUsersRepository.setNotifToken(user.uid)
                    }
                }else{
                    loginCompletionSource.setResult(false)
                }
            }

        return loginCompletionSource.task
    }
    fun changePassword(newPassword: String): Task<Boolean> {
        val passwordChangeCompletionSource = TaskCompletionSource<Boolean>()

        val user: FirebaseUser? = firebaseAuth.currentUser
        if (user != null) {
            user.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        passwordChangeCompletionSource.setResult(true)
                    } else {
                        passwordChangeCompletionSource.setResult(false)
                    }
                }
        } else {
            // User not logged in or user reference is null
            passwordChangeCompletionSource.setResult(false)
        }

        return passwordChangeCompletionSource.task
    }
}
