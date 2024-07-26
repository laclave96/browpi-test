package com.bowpi.test

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.bowpi.test.presentation.AppViewModel
import com.bowpi.test.ui.screen.MainScreen
import com.bowpi.test.ui.theme.BowpiTestTheme
import com.bowpi.test.utils.toast
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            BowpiTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val appViewModel by viewModel<AppViewModel>()
                    val appState by appViewModel.state.collectAsStateWithLifecycle()

                    MainScreen(state = appState, onEvent = appViewModel::onEvent)

                    LaunchedEffect(Unit) {
                        appViewModel.outputEvent.flowWithLifecycle(
                            lifecycle,
                            Lifecycle.State.STARTED
                        ).collectLatest { outputEvent ->
                            when (outputEvent) {
                                AppViewModel.OutputEvent.NetworkUnavailable -> {}
                                is AppViewModel.OutputEvent.ShowMessage ->
                                    toast(outputEvent.message)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BowpiTestTheme {
        Greeting("Android")
    }
}