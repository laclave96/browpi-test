package com.bowpi.test.data.repository

import com.bowpi.test.R
import com.bowpi.test.data.common.Status
import com.bowpi.test.data.common.TodoCocktail
import com.bowpi.test.utils.Message
import com.bowpi.test.utils._Result
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TodoCocktailsRepositoryImpl(
    private val cocktailsDataRef: DatabaseReference
) : TodoCocktailsRepository {

    override suspend fun getCocktail(id: String): _Result<TodoCocktail> =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                cocktailsDataRef.child(id).get()
                    .addOnSuccessListener { snapshot ->
                        val cocktail = snapshot.getValue(TodoCocktail::class.java) ?: run {
                            continuation.resume(
                                _Result.Error(
                                    Message.StringResource(R.string.unexpected_error)
                                )
                            )
                            return@addOnSuccessListener
                        }
                        continuation.resume(_Result.Success(cocktail))
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                        continuation.resume(
                            _Result.Error(
                                Message.StringResource(R.string.get_cocktail_error)
                            )
                        )
                    }
            }
        }

    override fun getCocktails(query: String, status: Status): Flow<_Result<List<TodoCocktail>>> =
        callbackFlow<_Result<List<TodoCocktail>>> {

            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val cocktailList = mutableListOf<TodoCocktail>()
                    dataSnapshot.children.forEach { snapshot ->
                        val cocktail = snapshot.getValue(TodoCocktail::class.java)
                        cocktail?.let {
                            if (it.drink.name.contains(
                                    query,
                                    ignoreCase = true
                                ) && (it.status == status || status == Status.Unspecified)
                            ) cocktailList.add(it)
                        }
                    }
                    launch { send(_Result.Success(cocktailList.sortedBy { it.drink.name })) }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    databaseError.toException().printStackTrace()
                    launch {
                        send(
                            _Result.Error(
                                Message.StringResource(R.string.load_cocktails_error)
                            )
                        )
                    }
                }
            }

            cocktailsDataRef.addValueEventListener(listener)
            awaitClose { cocktailsDataRef.removeEventListener(listener) }
        }.flowOn(Dispatchers.IO)

    override suspend fun saveCocktail(cocktail: TodoCocktail): _Result<Unit> =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                cocktailsDataRef.child(cocktail.id).setValue(cocktail)
                    .addOnSuccessListener {
                        continuation.resume(_Result.Success(Unit))
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                        continuation.resume(
                            _Result.Error(
                                Message.StringResource(R.string.save_cocktail_error)
                            )
                        )
                    }
            }
        }

}