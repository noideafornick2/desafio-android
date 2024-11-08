package com.picpay.desafio.android.app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.picpay.desafio.android.domain.MainRepository
import com.picpay.desafio.android.domain.UsersModel
import com.picpay.desafio.android.util.Resource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class MainActivityViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainActivityViewModel
    @Mock
    private lateinit var repository: MainRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        MockitoAnnotations.initMocks(this)
        viewModel = MainActivityViewModel(repository)
    }

    @Test
    fun `LoadUsers_Should_Return_success`() = runBlocking{
        val mockUsers = listOf(
            UsersModel(
                id = 1,
                name =  "John Doe",
                username = "jhon.doe",
                img = "https://randomuser.me/api/portraits/men/1.jpg"
            )
        )
        val mockSuccess = Resource.Success(mockUsers)

        `when`(repository.getUsers(10000, true)).thenReturn(mockSuccess)

        viewModel.loadUsers(10000, true)

        assertEquals(mockUsers, viewModel.showedUsersList.value)
    }

    @Test
    fun whenGivenAValidUserShouldShowOnlyThatUser() {
        viewModel = MainActivityViewModel(repository)

        val list1 = UsersModel(id = 1, username = "johndoe", name = "Johnny", img = "")
        val list2 = UsersModel(id = 2, username = "johndoe2", name = "Brian", img = "")
        val list3 = UsersModel(id = 3, username = "nick", name = "Nick", img = "")
        viewModel.allUsersList.value = listOf<UsersModel>(
            list1,
            list2,
            list3,
        )

        viewModel.searchUsers("nick")
        assertEquals(viewModel.showedUsersList.value, listOf(list3))
    }

    @Test
    fun whenGivenAnInvalidUserShouldShowOnlyThatUser() {
        viewModel = MainActivityViewModel(repository)

        val list1 = UsersModel(id = 1, username = "johndoe", name = "Johnny", img = "")
        val list2 = UsersModel(id = 2, username = "johndoe2", name = "Brian", img = "")
        val list3 = UsersModel(id = 3, username = "nick", name = "Nick", img = "")
        viewModel.allUsersList.value = listOf<UsersModel>(
            list1,
            list2,
            list3,
        )

        viewModel.searchUsers("Ryan")
        assertEquals(viewModel.showedUsersList.value?.size, 0)
    }
}