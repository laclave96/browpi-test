package com.bowpi.test.ui.component.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bowpi.test.ui.component.BottomNavItem
import com.bowpi.test.ui.theme.DarkGreen
import com.bowpi.test.ui.theme.LightGreen

@Composable
fun NavigationItem(
    navItem: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val background = if (isSelected) LightGreen.copy(alpha = 0.6f) else Color.Transparent
    val iconColor = if (isSelected) DarkGreen else Color.DarkGray.copy(alpha = 0.6f)
    val textColor = if (isSelected) Color.Black else Color.DarkGray.copy(alpha = 0.6f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) { onClick() }) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(23.dp))
                .background(background)
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = navItem.item.iconResId),
                contentDescription = null,
                tint = iconColor,
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(id = navItem.item.titleResId),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
        )
    }

}