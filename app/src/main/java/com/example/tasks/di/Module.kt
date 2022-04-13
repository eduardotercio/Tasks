package com.example.tasks.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.tasks.util.constants.TaskConstants.DATABASE.DATABASE_NAME
import com.example.tasks.data.local.AppDatabase
import com.example.tasks.data.model.HeaderModel
import com.example.tasks.data.model.TaskModel
import com.example.tasks.data.remote.AuthServiceAPI
import com.example.tasks.data.remote.PrioritySerivceAPI
import com.example.tasks.data.remote.TaskServiceAPI
import com.example.tasks.util.constants.TaskConstants.SHARED
import com.example.tasks.util.constants.TaskConstants.HEADER
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    // Dependências para o Room
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun providePriorityDAO(database: AppDatabase) = database.priorityDao()

    // Dependência para o Shared Preferences
    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences("taskShared", Context.MODE_PRIVATE)

    // Dependências para o Retrofit e requisições API
//    @Qualifier
//    @Retention(AnnotationRetention.BINARY)
//    annotation class AuthInterceptorOkHttpClient
//
//    @Qualifier
//    @Retention(AnnotationRetention.BINARY)
//    annotation class OtherInterceptorOkHttpClient

    @Singleton
    @Provides
    fun provideOkHttpClient(
        shared: SharedPreferences
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
        client.addInterceptor(logging)
            .addInterceptor(Interceptor { chain ->
                val token = shared.getString(SHARED.TOKEN_KEY, "")
                val personKey = shared.getString(SHARED.PERSON_KEY, "")
                val request = chain.request().newBuilder()
                    .addHeader(HEADER.PERSON_KEY, personKey!!)
                    .addHeader(HEADER.TOKEN_KEY, token!!)
                    .build()
                chain.proceed(request)
            })
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
    fun provideAuthServiceApi(retrofit: Retrofit): AuthServiceAPI {
        return retrofit.create(AuthServiceAPI::class.java)
    }

    @Singleton
    @Provides
    fun providePriotiryServiceApi(retrofit: Retrofit): PrioritySerivceAPI {
        return retrofit.create(PrioritySerivceAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideTaskServiceApi(retrofit: Retrofit): TaskServiceAPI {
        return retrofit.create(TaskServiceAPI::class.java)
    }

}