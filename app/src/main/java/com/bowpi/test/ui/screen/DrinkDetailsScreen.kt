package com.bowpi.test.ui.screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.bowpi.test.R
import com.bowpi.test.data.remote.model.Drink
import com.bowpi.test.ui.component.ExpandedFloatingButton
import com.bowpi.test.utils.shimmerEffect

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DrinkDetailsScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    drink: Drink,
    onSave: () -> Unit
) {
    var isFloatingButtonExpanded by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    var previousScrollOffset by remember { mutableIntStateOf(0) }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .collect { currentScrollOffset ->
                if (currentScrollOffset > previousScrollOffset) {
                    // Scrolled down
                    isFloatingButtonExpanded = false
                } else if (currentScrollOffset < previousScrollOffset) {
                    // Scrolled up
                    isFloatingButtonExpanded = true
                }
                previousScrollOffset = currentScrollOffset
            }
    }

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = drink.thumbUrl)
            .apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
                error(R.drawable.ic_drink)
                crossfade(1000)
            }).build()
    )
    val painterState = painter.state

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Image(
                painter = if (drink.thumbUrl.isNullOrEmpty())
                    painterResource(id = R.drawable.ic_drink)
                else painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        when (painterState) {
                            is AsyncImagePainter.State.Loading ->
                                Modifier
                                    .height(400.dp)
                                    .shimmerEffect(Color.LightGray.copy(alpha = 0.7f))

                            is AsyncImagePainter.State.Error -> Modifier.height(400.dp)
                            is AsyncImagePainter.State.Success -> Modifier
                            else -> Modifier
                        }
                    )
                ,
                contentScale = if (painterState is AsyncImagePainter.State.Success)
                    ContentScale.FillWidth else ContentScale.Inside
            )

            Column(modifier = Modifier.padding(15.dp)) {
                Column {
                    Text(
                        text = drink.name,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(0.dp))
                    Text(
                        text = drink.glass ?: "",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = drink.category ?: "",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.DarkGray.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.ingredients),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = drink.textIngredients,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.instructions),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = drink.instructions ?: "",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(90.dp))
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 15.dp)
        ) {
            ExpandedFloatingButton(
                textResId = R.string.save,
                iconResId = R.drawable.ic_bookmark_fill,
                expanded = isFloatingButtonExpanded,
            ) {
                onSave()
            }
        }
    }


}

