package com.bowpi.test.ui.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.bowpi.test.presentation.AppEvent
import com.bowpi.test.presentation.AppState
import com.bowpi.test.ui.component.BottomNavigationBar


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen(state: AppState, onEvent: (AppEvent) -> Unit) {
    val ctx = LocalContext.current
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
    ) { paddingValues ->

        NavHost(
            navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            navigation(
                route = Screen.Home.route,
                startDestination = Screen.Home.SearchDrinks.route
            ) {
                composable(Screen.Home.SearchDrinks.route) {
                    DrinksScreen(
                        drinks = state.drinks,
                        isLoading = state.isLoading,
                        saveCocktailTaskStatus = state.saveCocktailTaskStatus
                    ) { event ->
                        when (event) {
                            is DrinkScreenEvent.SearchDrinks ->
                                onEvent(AppEvent.SearchDrinks(event.name))

                            is DrinkScreenEvent.SeeDrink -> {
                                onEvent(AppEvent.SeeDrinkDetails(event.id))
                                navController.navigate(Screen.Home.DrinkDetails.route)
                            }

                            is DrinkScreenEvent.SaveNewCocktail ->
                                onEvent(
                                    AppEvent.CreateTodoCocktail(
                                        name = event.name,
                                        instructions = event.instructions
                                    )
                                )
                        }
                    }
                    BackHandler {
                        (ctx as Activity).finish()
                    }
                }
                composable(Screen.Home.DrinkDetails.route) {
                    state.fullDrink?.let {
                        DrinkDetailsScreen(
                            animatedVisibilityScope = this,
                            drink = it
                        ) {
                            onEvent(AppEvent.AddDrinkToMyCocktailList(it.id))
                        }
                    }

                }
            }
            navigation(
                route = Screen.TodoCocktails.route,
                startDestination = Screen.TodoCocktails.MyList.route
            ) {
                composable(Screen.TodoCocktails.MyList.route) {
                    MyCocktailListScreen(cocktails = state.myCocktailList) { event ->
                        when (event) {
                            is MyCocktailListScreenEvent.CocktailCompleted ->
                                onEvent(AppEvent.CocktailCompleted(event.id))

                            is MyCocktailListScreenEvent.SearchCocktails ->
                                onEvent(
                                    AppEvent.SearchTodoCocktails(
                                        query = event.query,
                                        status = event.status
                                    )
                                )

                            is MyCocktailListScreenEvent.SeeCocktailDetails -> {
                                onEvent(AppEvent.SeeCocktailDetails(event.id))
                                navController.navigate(Screen.TodoCocktails.Edit.route)
                            }
                        }
                    }
                }
                composable(Screen.TodoCocktails.Edit.route) {
                    state.fullCocktail?.let { cocktail ->
                        EditCocktailScreen(cocktail = cocktail) {
                            onEvent(AppEvent.UpdateTodoCocktail(it))
                            navController.navigate(Screen.TodoCocktails.MyList.route)
                        }
                    }

                }
            }

        }


    }
}