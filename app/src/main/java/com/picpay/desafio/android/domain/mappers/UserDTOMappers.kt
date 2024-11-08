package com.picpay.desafio.android.domain.mappers

import com.picpay.desafio.android.data.local.UsersEntity
import com.picpay.desafio.android.data.remote.UserDTO
import com.picpay.desafio.android.domain.UsersModel

fun UserDTO.mapToEntity(): UsersEntity {
    return UsersEntity(
        img = this.img,
        name = this.name,
        idAPI = this.id,
        username = this.username
    )
}

fun UserDTO.mapToModel(): UsersModel {
    return UsersModel(
        img = this.img,
        name = this.name,
        id = this.id,
        username = this.username
    )
}