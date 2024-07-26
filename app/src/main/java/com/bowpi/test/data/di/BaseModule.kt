package com.bowpi.test.data.di

import com.bowpi.test.utils.NetworkConnectivityObserver
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit

val baseModule = module {

    single {
        NetworkConnectivityObserver(androidContext())
    }

    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(
            /*HttpLoggingInterceptor.Level.HEADERS*/
            HttpLoggingInterceptor.Level.NONE
        )
    }

    single {
        OkHttpClient.Builder().addInterceptor(get<HttpLoggingInterceptor>()).build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://your_base_url")
            .client(get())
            .build()
    }

    single<FirebaseDatabase> {
        Firebase.database.setPersistenceEnabled(true)
        Firebase.database
    }
}