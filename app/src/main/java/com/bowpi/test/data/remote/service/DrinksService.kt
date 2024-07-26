package com.bowpi.test.data.remote.service

import com.bowpi.test.Constants.DrinksApiScheme.QUERY_BY_NAME
import com.bowpi.test.Constants.DrinksApiScheme.SEARCH_PATH
import com.bowpi.test.data.remote.model.DrinkList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DrinksService {

    @GET(SEARCH_PATH)
    suspend fun search(
        @Query(QUERY_BY_NAME) query: String
    ): Response<DrinkList>

}