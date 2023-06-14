package com.example.greetingcard

sealed class BottomNavigationItems(var route: String, var title: String, var icon: Int) {
    object Home: BottomNavigationItems("home", "Groups", R.drawable.ic_home)
    object MyTasks: BottomNavigationItems("tasks", "My Tasks", R.drawable.ic_task)
    object AddTask: BottomNavigationItems("addTask", "Add Task", R.drawable.ic_add_circle)
    object Notifications: BottomNavigationItems("notifications", "Notifications", R.drawable.ic_notifications)
    object Profile: BottomNavigationItems("profile", "Profile", R.drawable.ic_account_circle)

}