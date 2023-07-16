package com.example.greetingcard

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.greetingcard.navigation.AppNavGraph
import com.example.greetingcard.navigation.LeafScreen
import com.example.greetingcard.navigation.RootScreen


/*
followed these tutorials to do the nested navigation and bottom bar
https://medium.com/@engr.waseemabbas8/implement-nested-navigation-with-bottom-navigation-bar-in-android-jetpack-compose-7cd0efbe08ad
https://www.youtube.com/watch?v=gg-KBGH9T8s&t=98s
https://www.youtube.com/watch?v=tt3dYmqJTrw

 */

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(context: Context) {
    val navController = rememberNavController()
    val currentSelectedScreen by navController.currentScreenAsState()

    // Check if the screen if within the log-in or sign-up route
    val isLoginRoute = currentSelectedScreen == RootScreen.Login

    Scaffold(
        topBar = {},

        // Hide the bottom bar for the log-in route
        bottomBar = {
            if (!isLoginRoute) {
                BottomNavBar(navController = navController, currentSelectedScreen = currentSelectedScreen)
            }
        }
    ) {

        AppNavGraph(navController = navController, context)

    }
}

@Composable
private fun BottomNavBar(
    navController: NavController,
    currentSelectedScreen: RootScreen
) {
    BottomNavigation(backgroundColor = colorResource(id = R.color.primary_blue) ,
        contentColor = Color.White) {
        BottomNavigationItem(
            selected = currentSelectedScreen == RootScreen.Home,
            onClick = { navController.navigateToRootScreen(RootScreen.Home) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Groups", fontSize = 9.sp)
            },
            icon = { Icon(painterResource(id = R.drawable.ic_home), "Home")
            },
            selectedContentColor = Color.White,
            unselectedContentColor = colorResource(id = R.color.icon_blue)
        )
        BottomNavigationItem(
            selected = currentSelectedScreen == RootScreen.MyTasks,
            onClick = { navController.navigateToRootScreen(RootScreen.MyTasks) },
            alwaysShowLabel = true,
            label = {
                Text(text = "My Tasks", fontSize = 9.sp)
            },
            icon = {
                Icon(painterResource(id =  R.drawable.ic_task), "Tasks")
            },
            selectedContentColor = Color.White,
            unselectedContentColor = colorResource(id = R.color.icon_blue)
        )
        BottomNavigationItem(
            selected = currentSelectedScreen == RootScreen.AddTask,
            onClick = { navController.navigateToRootScreen(RootScreen.AddTask) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Add Task", fontSize = 9.sp)
            },
            icon = { Icon(painterResource(id =  R.drawable.ic_add_circle), "Add")

            },
            selectedContentColor = Color.White,
            unselectedContentColor = colorResource(id = R.color.icon_blue)
        )
        BottomNavigationItem(
            selected = currentSelectedScreen == RootScreen.Activity,
            onClick = { navController.navigateToRootScreen(RootScreen.Activity) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Activity", fontSize = 9.sp)
            },
            icon = { Icon(painterResource(id =  R.drawable.ic_notifications), "Activity")
            },
            selectedContentColor = Color.White,
            unselectedContentColor = colorResource(id = R.color.icon_blue)
        )
        BottomNavigationItem(
            selected = currentSelectedScreen == RootScreen.Profile,
            onClick = { navController.navigateToRootScreen(RootScreen.Profile) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Profile", fontSize = 9.sp)
            },
            icon = {
                Icon(painterResource(id =  R.drawable.ic_account_circle), "Profile")
            },
            selectedContentColor = Color.White,
            unselectedContentColor = colorResource(id = R.color.icon_blue)
        )
    }
}

@Stable
@Composable
private fun NavController.currentScreenAsState(): State<RootScreen> {
    val selectedItem = remember { mutableStateOf<RootScreen>(RootScreen.Home) }
    DisposableEffect(key1 = this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == RootScreen.Login.route } -> {
                    selectedItem.value = RootScreen.Login
                }
                destination.hierarchy.any { it.route == RootScreen.Home.route } -> {
                    selectedItem.value = RootScreen.Home
                }
                destination.hierarchy.any { it.route == RootScreen.MyTasks.route } -> {
                    selectedItem.value = RootScreen.MyTasks
                }
                destination.hierarchy.any { it.route == RootScreen.AddTask.route } -> {
                    selectedItem.value = RootScreen.AddTask
                }
                destination.hierarchy.any { it.route == RootScreen.Activity.route } -> {
                    selectedItem.value = RootScreen.Activity
                }
                destination.hierarchy.any { it.route == RootScreen.Profile.route } -> {
                    selectedItem.value = RootScreen.Profile
                }
            }

        }
        addOnDestinationChangedListener(listener)
        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }
    return selectedItem
}

private fun NavController.navigateToRootScreen(rootScreen: RootScreen) {
    navigate(rootScreen.route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}

