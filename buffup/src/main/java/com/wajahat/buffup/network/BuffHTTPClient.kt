package com.wajahat.buffup.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class BuffHTTPClient {

    private lateinit var dataAccessService: BuffDataAccessService

    init {
        initClient()
    }

    fun getDataAccessService() = dataAccessService

    /** Initialize and configure Retrofit networking library */
    private fun initClient() {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .registerTypeAdapter(Boolean::class.java, GSONIntToBooleanAdapter())
            .registerTypeAdapter(Boolean::class.javaPrimitiveType, GSONIntToBooleanAdapter())
            .create()

        dataAccessService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getHttpClientBuilder())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
            .create(BuffDataAccessService::class.java)
    }

    /** Initialize with configuration of HTTPClient */
    private fun getHttpClientBuilder(): OkHttpClient {
        try {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor { chain ->
                    return@addInterceptor chain.proceed(chain.request())
                }
                .build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    companion object {
        private const val BASE_URL = "https://buffup.proxy.beeceptor.com/"
        private const val CONNECTION_TIME_OUT = 60
        private const val READ_TIME_OUT = 20
        private const val WRITE_TIME_OUT = 20
    }
}