package com.bowpi.test.ui.screen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.bowpi.test.R
import com.bowpi.test.data.common.TodoCocktail
import com.bowpi.test.ui.component.ExpandedFloatingButton
import com.bowpi.test.ui.theme.DarkGreen
import com.bowpi.test.ui.theme.quicksand
import com.bowpi.test.utils.Message
import com.bowpi.test.utils.shimmerEffect
import com.bowpi.test.utils.toast
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCocktailScreen(cocktail: TodoCocktail, onSave: (TodoCocktail) -> Unit) {

    val ctx = LocalContext.current
    var name by rememberSaveable {
        mutableStateOf(cocktail.drink.name)
    }

    var glass by rememberSaveable {
        mutableStateOf(cocktail.drink.glass ?: "")
    }

    var category by rememberSaveable {
        mutableStateOf(cocktail.drink.category ?: "")
    }

    var ingredientsAndMeasures by rememberSaveable {
        mutableStateOf(cocktail.drink.ingredientsAndMeasures)
    }

    var ingredientsText by rememberSaveable {
        mutableStateOf(cocktail.drink.textIngredients)
    }

    var instructions by rememberSaveable {
        mutableStateOf(cocktail.drink.instructions ?: "")
    }

    var rating by rememberSaveable {
        mutableIntStateOf(cocktail.rating)
    }

    LaunchedEffect(cocktail.drink.name) {
        name = cocktail.drink.name
    }
    LaunchedEffect(cocktail.drink.glass) {
        glass = cocktail.drink.glass?:""
    }
    LaunchedEffect(cocktail.drink.category) {
        category = cocktail.drink.category?:""
    }
    LaunchedEffect(cocktail.drink.ingredientsAndMeasures) {
        ingredientsAndMeasures = cocktail.drink.ingredientsAndMeasures
    }
    LaunchedEffect(cocktail.drink.textIngredients) {
        ingredientsText = cocktail.drink.textIngredients
    }
    LaunchedEffect(cocktail.drink.instructions) {
        instructions = cocktail.drink.instructions?:""
    }
    LaunchedEffect(cocktail.rating) { rating = cocktail.rating }

    var dialog by remember {
        mutableStateOf(EditCocktailDialog.AddIngredient)
    }

    LaunchedEffect(ingredientsAndMeasures.size) {
        val stringBuilder = StringBuilder()
        for ((ingredient, measure) in ingredientsAndMeasures) {
            stringBuilder.append("• $measure $ingredient\n")
        }
        ingredientsText = stringBuilder.toString()
    }

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

    var openBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()


    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = cocktail.drink.thumbUrl)
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
                painter = if (cocktail.drink.thumbUrl.isNullOrEmpty())
                    painterResource(id = R.drawable.ic_drink)
                else painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray.copy(alpha = 0.3f))
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
                    ),
                contentScale = if (painterState is AsyncImagePainter.State.Success)
                    ContentScale.FillWidth else ContentScale.Inside
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {

                Text(
                    text = stringResource(id = R.string.name),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray.copy(alpha = 1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    textStyle = TextStyle(
                        fontFamily = quicksand,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.glass),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray.copy(alpha = 1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))

                TextField(
                    value = glass,
                    onValueChange = { glass = it },
                    textStyle = TextStyle(
                        fontFamily = quicksand,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(id = R.string.category),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray.copy(alpha = 1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))

                TextField(
                    value = category,
                    onValueChange = { category = it },
                    textStyle = TextStyle(
                        fontFamily = quicksand,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(id = R.string.ingredients),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray.copy(alpha = 1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))
                TextField(
                    value = ingredientsText,
                    onValueChange = {},
                    enabled = false,
                    textStyle = TextStyle(
                        fontFamily = quicksand,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray)
                        .clickable {
                            dialog = EditCocktailDialog.AddIngredient
                            openBottomSheet = true
                        },
                    minLines = 10
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(id = R.string.instructions),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray.copy(alpha = 1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))
                TextField(
                    value = instructions,
                    onValueChange = { instructions = it },
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
                    minLines = 10
                )
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(DarkGreen)
                        .clickable {
                            dialog = EditCocktailDialog.UpdateRating
                            openBottomSheet = true
                        }
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = rating.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(bottom = 1.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.rating),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )

                }
            }
            Spacer(modifier = Modifier.height(90.dp))
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 15.dp)
        ) {
            ExpandedFloatingButton(
                textResId = R.string.done,
                iconResId = R.drawable.ic_done,
                expanded = isFloatingButtonExpanded,
            ) {
                if (name.isEmpty()) {
                    ctx.toast(Message.StringResource(R.string.name_required))
                    return@ExpandedFloatingButton
                }
                onSave(
                    cocktail.copy(
                        drink = cocktail.drink.copy(
                            name = name,
                            glass = glass,
                            category = category,
                            instructions = instructions,
                            ingredientsAndMeasures = ingredientsAndMeasures
                        ),
                        rating = rating,
                    )
                )
            }
        }
    }

    var ingredient by remember { mutableStateOf("") }
    var measure by remember { mutableStateOf("") }

    if (openBottomSheet)
        ModalBottomSheet(
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            tonalElevation = 15.dp,
            onDismissRequest = {
                openBottomSheet = false
                ingredient = ""
                measure = ""
            }
        ) {
            when (dialog) {
                EditCocktailDialog.AddIngredient ->
                    AddIngredientDialog(
                        ingredient = ingredient,
                        measure = measure,
                        onChangeIngredient = { ingredient = it },
                        onChangeMeasure = { measure = it }
                    ) {
                        if (ingredient.isEmpty()) {
                            ctx.toast(Message.StringResource(R.string.ingredient_required))
                            return@AddIngredientDialog
                        }
                        ingredientsAndMeasures[ingredient] = measure
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            openBottomSheet = false
                            ingredient = ""
                            measure = ""
                        }
                    }

                EditCocktailDialog.UpdateRating ->
                    CocktailRatingDialog(rating = rating) {
                        rating = it
                    }
            }
        }


}

private enum class EditCocktailDialog {
    AddIngredient, UpdateRating
}