package com.TaskShare.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class IncomingViewModel: ViewModel() {
    //val incomingRequestsState = mutableStateOf(IncomingRequestsViewState())

    fun getIncomingRequests(): List<IncomingViewState>{
        var request1 = IncomingViewState("Timmy", "Nook")
        var request2 = IncomingViewState("Tommy", "Nook")
        var request3 = IncomingViewState("Mario", "Mario")
        var request4 = IncomingViewState("Luigi", "Mario")
        var request5 = IncomingViewState("Bob", "Smith")
        var request6 = IncomingViewState("Someone", "Something")
        var request7 = IncomingViewState("Ash", "Ketchum")

        var incomingRequests = mutableListOf<IncomingViewState>(request1, request2, request3, request4,
            request5, request6, request7, request6, request6)

        return incomingRequests
    }
}

// States

data class IncomingRequestsViewState(
    val incomingRequests: MutableList<IncomingViewState> = mutableListOf()
)

data class IncomingViewState(
    val firstName: String = "",
    val lastName: String = ""
)
