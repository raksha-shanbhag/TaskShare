package com.example.greetingcard

/*
To build bottom navigation bar I followed along to two tutorials:
https://www.youtube.com/watch?v=gg-KBGH9T8s&t=98s
https://www.youtube.com/watch?v=tt3dYmqJTrw
*/

sealed class BottomNavigationItems(var route: String, var title: String, var icon: Int) {
    object Home: BottomNavigationItems("home", "Groups", R.drawable.ic_home)
    object MyTasks: BottomNavigationItems("tasks", "My Tasks", R.drawable.ic_task)
    object AddTask: BottomNavigationItems("addTask", "Add Task", R.drawable.ic_add_circle)
    object Notifications: BottomNavigationItems("notifications", "Notifications", R.drawable.ic_notifications)
    object Profile: BottomNavigationItems("profile", "Profile", R.drawable.ic_account_circle)

}