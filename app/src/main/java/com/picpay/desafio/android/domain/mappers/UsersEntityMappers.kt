package com.picpay.desafio.android.domain.mappers

import com.picpay.desafio.android.data.local.UsersEntity
import com.picpay.desafio.android.domain.UsersModel

fun UsersEntity.mapToModel(): UsersModel {
    return UsersModel(
        img = this.img,
        name = this.name,
        id = this.idAPI,
        username = this.username
    )
}