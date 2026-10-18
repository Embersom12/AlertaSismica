package com.example.alertasismica.navigation

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.alertasismica.ui.screen.ChecklistScreen
import com.example.alertasismica.ui.screen.ContactFormScreen
import com.example.alertasismica.ui.screen.ContactsScreen
import com.example.alertasismica.ui.screen.LoginScreen
import com.example.alertasismica.ui.screen.QuakeDetailScreen
import com.example.alertasismica.ui.screen.QuakeListScreen
import com.example.alertasismica.ui.screen.RegisterScreen
import com.example.alertasismica.ui.screen.SavedQuakesScreen

private data class BottomTab(val route: String, val label: String, val icon: ImageVector)

private val bottomTabs = listOf(
    BottomTab(Routes.QUAKES, "Sismos", Icons.Default.Warning),
    BottomTab(Routes.SAVED, "Guardados", Icons.Default.Bookmark),
    BottomTab(Routes.CONTACTS, "Contactos", Icons.Default.Phone),
    BottomTab(Routes.CHECKLIST, "Mochila", Icons.Default.CheckCircle)
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val showBottomBar = bottomTabs.any { it.route == currentRoute }


    val darkStatusIcons =
        currentRoute == null || currentRoute == Routes.LOGIN || currentRoute == Routes.REGISTER
    val view = LocalView.current
    SideEffect {
        val window = (view.context as? Activity)?.window
        if (window != null) {
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                darkStatusIcons
        }
    }

    val onLogout: () -> Unit = {
        navController.navigate(Routes.LOGIN) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = Color.White) {
                    bottomTabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(Routes.QUAKES) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = null) },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding: PaddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Routes.QUAKES) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onGoToRegister = { navController.navigate(Routes.REGISTER) }
                )
            }
            composable(Routes.REGISTER) {
                RegisterScreen(
                    onRegistered = { navController.popBackStack() },
                    onGoToLogin = { navController.popBackStack() }
                )
            }

            composable(Routes.QUAKES) {
                QuakeListScreen(
                    onQuakeClick = { id -> navController.navigate(Routes.quakeDetail(id)) },
                    onLogout = onLogout
                )
            }
            composable(
                route = Routes.QUAKE_DETAIL,
                arguments = listOf(
                    navArgument(Routes.ARG_QUAKE_ID) { type = NavType.StringType }
                )
            ) {
                QuakeDetailScreen(onBack = { navController.popBackStack() })
            }

            composable(Routes.SAVED) {
                SavedQuakesScreen(
                    onQuakeClick = { id -> navController.navigate(Routes.quakeDetail(id)) },
                    onLogout = onLogout
                )
            }

            composable(Routes.CONTACTS) {
                ContactsScreen(
                    onAddContact = { navController.navigate(Routes.CONTACT_NEW) },
                    onEditContact = { id -> navController.navigate(Routes.contactEdit(id)) },
                    onLogout = onLogout
                )
            }
            composable(Routes.CONTACT_NEW) {
                ContactFormScreen(onBack = { navController.popBackStack() })
            }
            composable(
                route = Routes.CONTACT_EDIT,
                arguments = listOf(
                    navArgument(Routes.ARG_CONTACT_ID) { type = NavType.IntType }
                )
            ) {
                ContactFormScreen(onBack = { navController.popBackStack() })
            }

            composable(Routes.CHECKLIST) {
                ChecklistScreen(onLogout = onLogout)
            }
        }
    }
}
