package com.bowpi.test.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bowpi.test.R
import com.bowpi.test.presentation.model.UiModel
import com.bowpi.test.ui.component.item.NavigationItem
import com.bowpi.test.ui.screen.Screen
import com.bowpi.test.ui.theme.LightBlue

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    val items = listOf(BottomNavItem.Explore, BottomNavItem.MyList)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightBlue.copy(alpha = 1f))
            .padding(bottom = 10.dp, top = 15.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        items.forEach { item ->
            NavigationItem(
                navItem = item,
                isSelected = currentRoute?.contains(item.route) ?: false,
            ) {
                navController.navigate(item.route) {
                    launchSingleTop = true
                }
            }
        }
    }
}

sealed class BottomNavItem(
    val route: String,
    val item: UiModel
) {
    data object Explore : BottomNavItem(
        route = Screen.Home.route,
        item = UiModel(
            titleResId = R.string.explore,
            iconResId = R.drawable.ic_search_fill
        )
    )

    data object MyList : BottomNavItem(
        route = Screen.TodoCocktails.route,
        item = UiModel(
            titleResId = R.string.my_list,
            iconResId = R.drawable.ic_bookmark_fill
        )
    )
}




