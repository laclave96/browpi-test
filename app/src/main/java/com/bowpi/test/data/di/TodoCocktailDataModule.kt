package com.bowpi.test.data.di

import com.bowpi.test.Constants.COCKTAILS_REF
import com.bowpi.test.data.repository.TodoCocktailsRepository
import com.bowpi.test.data.repository.TodoCocktailsRepositoryImpl
import com.google.firebase.database.FirebaseDatabase
import org.koin.dsl.module

val todoCocktailDataModule = module {
    includes(baseModule)

    single<TodoCocktailsRepository> {
        val cocktailsRef = get<FirebaseDatabase>().getReference(COCKTAILS_REF)
        cocktailsRef.keepSynced(true)
        TodoCocktailsRepositoryImpl(cocktailsRef)
    }
}