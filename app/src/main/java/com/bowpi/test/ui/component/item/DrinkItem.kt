package com.bowpi.test.ui.component.item

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.bowpi.test.R
import com.bowpi.test.presentation.model.DrinkUiModel
import com.bowpi.test.utils.shimmerEffect


@Composable
fun DrinkItem(
    drink: DrinkUiModel,
    onClick: () -> Unit
) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = drink.thumbUrl)
            .apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
                error(R.drawable.ic_drink)
                crossfade(1000)
            }).build()
    )
    val painterState = painter.state

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .clickable { onClick() }
            .padding(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = if (drink.thumbUrl.isNullOrEmpty())
                    painterResource(id = R.drawable.ic_drink)
                else painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (painterState is AsyncImagePainter.State.Loading)
                            Modifier.shimmerEffect(
                                Color.LightGray.copy(alpha = 0.7f)
                            ) else Modifier
                    )
                    .then(
                        if (drink.thumbUrl.isNullOrEmpty() || painterState is AsyncImagePainter.State.Error)
                            Modifier.padding(10.dp) else Modifier
                    ),
                contentScale = if (painterState is AsyncImagePainter.State.Success)
                    ContentScale.Crop else ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = drink.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(0.dp))
            Text(
                text = drink.glass ?: "",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = drink.category ?: "",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Color.DarkGray.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

    }

}