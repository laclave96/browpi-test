package com.bowpi.test.presentation.model

import com.bowpi.test.data.common.Status
import com.bowpi.test.data.common.TodoCocktail
import com.bowpi.test.utils.DateTimeObj

data class TodoCocktailUiModel(
    val id: String,
    val name: String,
    val glass: String?,
    val thumbUrl: String?,
    val status: Status,
    val rating: String,
    val datetime: DateTimeObj
)

fun TodoCocktail.toUiModel() =
    TodoCocktailUiModel(
        id = id,
        name = drink.name,
        glass = drink.glass,
        thumbUrl = drink.thumbUrl,
        status = status,
        rating = rating.toString(),
        datetime = DateTimeObj.fromTimestamp(timestamp)
    )
