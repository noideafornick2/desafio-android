package com.picpay.desafio.android.di

import android.content.Context
import androidx.room.Room
import com.picpay.desafio.android.data.local.UsersDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideListTypeDatabase(
        @ApplicationContext app: Context
    ) = Room.inMemoryDatabaseBuilder(
        app,
        UsersDatabase::class.java,
    ).allowMainThreadQueries()
        .build()
}
