package com.TaskShare.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class OutgoingViewModel: ViewModel() {
    //val outgoingRequestsState = mutableStateOf(OutgoingRequestsViewState())

    fun getOutgoingRequests(): List<OutgoingViewState>{
        var request1 = OutgoingViewState("Timmy", "Nook")
        var request2 = OutgoingViewState("Tommy", "Nook")
        var request3 = OutgoingViewState("Mario", "Mario")
        var request4 = OutgoingViewState("Luigi", "Mario")
        var request5 = OutgoingViewState("Bob", "Smith")
        var request6 = OutgoingViewState("Someone", "Something")
        var request7 = OutgoingViewState("Ash", "Ketchum")

        var outgoingRequests = mutableListOf<OutgoingViewState>(request1, request2, request3, request4,
            request5, request6, request7, request6, request6)

        return outgoingRequests
    }
}

// States

data class OutgoingRequestsViewState(
    val outgoingRequests: MutableList<OutgoingViewState> = mutableListOf()
)

data class OutgoingViewState(
    val firstName: String = "",
    val lastName: String = ""
)