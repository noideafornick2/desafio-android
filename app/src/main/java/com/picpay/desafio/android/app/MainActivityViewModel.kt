package com.picpay.desafio.android.app

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.domain.MainRepository
import com.picpay.desafio.android.domain.UsersModel
import com.picpay.desafio.android.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    @VisibleForTesting
    val allUsersList : MutableLiveData<List<UsersModel>> = MutableLiveData()
    val showedUsersList : MutableLiveData<List<UsersModel>> = MutableLiveData()
    val errorMessage : MutableLiveData<String> = MutableLiveData("")
    val isLoading = MutableLiveData(true)
    var isSearching = false

    fun loadUsers(lastRequestTime: Int = 0, isNetworkWorking: Boolean){
        viewModelScope.launch {
            val result = repository.getUsers(lastRequestTime, isNetworkWorking)
            when(result) {
                is Resource.Success -> {
                    result.data.let {
                        showedUsersList.value = it
                        allUsersList.value = it
                    }
                    isLoading.postValue(false)
                }
                is Resource.Error -> {
                    errorMessage.value = result.message.orEmpty()
                }
                else -> {}
            }
        }
    }

    fun searchUsers(username: String){
        val usersFounded: MutableList<UsersModel> = mutableListOf()
        allUsersList.value?.map {
            if (it.username.lowercase().contains(username.lowercase()) ||
            it.name.lowercase().contains(username)) {
                usersFounded.add(it)
            }
        }
        showedUsersList.value = usersFounded
    }

}