package com.bowpi.test.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class UiModel(
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int,
)
