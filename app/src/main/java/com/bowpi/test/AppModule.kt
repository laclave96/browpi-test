package com.bowpi.test

import com.bowpi.test.data.di.drinksDataModule
import com.bowpi.test.data.di.todoCocktailDataModule
import com.bowpi.test.presentation.AppViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    includes(drinksDataModule, todoCocktailDataModule)
    viewModel { AppViewModel(get(), get(), get()) }
}

