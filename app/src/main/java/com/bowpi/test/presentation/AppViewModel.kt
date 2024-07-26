package com.bowpi.test.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bowpi.test.R
import com.bowpi.test.data.common.Status
import com.bowpi.test.data.common.TodoCocktail
import com.bowpi.test.data.remote.model.Drink
import com.bowpi.test.data.repository.DrinksRepository
import com.bowpi.test.data.repository.TodoCocktailsRepository
import com.bowpi.test.presentation.model.toUiModel
import com.bowpi.test.utils.ConnectivityObserver
import com.bowpi.test.utils.Message
import com.bowpi.test.utils.NetworkConnectivityObserver
import com.bowpi.test.utils.TaskStatus
import com.bowpi.test.utils._Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class AppViewModel(
    private val connectivityObserver: NetworkConnectivityObserver,
    private val drinksRepository: DrinksRepository,
    private val todoCocktailsRepository: TodoCocktailsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()

    private val _outputEvent = MutableSharedFlow<OutputEvent>()
    val outputEvent = _outputEvent.asSharedFlow()

    private var _networkStatus = ConnectivityObserver.Status.Available
    private var _drinks: List<Drink> = listOf()

    private var networkObserverJob: Job? = null
    private var searchDrinksJob: Job? = null
    private var getCocktailListJob: Job? = null
    private var getCocktailJob: Job? = null
    private var saveNewCocktailJob: Job? = null

    sealed class OutputEvent {
        class ShowMessage(val message: Message) : OutputEvent()
        data object NetworkUnavailable : OutputEvent()
    }

    init {
        observeNetworkChange()
        getMyCocktailList()
    }

    fun onEvent(inputEvent: AppEvent) {
        when (inputEvent) {
            is AppEvent.SearchDrinks -> searchDrinks(inputEvent.name)
            is AppEvent.SeeDrinkDetails -> getDrink(inputEvent.id)
            is AppEvent.SearchTodoCocktails ->
                getMyCocktailList(
                    query = inputEvent.query,
                    status = inputEvent.status
                )

            is AppEvent.AddDrinkToMyCocktailList ->
                addDrinkToMyCocktailList(inputEvent.drinkId)

            is AppEvent.CreateTodoCocktail ->
                createTodoCocktail(
                    name = inputEvent.name,
                    instructions = inputEvent.instructions
                )

            is AppEvent.UpdateTodoCocktail ->
                updateTodoCocktail(inputEvent.cocktail)

            is AppEvent.CocktailCompleted ->
                updateTodoCocktailToComplete(inputEvent.id)

            is AppEvent.SeeCocktailDetails ->
                loadTodoCocktail(inputEvent.id)
        }
    }

    private fun observeNetworkChange() {
        networkObserverJob?.cancel()
        networkObserverJob = viewModelScope.launch(Dispatchers.IO) {
            connectivityObserver.observe().collectLatest { status ->
                _networkStatus = status
            }
        }

    }

    private suspend fun isNetworkAvailable(): Boolean {
        if (_networkStatus != ConnectivityObserver.Status.Available) {
            _outputEvent.emit(OutputEvent.NetworkUnavailable)
            _outputEvent.emit(
                OutputEvent.ShowMessage(
                    Message.StringResource(R.string.network_unavailable)
                )
            )
            return false
        }
        return true
    }

    private fun searchDrinks(name: String) {
        searchDrinksJob?.cancel()
        searchDrinksJob = viewModelScope.launch {
            if (name.isEmpty()) _state.update { it.copy(drinks = listOf()) }
            if (!isNetworkAvailable()) return@launch
            _state.update { it.copy(isLoading = true) }

            when (val result = drinksRepository.search(name)) {
                is _Result.Error ->
                    _outputEvent.emit(OutputEvent.ShowMessage(result.message))

                is _Result.Success -> {
                    _drinks = result.data
                    _state.update {
                        it.copy(
                            drinks = _drinks.map { drink ->
                                drink.toUiModel()
                            }
                        )
                    }
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun getDrink(id: Int) {
        _state.getAndUpdate { state ->
            state.copy(fullDrink = _drinks.find { it.id == id })
        }
    }

    private fun loadTodoCocktail(id: String) {
        getCocktailJob?.cancel()
        getCocktailJob = viewModelScope.launch {
            when (val result = todoCocktailsRepository.getCocktail(id)) {
                is _Result.Error ->
                    _outputEvent.emit(
                        OutputEvent.ShowMessage(result.message)
                    )

                is _Result.Success ->
                    _state.update { it.copy(fullCocktail = result.data) }
            }
        }
    }

    private fun getMyCocktailList(query: String = "", status: Status = Status.Unspecified) {
        getCocktailListJob?.cancel()
        getCocktailListJob = viewModelScope.launch {
            todoCocktailsRepository.getCocktails(query, status).collectLatest { result ->
                when (result) {
                    is _Result.Error ->
                        _outputEvent.emit(
                            OutputEvent.ShowMessage(result.message)
                        )

                    is _Result.Success ->
                        _state.update {
                            it.copy(
                                myCocktailList = result.data.map { cocktail ->
                                    cocktail.toUiModel()
                                }
                            )
                        }
                }
            }
        }
    }

    private fun addDrinkToMyCocktailList(drinkId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val drink = _drinks.find { it.id == drinkId } ?: run {
                OutputEvent.ShowMessage(
                    Message.StringResource(R.string.drink_not_found)
                )
                return@launch
            }

            val cocktail = TodoCocktail(
                id = UUID.randomUUID().toString(),
                drink = drink,
            )

            checkOnlineUpdateStatus()

            when (val result = todoCocktailsRepository.saveCocktail(cocktail)) {
                is _Result.Error ->
                    _outputEvent.emit(
                        OutputEvent.ShowMessage(result.message)
                    )

                is _Result.Success ->
                    _outputEvent.emit(
                        OutputEvent.ShowMessage(
                            Message.StringResource(R.string.save_successfully)
                        )
                    )

            }
        }
    }

    private fun createTodoCocktail(name: String, instructions: String) {
        saveNewCocktailJob?.cancel()
        saveNewCocktailJob = viewModelScope.launch {
            _state.update { it.copy(saveCocktailTaskStatus = TaskStatus.Default) }
            if (name.isEmpty()) {
                OutputEvent.ShowMessage(
                    Message.StringResource(R.string.name_required)
                )
                _state.update { it.copy(saveCocktailTaskStatus = TaskStatus.Error) }
                return@launch
            }
            if (instructions.isEmpty()) {
                OutputEvent.ShowMessage(
                    Message.StringResource(R.string.drink_instructions_required)
                )
                _state.update { it.copy(saveCocktailTaskStatus = TaskStatus.Error) }
                return@launch
            }

            val cocktail = TodoCocktail(
                id = UUID.randomUUID().toString(),
                drink = Drink(
                    id = Random.nextInt(),
                    name = name,
                    instructions = instructions,
                )
            )

            checkOnlineUpdateStatus()

            when (val result = todoCocktailsRepository.saveCocktail(cocktail)) {
                is _Result.Error -> {
                    _state.update { it.copy(saveCocktailTaskStatus = TaskStatus.Error) }
                    _outputEvent.emit(
                        OutputEvent.ShowMessage(result.message)
                    )
                }

                is _Result.Success -> {
                    _state.update { it.copy(saveCocktailTaskStatus = TaskStatus.Success) }
                    _outputEvent.emit(
                        OutputEvent.ShowMessage(
                            Message.StringResource(R.string.save_successfully)
                        )
                    )
                }

            }
        }
    }

    private fun updateTodoCocktailToComplete(id: String) {
        viewModelScope.launch {
            val cocktail = when (val result = todoCocktailsRepository.getCocktail(id)) {
                is _Result.Error -> {
                    _outputEvent.emit(
                        OutputEvent.ShowMessage(result.message)
                    )
                    return@launch
                }

                is _Result.Success -> result.data
            }
            updateTodoCocktail(cocktail.copy(status = Status.Completed))
        }
    }

    private fun updateTodoCocktail(cocktail: TodoCocktail) {
        viewModelScope.launch {
            checkOnlineUpdateStatus()

            when (val result = todoCocktailsRepository.saveCocktail(cocktail)) {
                is _Result.Error ->
                    _outputEvent.emit(
                        OutputEvent.ShowMessage(result.message)
                    )

                is _Result.Success ->
                    _outputEvent.emit(
                        OutputEvent.ShowMessage(
                            Message.StringResource(R.string.update_successfully)
                        )
                    )

            }
        }
    }

    private suspend fun checkOnlineUpdateStatus() {
        if (!isNetworkAvailable()) {
            _outputEvent.emit(
                OutputEvent.ShowMessage(
                    Message.StringResource(R.string.pending_updates)
                )
            )
        }
    }
}