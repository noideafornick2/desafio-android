package com.picpay.desafio.android.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.picpay.desafio.android.data.MainRepositoryImpl
import com.picpay.desafio.android.data.local.UsersDAO
import com.picpay.desafio.android.data.local.UsersDatabase
import com.picpay.desafio.android.data.remote.PicPayApiService
import com.picpay.desafio.android.domain.MainRepository
import com.picpay.desafio.android.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUsersDatabase(
        @ApplicationContext app: Context,
        provider: Provider<UsersDAO>
    ) = Room.databaseBuilder(
        app,
        UsersDatabase::class.java,
        Constants.LocalData.DB_NAME
    ).addCallback(UsersDBCallback(provider))
        .build()

    @Singleton
    @Provides
    fun provideUsersDAO(db: UsersDatabase) = db.getYourDao()

    class UsersDBCallback (private val provider: Provider<UsersDAO>) : RoomDatabase.Callback() {

        private val applicationScope = CoroutineScope(SupervisorJob())

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            applicationScope.launch(Dispatchers.IO) { populateDatabase() }
        }

        private suspend fun populateDatabase() {}
    }


    @Provides
    fun provideMainRepository(api: PicPayApiService, dao: UsersDAO): MainRepository {
        return MainRepositoryImpl(api, dao)
    }

    @Singleton
    @Provides
    fun provideOkhttp(): OkHttpClient {
        return OkHttpClient.Builder()
          .build()
    }

    @Singleton
    @Provides
    fun provideApi(okHttp: OkHttpClient): PicPayApiService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.Endpoints.BASE_URL)
            .client(okHttp)
            .build()
            .create(PicPayApiService::class.java)
    }

}