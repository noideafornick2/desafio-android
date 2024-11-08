package com.picpay.desafio.android.data

import com.picpay.desafio.android.data.local.UsersDAO
import com.picpay.desafio.android.data.remote.PicPayApiService
import com.picpay.desafio.android.data.remote.UserDTO
import com.picpay.desafio.android.domain.MainRepository
import com.picpay.desafio.android.domain.UsersModel
import com.picpay.desafio.android.domain.mappers.mapToEntity
import com.picpay.desafio.android.domain.mappers.mapToModel
import com.picpay.desafio.android.util.Constants
import com.picpay.desafio.android.util.Constants.CACHE_EXPIRATION_TIME
import com.picpay.desafio.android.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityScoped
class MainRepositoryImpl @Inject constructor(
    private val api: PicPayApiService,
    private val dao: UsersDAO
) : MainRepository {

    override suspend fun getUsersFromNetwork(): Resource<List<UsersModel>> {
        val response = try {
            withContext(Dispatchers.IO){
                api.getUsers()
            }
        } catch(e: Exception) {
            println("api error: "+e.message.orEmpty())
            return Resource.Error(
                "Data loading has failed, you can try again later"
            )
        }
        insertUsers(response)
        val userModelList = response.map {
            it.mapToModel()
        }
       return Resource.Success(userModelList, Constants.LogMessage.API)
    }

    override suspend fun getUsersFromDB(): List<UsersModel> {
        val usersList: MutableList<UsersModel> = mutableListOf()
        try {
            withContext(Dispatchers.IO) {
                dao.getCachedUsers().map {
                    usersList.add(it.mapToModel())
                }
            }
        } catch (e: Exception) {
            println("db error: "+e.message.orEmpty())
        }
        return usersList.toList()
    }

    override suspend fun getUsers(lastRequestTime: Int, isNetworkWorking: Boolean): Resource<List<UsersModel>>  {
        val currentTime = System.currentTimeMillis().toInt()
        val logMessage: String

        val cache = getUsersFromDB()
        if (
            cache.isNotEmpty() &&
            currentTime - lastRequestTime < CACHE_EXPIRATION_TIME
        ) {
            logMessage = Constants.LogMessage.CACHE
            println(logMessage)
            return Resource.Success(cache, logMessage)
        } else {
            if (cache.isNotEmpty() && !isNetworkWorking) {
                logMessage = Constants.LogMessage.CACHE_WITHOUT_CONNECTION
                println(logMessage)
                return Resource.Success(cache, logMessage)
            } else {
                logMessage = Constants.LogMessage.API
                println(logMessage)
                return getUsersFromNetwork()
            }
        }
    }

    private suspend fun insertUsers(users: List<UserDTO>) {
        dao.clearCachedUsers()
        withContext(Dispatchers.IO) {
            users.map {
                dao.insertUsers(it.mapToEntity())
            }
        }
    }

}