package br.eng.pedro_mendes.modulo_iv.data.remote

import android.util.Log
import br.eng.pedro_mendes.modulo_iv.utils.LocalDateAdapter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class RetrofitClient {
    val studentRepository: StudentRepository


    companion object {
        private var instance: RetrofitClient? = null

        @Synchronized
        fun getInstance(): RetrofitClient {
            if (instance == null) {
                instance = RetrofitClient()
            }

            return instance as RetrofitClient
        }
    }

    init {
        val gson = GsonBuilder().registerTypeAdapter(
            LocalDate::class.java,
            LocalDateAdapter().nullSafe()
        ).create()

        val retrofit = Retrofit.Builder()
            .client(createHttpClient())
            .baseUrl("http://igtiandroid.ddns.net:8080")
            .addConverterFactory(
                GsonConverterFactory.create(gson)
            )
            .build()

        studentRepository = retrofit.create(StudentRepository::class.java)
    }

    private fun createHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15L, TimeUnit.SECONDS)
        .readTimeout(15L, TimeUnit.SECONDS)
        .writeTimeout(15L, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor {
            Log.d("APP", it)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addNetworkInterceptor(HttpLoggingInterceptor {
            Log.d("NETWORK", it)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
}