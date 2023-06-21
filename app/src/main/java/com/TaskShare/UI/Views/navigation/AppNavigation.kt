package com.example.greetingcard.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.greetingcard.screens.AddTaskScreen
import com.example.greetingcard.screens.CreateGroupScreen
import com.example.greetingcard.screens.HomeScreen
import com.example.greetingcard.screens.MyTasksScreen
import com.example.greetingcard.screens.NotificationScreen
import com.example.greetingcard.screens.ProfileScreen
import com.example.greetingcard.screens.TaskDetailsScreen
import com.example.greetingcard.screens.ViewGroupScreen
import com.example.greetingcard.screens.ViewGroupTasksScreen


/*
followed these tutorials to do the nested navigation and bottom bar:s
https://medium.com/@engr.waseemabbas8/implement-nested-navigation-with-bottom-navigation-bar-in-android-jetpack-compose-7cd0efbe08ad
https://www.youtube.com/watch?v=gg-KBGH9T8s&t=98s
https://www.youtube.com/watch?v=tt3dYmqJTrw
 */

@Composable
fun AppNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = RootScreen.Home.route
    ) {
        addHomeRoute(navController)
        addMyTasksRoute(navController)
        addCreateTaskRoute(navController)
        addActivityRoute(navController)
        addProfileRoute(navController)
    }
}

//home navigation
private fun NavGraphBuilder.addHomeRoute(navController: NavController) {
    navigation(
        route = RootScreen.Home.route,
        startDestination = LeafScreen.Home.route
    ) {
        showGroups(navController)
        showViewGroup(navController)
        showCreateGroup(navController)
        showViewGroupTasks(navController)
    }
}
private fun NavGraphBuilder.showGroups(navController: NavController) {
    composable(route = LeafScreen.Home.route) {
        HomeScreen(
            showDetail = {
                navController.navigate(LeafScreen.ViewGroupTasks.route)
            },
            showCreate = {
                navController.navigate(LeafScreen.CreateGroup.route)
            }
        )
    }
}
private fun NavGraphBuilder.showViewGroup(navController: NavController) {
    composable(route = LeafScreen.ViewGroup.route) {
        ViewGroupScreen(
            onBack = {
                navController.navigateUp()
            }
        )
    }
}


private fun NavGraphBuilder.showViewGroupTasks(navController: NavController) {
    composable(route = LeafScreen.ViewGroupTasks.route) {
        ViewGroupTasksScreen(
            onBack = {
                navController.navigateUp()
            },
            showEdit = {
                navController.navigate(LeafScreen.ViewGroup.route)
            }
        )
    }
}

private fun NavGraphBuilder.showCreateGroup(navController: NavController) {
    composable(route = LeafScreen.CreateGroup.route) {
        CreateGroupScreen(
            onBack = {
                navController.navigateUp()
            }
        )
    }
}
//end of home navigation

//myTasks navigation
private fun NavGraphBuilder.addMyTasksRoute(navController: NavController) {
    navigation(
        route = RootScreen.MyTasks.route,
        startDestination = LeafScreen.MyTasks.route
    ) {
        showMyTasks(navController)
        showTaskDetail(navController)
    }
}
private fun NavGraphBuilder.showMyTasks(navController: NavController) {
    composable(route = LeafScreen.MyTasks.route) {
        MyTasksScreen(
            showDetail = {
                navController.navigate(LeafScreen.TaskDetails.route)
            }
        )
    }
}

private fun NavGraphBuilder.showTaskDetail(navController: NavController) {
    composable(route = LeafScreen.TaskDetails.route) {
        TaskDetailsScreen(
            onBack = {
                navController.navigateUp()
            }
        )
    }
}
//end of myTasks navigation

//addTask navigation
private fun NavGraphBuilder.addCreateTaskRoute(navController: NavController) {
    navigation(
        route = RootScreen.AddTask.route,
        startDestination = LeafScreen.AddTask.route
    ) {
        showAddTask(navController)
    }
}
private fun NavGraphBuilder.showAddTask(navController: NavController) {
    composable(route = LeafScreen.AddTask.route) {
        AddTaskScreen()
    }
}
//end of AddTask navigation

//activity navigation
private fun NavGraphBuilder.addActivityRoute(navController: NavController) {
    navigation(
        route = RootScreen.Activity.route,
        startDestination = LeafScreen.Activity.route
    ) {
        showActivity(navController)
    }
}
private fun NavGraphBuilder.showActivity(navController: NavController) {
    composable(route = LeafScreen.Activity.route) {
        NotificationScreen()
    }
}
//end of activity navigation

//profile navigation
private fun NavGraphBuilder.addProfileRoute(navController: NavController) {
    navigation(
        route = RootScreen.Profile.route,
        startDestination = LeafScreen.Profile.route
    ) {
        showProfile(navController)
    }
}
private fun NavGraphBuilder.showProfile(navController: NavController) {
    composable(route = LeafScreen.Profile.route) {
        ProfileScreen()
    }
}
//end of profile navigation