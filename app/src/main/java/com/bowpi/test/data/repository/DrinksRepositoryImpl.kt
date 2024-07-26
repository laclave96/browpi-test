package com.bowpi.test.data.repository

import com.bowpi.test.utils.Message
import com.bowpi.test.R
import com.bowpi.test.utils._Result
import com.bowpi.test.data.remote.model.Drink
import com.bowpi.test.data.remote.service.DrinksService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DrinksRepositoryImpl(private val drinksService: DrinksService) : DrinksRepository {

    override suspend fun search(name: String): _Result<List<Drink>> {
        try {
            val response = withContext(Dispatchers.IO) {
                drinksService.search(name)
            }

            if (response.isSuccessful)
                return _Result.Success(response.body()?.list ?: listOf())

            return _Result.Error(Message.StringResource(R.string.unexpected_error))

        } catch (e: Exception) {
            e.printStackTrace()
            return _Result.Error(Message.StringResource(R.string.search_drinks_error))
        }

    }

}