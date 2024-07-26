package com.bowpi.test.data.repository

import com.bowpi.test.data.common.Status
import com.bowpi.test.data.common.TodoCocktail
import com.bowpi.test.utils._Result
import kotlinx.coroutines.flow.Flow

interface TodoCocktailsRepository {

    suspend fun getCocktail(id: String): _Result<TodoCocktail>

    fun getCocktails(query: String, status: Status): Flow<_Result<List<TodoCocktail>>>

    suspend fun saveCocktail(cocktail: TodoCocktail): _Result<Unit>

}