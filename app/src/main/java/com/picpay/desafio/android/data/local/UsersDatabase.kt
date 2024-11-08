package com.picpay.desafio.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UsersEntity::class],
    version = 1,
    exportSchema = false
)
abstract class UsersDatabase : RoomDatabase() {

    abstract fun getYourDao(): UsersDAO

}
