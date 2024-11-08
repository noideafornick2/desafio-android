package com.picpay.desafio.android.data.remote

import com.picpay.desafio.android.util.Constants
import retrofit2.http.GET

interface PicPayApiService {

    @GET(Constants.Endpoints.USERS_EP)
    suspend fun getUsers(): List<UserDTO>
}