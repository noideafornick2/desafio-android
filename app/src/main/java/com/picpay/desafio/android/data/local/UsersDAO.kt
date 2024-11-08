package com.picpay.desafio.android.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsersDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(list: UsersEntity)

    @Query("SELECT * FROM users_table")
    suspend fun getCachedUsers(): List<UsersEntity>

    @Query("DELETE FROM users_table WHERE 1 = 1")
    suspend fun clearCachedUsers()

}