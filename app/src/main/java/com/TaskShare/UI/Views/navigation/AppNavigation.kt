package com.example.greetingcard.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.TaskShare.UI.Views.screens.AddFriendScreen
import com.TaskShare.UI.Views.screens.BlockUserScreen
import com.TaskShare.UI.Views.screens.BlockedUserScreen
import com.TaskShare.UI.Views.screens.ChangePasswordScreen
import com.TaskShare.UI.Views.screens.EditProfileScreen
import com.TaskShare.UI.Views.screens.FriendScreen
import com.TaskShare.ViewModels.GroupViewModel
import com.example.greetingcard.screens.AddTaskScreen
import com.example.greetingcard.screens.CreateGroupScreen
import com.example.greetingcard.screens.HomeScreen
import com.example.greetingcard.screens.LoginScreen
import com.example.greetingcard.screens.MyTasksScreen
import com.example.greetingcard.screens.NotificationScreen
import com.example.greetingcard.screens.ProfileScreen
import com.example.greetingcard.screens.SignUpScreen
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
    val viewModel: GroupViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = RootScreen.Login.route
        //startDestination = RootScreen.Home.route
    ) {
        addLoginRoute(navController, viewModel)
        addHomeRoute(navController, viewModel)
        addMyTasksRoute(navController)
        addCreateTaskRoute(navController)
        addActivityRoute(navController)
        addProfileRoute(navController)
    }
}

// Log-in navigation
private fun NavGraphBuilder.addLoginRoute(navController: NavController, viewModel: GroupViewModel) {
    navigation(
        route = RootScreen.Login.route,
        startDestination = LeafScreen.Login.route
    ) {
        showLogin(navController)
        showSignUp(navController)
    }
}

private fun NavGraphBuilder.showLogin(navController: NavController) {
    composable(route = LeafScreen.Login.route) {
        LoginScreen(
            onLogInClick = {
                navController.navigate(RootScreen.Home.route)
            },
            onSignUpClick = {
                navController.navigate(LeafScreen.SignUp.route)
            }
        )
    }
}

private fun NavGraphBuilder.showSignUp(navController: NavController) {
    composable(route = LeafScreen.SignUp.route) {
        SignUpScreen(
            onLogInClick = {
                navController.navigate(RootScreen.Login.route)
            },
            onSignUpClick = {
                navController.navigate(RootScreen.Home.route)
            }
        )
    }
}


//home navigation
private fun NavGraphBuilder.addHomeRoute(navController: NavController, viewModel: GroupViewModel) {
    navigation(
        route = RootScreen.Home.route,
        startDestination = LeafScreen.Home.route
    ) {
        showGroups(navController, viewModel)
        showViewGroup(navController, viewModel)
        showCreateGroup(navController, viewModel)
        showViewGroupTasks(navController, viewModel)
    }
}
private fun NavGraphBuilder.showGroups(navController: NavController, viewModel: GroupViewModel) {
    composable(route = LeafScreen.Home.route) {
        HomeScreen(
            showDetail = {
                navController.navigate(LeafScreen.ViewGroupTasks.route)
            },
            showCreate = {
                navController.navigate(LeafScreen.CreateGroup.route)
            },
            viewModel
        )
    }
}
private fun NavGraphBuilder.showViewGroup(navController: NavController, viewModel: GroupViewModel) {
    composable(route = LeafScreen.ViewGroup.route) {
        ViewGroupScreen(
            onBack = {
                navController.navigateUp()
            },
             viewModel
        )
    }
}


private fun NavGraphBuilder.showViewGroupTasks(navController: NavController, viewModel: GroupViewModel) {
    composable(route = LeafScreen.ViewGroupTasks.route) {
        ViewGroupTasksScreen(
            onBack = {
                navController.navigateUp()
            },
            showEdit = {
                navController.navigate(LeafScreen.ViewGroup.route)
            },
            viewModel
        )
    }
}

private fun NavGraphBuilder.showCreateGroup(navController: NavController, viewModel: GroupViewModel) {
    composable(route = LeafScreen.CreateGroup.route) {
        CreateGroupScreen(
            onBack = {
                navController.navigateUp()
            },
            viewModel
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
        showFriends(navController)
        showEditProfile(navController)
        showChangePassword(navController)
        showBlockedAccounts(navController)
        showAddFriends(navController)
        showBlockUser(navController)
    }
}
private fun NavGraphBuilder.showProfile(navController: NavController) {
    composable(route = LeafScreen.Profile.route) {
        ProfileScreen(
            onSeeFriends = { navController.navigate(LeafScreen.Friends.route) },
            onEditProfile = { navController.navigate(LeafScreen.EditProfile.route) },
            onChangePassword = { navController.navigate(LeafScreen.ChangePassword.route) },
            onSeeBlockedAccs = { navController.navigate(LeafScreen.BlockedAccounts.route) },
            onLogOut = { },
        )
    }
}

private fun NavGraphBuilder.showFriends(navController: NavController) {
    composable(route = LeafScreen.Friends.route) {
        FriendScreen(
            onBack = { navController.navigateUp() },
            onAddFriends = { navController.navigate(LeafScreen.AddFriend.route) },
            onRemoveFriend = {}
        )
    }
}

private fun NavGraphBuilder.showEditProfile(navController: NavController) {
    composable(route = LeafScreen.EditProfile.route) {
        EditProfileScreen(
            onBack = { navController.navigateUp() },
            onEditPicture = {},
            onSaveEdit = {}
        )
    }
}

private fun NavGraphBuilder.showChangePassword(navController: NavController) {
    composable(route = LeafScreen.ChangePassword.route) {
        ChangePasswordScreen(
            onBack = { navController.navigateUp() },
            onSavePassword = {}
        )
    }
}

private fun NavGraphBuilder.showBlockedAccounts(navController: NavController) {
    composable(route = LeafScreen.BlockedAccounts.route) {
        BlockedUserScreen(
            onBack = { navController.navigateUp() },
            onBlockUsers = { navController.navigate(LeafScreen.BlockUser.route) },
            onUnblockUser = {},
        )
    }
}

private fun NavGraphBuilder.showAddFriends(navController: NavController) {
    composable(route = LeafScreen.AddFriend.route) {
        AddFriendScreen(
            onBack = { navController.navigateUp() },
            onFriendRequest = {}
        )
    }
}

private fun NavGraphBuilder.showBlockUser(navController: NavController) {
    composable(route = LeafScreen.BlockUser.route) {
        BlockUserScreen(
            onBack = { navController.navigateUp() },
            onBlockUser = {}
        )
    }
}
//end of profile navigation