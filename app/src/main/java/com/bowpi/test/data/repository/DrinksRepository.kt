package com.bowpi.test.data.repository

import com.bowpi.test.utils._Result
import com.bowpi.test.data.remote.model.Drink

interface DrinksRepository {
    suspend fun search(name: String): _Result<List<Drink>>
}