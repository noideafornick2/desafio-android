package com.picpay.desafio.android.data.local

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.picpay.desafio.android.util.Constants

@Entity(tableName = Constants.LocalData.TB_NAME)
data class UsersEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val idAPI: Int,
    val img: String,
    val name: String,
    val username: String

) {
  @Ignore
  constructor(
      idAPI: Int,
      name: String,
      img: String,
      username: String
  )
  :
  this(
      id = 0,
      idAPI = idAPI,
      name = name,
      img = img,
      username = username
  )
}
