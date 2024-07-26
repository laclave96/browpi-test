package com.bowpi.test.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bowpi.test.R
import com.bowpi.test.presentation.model.DrinkUiModel
import com.bowpi.test.ui.component.CustomSearchBar
import com.bowpi.test.ui.component.ExpandedFloatingButton
import com.bowpi.test.ui.component.item.DrinkItem
import com.bowpi.test.ui.theme.DarkGreen
import com.bowpi.test.utils.Message
import com.bowpi.test.utils.TaskStatus
import com.bowpi.test.utils.toast
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalSharedTransitionApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DrinksScreen(
    drinks: List<DrinkUiModel>,
    isLoading: Boolean,
    saveCocktailTaskStatus: TaskStatus,
    onEvent: (DrinkScreenEvent) -> Unit,
) {
    val ctx = LocalContext.current
    var query by rememberSaveable { mutableStateOf("") }
    var isFloatingButtonExpanded by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()

    var previousIndex by remember { mutableIntStateOf(0) }
    var previousScrollOffset by remember { mutableIntStateOf(0) }

    var openBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .map { it }
            .distinctUntilChanged()
            .collect { (index, offset) ->
                if (index > previousIndex || (index == previousIndex && offset > previousScrollOffset)) {
                    // Scrolled down
                    isFloatingButtonExpanded = false
                } else if (index < previousIndex || offset < previousScrollOffset) {
                    // Scrolled up
                    isFloatingButtonExpanded = true
                }
                previousIndex = index
                previousScrollOffset = offset
            }
    }

    LaunchedEffect(saveCocktailTaskStatus) {
        if (saveCocktailTaskStatus == TaskStatus.Success)
            launch {
                sheetState.hide()
            }.invokeOnCompletion {
                openBottomSheet = false
            }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading)
            CircularProgressIndicator(
                modifier = Modifier
                    .width(70.dp)
                    .align(Alignment.Center)
                    .offset(y = (-50).dp),
                color = DarkGreen,
                trackColor = Color.LightGray.copy(alpha = 0.5f),
            )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomSearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { onEvent(DrinkScreenEvent.SearchDrinks(it)) },
                onClear = { query = "" }
            )
            Spacer(modifier = Modifier.height(5.dp))

            LazyColumn(
                state = listState,
            ) {
                items(items = drinks, key = { it.id }) { drink ->
                    Box(
                        modifier = Modifier
                            .animateItemPlacement(
                                spring(
                                    dampingRatio = 0.8f,
                                    stiffness = 100f
                                )
                            )
                    ) {
                        DrinkItem(
                            drink = drink
                        ) {
                            onEvent(DrinkScreenEvent.SeeDrink(drink.id))
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 15.dp)
        ) {
            ExpandedFloatingButton(
                textResId = R.string.add,
                iconResId = R.drawable.ic_add,
                expanded = isFloatingButtonExpanded
            ) {
                openBottomSheet = true
            }
        }
    }
    var name by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }

    if (openBottomSheet)
        ModalBottomSheet(
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            tonalElevation = 15.dp,
            onDismissRequest = {
                openBottomSheet = false
                name = ""
                instructions = ""
            }
        ) {
            AddNewCocktailDialog(
                name = name,
                instructions = instructions,
                onChangeName = { name = it },
                onChangeInstructions = { instructions = it }
            ) {
                if (name.isEmpty()) {
                    ctx.toast(Message.StringResource(R.string.name_required))
                    return@AddNewCocktailDialog
                }
                if (instructions.isEmpty()) {
                    ctx.toast(Message.StringResource(R.string.drink_instructions_required))
                    return@AddNewCocktailDialog
                }
                onEvent(DrinkScreenEvent.SaveNewCocktail(name, instructions))
                name = ""
                instructions = ""
            }
        }


}


sealed class DrinkScreenEvent() {
    class SeeDrink(val id: Int) : DrinkScreenEvent()
    class SearchDrinks(val name: String) : DrinkScreenEvent()
    class SaveNewCocktail(val name: String, val instructions: String) : DrinkScreenEvent()
}