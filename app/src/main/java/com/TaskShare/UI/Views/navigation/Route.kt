package com.example.greetingcard.navigation


sealed class RootScreen(val route: String) {
    object Login : RootScreen("login_root")
    object Home : RootScreen("home_root")
    object MyTasks : RootScreen("myTasks_root")
    object AddTask : RootScreen("addTask_root")
    object Activity : RootScreen("activity_root")
    object Profile : RootScreen("profile_root")
}

sealed class LeafScreen(val route: String) {
    object Login : LeafScreen("logIn")
    object SignUp : LeafScreen("signUp")
    object Home : LeafScreen("home")
    object MyTasks : LeafScreen("myTasks")
    object AddTask : LeafScreen("addTask")
    object Activity : LeafScreen("activity")
    object Profile : LeafScreen("profile")
    object CreateGroup : LeafScreen("createGroup")
    object ViewGroup : LeafScreen("viewGroup")
    object ViewGroupTasks : LeafScreen("viewGroupTasks")
    object TaskDetails : LeafScreen("taskDetails")
}