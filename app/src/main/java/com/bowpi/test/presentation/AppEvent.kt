package com.bowpi.test.presentation

import com.bowpi.test.data.common.Status
import com.bowpi.test.data.common.TodoCocktail

sealed class AppEvent {
    class SearchDrinks(val name: String): AppEvent()
    class SeeDrinkDetails(val id: Int): AppEvent()
    class AddDrinkToMyCocktailList(val drinkId: Int): AppEvent()
    class SearchTodoCocktails(val query: String, val status: Status): AppEvent()
    class SeeCocktailDetails(val id: String): AppEvent()
    class CocktailCompleted(val id: String): AppEvent()
    class CreateTodoCocktail(val name: String, val instructions: String): AppEvent()
    class UpdateTodoCocktail(val cocktail: TodoCocktail): AppEvent()
}