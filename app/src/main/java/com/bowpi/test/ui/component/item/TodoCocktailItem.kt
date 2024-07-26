package com.bowpi.test.ui.component.item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.bowpi.test.data.common.Status
import com.bowpi.test.presentation.model.TodoCocktailUiModel
import com.bowpi.test.ui.theme.DarkGreen
import com.bowpi.test.ui.theme.LightBlue
import com.bowpi.test.ui.theme.LightGreen
import com.bowpi.test.ui.theme.MediumGreen
import com.bowpi.test.utils.shimmerEffect


@Composable
fun TodoCocktailItem(
    cocktail: TodoCocktailUiModel,
    onCompleted: () -> Unit,
    onClick: () -> Unit,
) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = cocktail.thumbUrl)
            .apply(block = fun ImageRequest.Builder.() {
                error(R.drawable.ic_drink)
                crossfade(1000)
            }).build()
    )
    val painterState = painter.state

    val backgroundColor by animateColorAsState(
        targetValue = if (cocktail.status == Status.Pending)
            LightBlue.copy(alpha = 0.7f)
        else DarkGreen,
        animationSpec = tween(500)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(backgroundColor)
            .animateContentSize()
            .clickable { onClick() }
            .padding(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (cocktail.status == Status.Pending)
                        DarkGreen else Color.White
                ),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = if (cocktail.thumbUrl.isNullOrEmpty())
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
                        if (cocktail.thumbUrl.isNullOrEmpty() || painterState is AsyncImagePainter.State.Error)
                            Modifier.padding(10.dp) else Modifier
                    ),
                contentScale = if (painterState is AsyncImagePainter.State.Success)
                    ContentScale.Crop else ContentScale.Fit,
                colorFilter = if (cocktail.thumbUrl.isNullOrEmpty())
                    ColorFilter.tint(
                        if (cocktail.status == Status.Pending)
                            Color.White else DarkGreen
                    )
                else null,
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cocktail.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (cocktail.status == Status.Pending)
                        Color.Black else Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(0.dp))
                Text(
                    text = cocktail.glass ?: "",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (cocktail.status == Status.Pending)
                        Color.DarkGray.copy(alpha = 0.7f) else Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = cocktail.rating,
                        fontSize = 17.sp,
                        color = if (cocktail.status == Status.Pending)
                            Color.Black else Color.White,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 1.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = null,
                        tint = if (cocktail.status == Status.Pending)
                            MediumGreen else LightGreen,
                        modifier = Modifier.size(17.dp)

                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            AnimatedVisibility(
                visible = cocktail.status == Status.Pending,
                enter = expandIn(),
                exit = fadeOut()
            ) {
                Box(modifier = Modifier.fillMaxHeight()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(y = (-15).dp)
                            .clip(CircleShape)
                            .background(DarkGreen)
                            .clickable { onCompleted() }
                            .padding(7.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = Color.White
                        )
                    }

                    Text(
                        text = cocktail.datetime.date,
                        modifier = Modifier.align(Alignment.BottomEnd),
                        color = Color.Black,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

    }
}