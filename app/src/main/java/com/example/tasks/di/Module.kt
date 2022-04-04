package com.example.tasks.di

import android.content.Context
import android.content.SharedPreferences
import com.example.tasks.data.remote.UserServiceAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences("taskShared", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
        client.addInterceptor(logging)
//            .addInterceptor(Interceptor { chain ->
//                val request = chain.request().newBuilder()
//                    .addHeader("Accept", "application/json")
//                    .addHeader("app_id", Constants.APP_ID)
//                    .addHeader("app_key", Constants.APP_KEY)
//                    .build()
//                chain.proceed(request)
//            })
        return client.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://devmasterteam.com/CursoAndroidAPI/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideServiceApi(retrofit: Retrofit): UserServiceAPI {
        return retrofit.create(UserServiceAPI::class.java)
    }

}