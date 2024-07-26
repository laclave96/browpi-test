package com.bowpi.test.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.bowpi.test.ui.component.StarRatingBar


@Composable
fun CocktailRatingDialog(rating: Int, onChangeRating: (Int) -> Unit) {
    val density = LocalDensity.current
    val windowInsets = WindowInsets.navigationBars
    val navBarHeightDp = with(density) { windowInsets.getBottom(density).toDp() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            StarRatingBar(rating = rating) {
                onChangeRating(it)
            }
        }

        Spacer(modifier = Modifier.height(50.dp + navBarHeightDp))
    }
}