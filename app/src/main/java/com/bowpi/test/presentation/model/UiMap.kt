package com.bowpi.test.presentation.model

import com.bowpi.test.R
import com.bowpi.test.data.common.Status

fun Status.toUi() =
    when (this) {
        Status.Unspecified ->
            UiModel(
                titleResId = R.string.all,
                iconResId = R.drawable.ic_all
            )
        Status.Pending ->
            UiModel(
                titleResId = R.string.pending,
                iconResId = R.drawable.ic_time
            )
        Status.Completed ->
            UiModel(
                titleResId = R.string.completed,
                iconResId = R.drawable.ic_done
            )
    }