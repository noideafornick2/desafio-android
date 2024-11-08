package com.picpay.desafio.android.domain

import com.picpay.desafio.android.util.Resource

interface MainRepository {
    suspend fun getUsersFromNetwork(): Resource<List<UsersModel>>
    suspend fun getUsersFromDB(): List<UsersModel>
    suspend fun getUsers(lastRequestTime: Int, isNetworkWorking: Boolean): Resource<List<UsersModel>>
}