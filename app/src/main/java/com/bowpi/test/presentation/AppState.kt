package com.bowpi.test.presentation

import com.bowpi.test.data.common.TodoCocktail
import com.bowpi.test.data.remote.model.Drink
import com.bowpi.test.presentation.model.DrinkUiModel
import com.bowpi.test.presentation.model.TodoCocktailUiModel
import com.bowpi.test.utils.TaskStatus

data class AppState (
    val drinks: List<DrinkUiModel> = listOf(),
    val fullDrink: Drink? = null,
    val myCocktailList: List<TodoCocktailUiModel> = listOf(),
    val fullCocktail: TodoCocktail? = null,
    val isLoading: Boolean = false,
    val saveCocktailTaskStatus: TaskStatus = TaskStatus.Default
)