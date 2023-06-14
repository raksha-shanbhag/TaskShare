package com.example.greetingcard

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.greetingcard.screens.AddTaskScreen
import com.example.greetingcard.screens.HomeScreen
import com.example.greetingcard.screens.MyTasksScreen
import com.example.greetingcard.screens.NotificationScreen
import com.example.greetingcard.screens.ProfileScreen

@Composable
fun Navigation(navController: NavHostController){
    NavHost(navController, startDestination = BottomNavigationItems.Home.route) {
        composable(BottomNavigationItems.Home.route) {
            HomeScreen()
        }
        composable(BottomNavigationItems.MyTasks.route) {
            MyTasksScreen()
        }
        composable(BottomNavigationItems.AddTask.route) {
            AddTaskScreen()
        }
        composable(BottomNavigationItems.Notifications.route) {
            NotificationScreen()
        }
        composable(BottomNavigationItems.Profile.route) {
            ProfileScreen()
        }
    }
}