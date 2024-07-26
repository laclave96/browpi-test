package com.bowpi.test.presentation.model

import java.io.Serializable


data class SelectableItem<T>(
    val content: T,
    val selected: Boolean = false
):Serializable