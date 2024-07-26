package com.bowpi.test.presentation.model

import com.bowpi.test.data.remote.model.Drink

data class DrinkUiModel(
    val id: Int,
    val name: String,
    val category: String?,
    val glass: String?,
    val thumbUrl: String?
)

fun Drink.toUiModel() =
    DrinkUiModel(
        id = id,
        name = name,
        category = category,
        glass = glass,
        thumbUrl = thumbUrl
    )
