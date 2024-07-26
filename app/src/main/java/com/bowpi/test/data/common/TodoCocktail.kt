package com.bowpi.test.data.common

import androidx.annotation.IntRange
import com.bowpi.test.data.remote.model.Drink

data class TodoCocktail (
    val id: String = "",
    val drink: Drink = Drink(id = 0, name = ""),
    val status: Status = Status.Pending,
    @IntRange(0,5) val rating: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
)