package com.bowpi.test.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bowpi.test.R
import com.bowpi.test.ui.theme.LightBlue
import com.bowpi.test.ui.theme.quicksand


@Composable
fun AddIngredientDialog(
    ingredient: String,
    measure: String,
    onChangeIngredient: (String) -> Unit,
    onChangeMeasure: (String) -> Unit,
    onSave: () -> Unit
){
    val density = LocalDensity.current
    val windowInsets = WindowInsets.navigationBars
    val navBarHeightDp = with(density) { windowInsets.getBottom(density).toDp() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(id = R.string.add_ingredient),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = ingredient,
            onValueChange = onChangeIngredient,
            textStyle = TextStyle(
                fontFamily = quicksand,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray.copy(alpha = 0.7f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.ingredient),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray.copy(alpha = 0.4f)
                )
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = measure,
            onValueChange = onChangeMeasure,
            textStyle = TextStyle(
                fontFamily = quicksand,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray.copy(alpha = 0.7f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.measure),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray.copy(alpha = 0.4f)
                )
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(35.dp))
                    .background(LightBlue)
                    .clickable { onSave() }
                    .padding(horizontal = 45.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.save),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp + navBarHeightDp))

    }
}