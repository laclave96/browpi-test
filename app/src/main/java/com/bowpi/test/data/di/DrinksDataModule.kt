package com.bowpi.test.data.di

import com.bowpi.test.Constants.DrinksApiScheme.BASE_URL
import com.bowpi.test.data.remote.DrinkDeserializer
import com.bowpi.test.data.remote.model.Drink
import com.bowpi.test.data.repository.DrinksRepository
import com.bowpi.test.data.repository.DrinksRepositoryImpl
import com.bowpi.test.data.remote.service.DrinksService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val drinksDataModule = module {

    includes(baseModule)

    single<DrinksService> {
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(Drink::class.java, DrinkDeserializer())
            .create()

        get<Retrofit>().newBuilder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(get())
            .build()
            .create(DrinksService::class.java)
    }

    single<DrinksRepository> { DrinksRepositoryImpl(get()) }

}