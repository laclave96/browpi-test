package com.bowpi.test.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bowpi.test.ui.theme.MediumGreen

@Composable
fun ExpandedFloatingButton(
    @StringRes textResId: Int,
    @DrawableRes iconResId: Int,
    expanded: Boolean,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        text = {
            Text(
                text = stringResource(id = textResId),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        },
        icon = {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = Color.White,
            )
        },
        containerColor = MediumGreen.copy(alpha = 1f),
        expanded = expanded,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 10.dp),
        onClick = onClick
    )
}