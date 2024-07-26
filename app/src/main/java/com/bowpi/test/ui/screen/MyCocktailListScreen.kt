package com.bowpi.test.ui.screen

import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bowpi.test.R
import com.bowpi.test.data.common.Status
import com.bowpi.test.presentation.model.SelectableItem
import com.bowpi.test.presentation.model.TodoCocktailUiModel
import com.bowpi.test.presentation.model.toUi
import com.bowpi.test.ui.component.CustomSearchBar
import com.bowpi.test.ui.component.item.TodoCocktailItem
import com.bowpi.test.ui.theme.DarkGreen
import com.bowpi.test.ui.theme.LightBlue

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyCocktailListScreen(
    cocktails: List<TodoCocktailUiModel>,
    onEvent: (MyCocktailListScreenEvent) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    var status by rememberSaveable { mutableStateOf(Status.Unspecified) }

    var openBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var filterItems by rememberSaveable {
        mutableStateOf(listOf<SelectableItem<Status>>())
    }

    LaunchedEffect(status) {
        filterItems = listOf(
            SelectableItem(
                content = Status.Unspecified,
                selected = status == Status.Unspecified
            ),
            SelectableItem(
                content = Status.Pending,
                selected = status == Status.Pending
            ),
            SelectableItem(
                content = Status.Completed,
                selected = status == Status.Completed
            ),
        )
        onEvent(
            MyCocktailListScreenEvent.SearchCocktails(
                query = query,
                status = status
            )
        )
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Box(modifier = Modifier.weight(1f)) {
                        CustomSearchBar(
                            query = query,
                            onQueryChange = {
                                query = it
                                onEvent(
                                    MyCocktailListScreenEvent.SearchCocktails(
                                        query = query,
                                        status = status
                                    )
                                )
                            },
                            onSearch = {
                                onEvent(
                                    MyCocktailListScreenEvent.SearchCocktails(
                                        query = query,
                                        status = status
                                    )
                                )
                            },
                            onClear = {
                                query = ""
                                onEvent(
                                    MyCocktailListScreenEvent.SearchCocktails(
                                        query = query,
                                        status = status
                                    )
                                )
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(LightBlue)
                            .clickable { openBottomSheet = true }
                            .padding(15.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items = cocktails, key = { it.id }) { cocktail ->
                        Box(
                            modifier = Modifier.animateItemPlacement(
                                spring(
                                    dampingRatio = 0.8f,
                                    stiffness = 100f
                                )
                            )
                        ) {
                            TodoCocktailItem(
                                cocktail = cocktail,
                                onCompleted = {
                                    onEvent(
                                        MyCocktailListScreenEvent.CocktailCompleted(cocktail.id)
                                    )
                                }
                            ) {
                                onEvent(
                                    MyCocktailListScreenEvent.SeeCocktailDetails(cocktail.id)
                                )
                            }
                        }

                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        if (openBottomSheet)
            ModalBottomSheet(
                sheetState = sheetState,
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                tonalElevation = 15.dp,
                onDismissRequest = {
                    openBottomSheet = false
                }
            ) {
                val density = LocalDensity.current
                val windowInsets = WindowInsets.navigationBars
                val navBarHeightDp = with(density) { windowInsets.getBottom(density).toDp() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    filterItems.forEach { item ->
                        FilterItem(item = item) {
                            status = item.content
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp + navBarHeightDp))
                }

            }
    }

}

@Composable
private fun FilterItem(item: SelectableItem<Status>, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth(0.55f)
            .clip(RoundedCornerShape(35.dp))
            .then(
                if (item.selected)
                    Modifier.border(
                        border = BorderStroke(2.dp, DarkGreen),
                        shape = RoundedCornerShape(35.dp)
                    )
                else Modifier
            )
            .background(LightBlue)
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = item.content.toUi().iconResId),
            contentDescription = null,
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = stringResource(id = item.content.toUi().titleResId),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
    }

}

sealed class MyCocktailListScreenEvent {
    class SearchCocktails(val query: String, val status: Status) : MyCocktailListScreenEvent()
    class SeeCocktailDetails(val id: String) : MyCocktailListScreenEvent()
    class CocktailCompleted(val id: String) : MyCocktailListScreenEvent()
}