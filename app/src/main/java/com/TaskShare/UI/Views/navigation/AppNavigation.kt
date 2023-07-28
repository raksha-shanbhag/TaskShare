package com.example.greetingcard.navigation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.TaskShare.UI.Views.screens.AddFriendScreen
import com.TaskShare.UI.Views.screens.BlockedUserScreen
import com.TaskShare.UI.Views.screens.ChangePasswordScreen
import com.TaskShare.UI.Views.screens.EditGroupScreen
import com.TaskShare.UI.Views.screens.EditProfileScreen
import com.TaskShare.UI.Views.screens.FriendScreen
import com.TaskShare.UI.Views.screens.IncomingRequestsScreen
import com.TaskShare.UI.Views.screens.OutgoingRequestsScreen
import com.TaskShare.ViewModels.GroupViewModel
import com.TaskShare.ViewModels.TaskViewModel
import com.example.greetingcard.screens.AddTaskScreen
import com.example.greetingcard.screens.CreateGroupScreen
import com.example.greetingcard.screens.EditTaskScreen
import com.example.greetingcard.screens.HomeScreen
import com.example.greetingcard.screens.LoginScreen
import com.example.greetingcard.screens.MyTasksScreen
import com.example.greetingcard.screens.NotificationScreen
import com.example.greetingcard.screens.ProfileScreen
import com.example.greetingcard.screens.SignUpScreen
import com.example.greetingcard.screens.TaskDetailsScreen
import com.example.greetingcard.screens.ViewGroupScreen
import com.example.greetingcard.screens.ViewGroupTasksScreen
import com.example.greetingcard.switchTabs


/*
followed these tutorials to do the nested navigation and bottom bar:s
https://medium.com/@engr.waseemabbas8/implement-nested-navigation-with-bottom-navigation-bar-in-android-jetpack-compose-7cd0efbe08ad
https://www.youtube.com/watch?v=gg-KBGH9T8s&t=98s
https://www.youtube.com/watch?v=tt3dYmqJTrw
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    context: Context
) {
    val groupViewModel: GroupViewModel = viewModel()
    val taskViewModel: TaskViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = RootScreen.Login.route
        //startDestination = RootScreen.Home.route
    ) {
        addLoginRoute(navController)
        addHomeRoute(navController, groupViewModel, taskViewModel)
        addMyTasksRoute(navController, taskViewModel, context)
        addCreateTaskRoute(navController, context)
        addActivityRoute(navController, taskViewModel)
        addProfileRoute(navController)
    }
}

// Log-in navigation
private fun NavGraphBuilder.addLoginRoute(navController: NavController) {
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
private fun NavGraphBuilder.addHomeRoute(navController: NavController, viewModel: GroupViewModel, taskViewModel: TaskViewModel) {
    navigation(
        route = RootScreen.Home.route,
        startDestination = LeafScreen.Home.route
    ) {
        showGroups(navController, viewModel)
        showViewGroup(navController, viewModel)
        showCreateGroup(navController, viewModel)
        showViewGroupTasks(navController, viewModel, taskViewModel)
        showEditGroup(navController, viewModel)
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
            showEdit = {
                navController.navigate(LeafScreen.EditGroup.route)
            },
            viewModel
        )
    }
}


private fun NavGraphBuilder.showViewGroupTasks(navController: NavController, viewModel: GroupViewModel, taskViewModel: TaskViewModel) {
    composable(route = LeafScreen.ViewGroupTasks.route) {
        ViewGroupTasksScreen(
            onBack = {
                navController.navigateUp()
            },
            showEdit = {
                navController.navigate(LeafScreen.ViewGroup.route)
            },
            showTaskDetail = {
                navController.navigate(LeafScreen.TaskDetails.route)
            },
            viewModel,
            taskViewModel
        )
    }
}

private fun NavGraphBuilder.showCreateGroup(navController: NavController, viewModel: GroupViewModel) {
    composable(route = LeafScreen.CreateGroup.route) {
        CreateGroupScreen(
            onBack = {
                navController.navigateUp()
            },
            onDone = {
                navController.navigate(RootScreen.Home.route)
            },
            viewModel
        )
    }
}


private fun NavGraphBuilder.showEditGroup(navController: NavController, viewModel: GroupViewModel) {
    composable(route = LeafScreen.EditGroup.route) {
        EditGroupScreen(
            onBack = {
                navController.navigateUp()
            },
            onDone = {
                navController.navigate(RootScreen.Home.route)
            },
            viewModel
        )
    }
}
//end of home navigation

//myTasks navigation
@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.addMyTasksRoute(navController: NavController, viewModel: TaskViewModel, context: Context) {
    navigation(
        route = RootScreen.MyTasks.route,
        startDestination = LeafScreen.MyTasks.route
    ) {
        showMyTasks(navController, viewModel)
        showTaskDetail(navController, viewModel)
        showEditTask(navController, context, viewModel)
    }
}
private fun NavGraphBuilder.showMyTasks(navController: NavController, viewModel: TaskViewModel) {
    composable(route = LeafScreen.MyTasks.route) {
        MyTasksScreen(
            showDetail = {
                navController.navigate(LeafScreen.TaskDetails.route)
            }, viewModel
        )
    }
}

private fun NavGraphBuilder.showTaskDetail(navController: NavController, viewModel: TaskViewModel) {
    composable(route = LeafScreen.TaskDetails.route) {
        TaskDetailsScreen(
            onBack = {
                navController.navigateUp()
            }, viewModel, editTask = {
                navController.navigate(LeafScreen.EditTask.route)
            }
        )
    }
}
//end of myTasks navigation

//addTask navigation
@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.addCreateTaskRoute(navController: NavController, context: Context) {
    navigation(
        route = RootScreen.AddTask.route,
        startDestination = LeafScreen.AddTask.route
    ) {
        showAddTask(navController, context)
    }
}
@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.showAddTask(navController: NavController, context: Context) {
    composable(route = LeafScreen.AddTask.route) {
        AddTaskScreen(context,
            redirectToMyTasks = {
                navController.switchTabs(LeafScreen.MyTasks.route)
            }
        )
    }
}
//end of AddTask navigation

//edit task navigation
//private fun NavGraphBuilder.addEditTaskRoute(navController: NavController, context: Context) {
//    composable(
//        route = LeafScreen.EditTask.route
//    ) {
//        showEditTask(navController, context)
//    }
//}
@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.showEditTask(navController: NavController, context: Context, viewModel: TaskViewModel) {
    composable(route = LeafScreen.EditTask.route) {
        EditTaskScreen(context,
            redirectToMyTasks = {
                navController.switchTabs(LeafScreen.MyTasks.route)
            }, viewModel
        )
    }
}
//end of edit task navigation


//activity navigation
private fun NavGraphBuilder.addActivityRoute(navController: NavController, viewModel: TaskViewModel) {
    navigation(
        route = RootScreen.Activity.route,
        startDestination = LeafScreen.Activity.route
    ) {
        showActivity(navController, viewModel)
    }
}
private fun NavGraphBuilder.showActivity(navController: NavController, viewModel: TaskViewModel) {
    composable(route = LeafScreen.Activity.route) {
        NotificationScreen(
            viewModel,
            goToDetail = {
                navController.navigate(LeafScreen.TaskDetails.route)
            },
        )
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
        showIncomingRequests(navController)
        showOutgoingRequests(navController)
    }
}
private fun NavGraphBuilder.showProfile(navController: NavController) {
    composable(route = LeafScreen.Profile.route) {
        ProfileScreen(
            onSeeFriends = { navController.navigate(LeafScreen.Friends.route) },
            onEditProfile = { navController.navigate(LeafScreen.EditProfile.route) },
            onChangePassword = { navController.navigate(LeafScreen.ChangePassword.route) },
            onSeeBlockedAccs = { navController.navigate(LeafScreen.BlockedAccounts.route) },
        )
    }
}

private fun NavGraphBuilder.showFriends(navController: NavController) {
    composable(route = LeafScreen.Friends.route) {
        FriendScreen(
            onBack = { navController.navigate(LeafScreen.Profile.route) },
            onAddFriends = { navController.navigate(LeafScreen.AddFriend.route) },
            onFriends = { navController.navigate(LeafScreen.Friends.route) },
            onOutgoing = { navController.navigate(LeafScreen.OutgoingRequests.route) },
            onIncoming = { navController.navigate(LeafScreen.IncomingRequests.route) }
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
        )
    }
}

private fun NavGraphBuilder.showBlockedAccounts(navController: NavController) {
    composable(route = LeafScreen.BlockedAccounts.route) {
        BlockedUserScreen(
            onBack = { navController.navigateUp() }
        )
    }
}

private fun NavGraphBuilder.showAddFriends(navController: NavController) {
    composable(route = LeafScreen.AddFriend.route) {
        AddFriendScreen(
            onBack = { navController.navigateUp() }
        )
    }
}

private fun NavGraphBuilder.showIncomingRequests(navController: NavController) {
    composable(route = LeafScreen.IncomingRequests.route) {
        IncomingRequestsScreen(
            onBack = { navController.navigate(LeafScreen.Profile.route) },
            onFriends = { navController.navigate(LeafScreen.Friends.route) },
            onIncoming = { navController.navigate(LeafScreen.IncomingRequests.route) },
            onOutgoing = { navController.navigate(LeafScreen.OutgoingRequests.route) }
        )
    }
}

private fun NavGraphBuilder.showOutgoingRequests(navController: NavController) {
    composable(route = LeafScreen.OutgoingRequests.route) {
        OutgoingRequestsScreen(
            onBack = { navController.navigate(LeafScreen.Profile.route) },
            onFriends = { navController.navigate(LeafScreen.Friends.route) },
            onIncoming = { navController.navigate(LeafScreen.IncomingRequests.route) },
            onOutgoing = { navController.navigate(LeafScreen.OutgoingRequests.route) }
        )
    }
}
//end of profile navigation