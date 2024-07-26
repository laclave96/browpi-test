package com.bowpi.test.ui.screen

sealed class Screen(val route: String) {

    data object Home : Screen("home") {
        data object SearchDrinks : Screen("${route}/search")
        data object DrinkDetails : Screen("${route}/details")
        data object AddCocktail : Screen("${route}/register")
    }

    data object TodoCocktails : Screen("cocktails") {
        data object MyList: Screen("$route/list")
        data object Edit : Screen("$route/edit")
    }

}