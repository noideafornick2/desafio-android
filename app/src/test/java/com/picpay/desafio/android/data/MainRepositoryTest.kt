package com.picpay.desafio.android.data

import com.nhaarman.mockitokotlin2.any
import com.picpay.desafio.android.data.local.UsersDAO
import com.picpay.desafio.android.data.local.UsersEntity
import com.picpay.desafio.android.data.remote.PicPayApiService
import com.picpay.desafio.android.data.remote.UserDTO
import com.picpay.desafio.android.domain.MainRepository
import com.picpay.desafio.android.domain.UsersModel
import com.picpay.desafio.android.domain.mappers.mapToEntity
import com.picpay.desafio.android.domain.mappers.mapToModel
import com.picpay.desafio.android.util.Constants
import com.picpay.desafio.android.util.Constants.CACHE_EXPIRATION_TIME
import com.picpay.desafio.android.util.Resource
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class MainRepositoryTest {

    @Mock
    private lateinit var dao: UsersDAO

    @Mock
    private lateinit var api: PicPayApiService

    private lateinit var repository: MainRepository

    private lateinit var mockUsersDTO: List<UserDTO>
    private lateinit var mockedUsersEntity: List<UsersEntity>
    private lateinit var mockUsersModel: List<UsersModel>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = MainRepositoryImpl(dao = dao, api = api)
        mockUsersDTO = listOf(
            UserDTO(
                id = 0,
                name =  "Sandrine Spinka",
                username = "Tod86",
                img = "https://randomuser.me/api/portraits/men/1.jpg"
            )
        )
        mockedUsersEntity = mockUsersDTO.map { it.mapToEntity() }
        mockUsersModel = mockUsersDTO.map { it.mapToModel() }
    }

    @Test
    fun `GetUsersFromDB Should Work When Requested`() = runTest {
        `when`(dao.getCachedUsers()).thenReturn(mockedUsersEntity)
        val result = runBlocking { repository.getUsersFromDB() }

        assertEquals(result, mockUsersModel)
        verify(api, times(0)).getUsers()// Verify no network interaction
    }

    @Test
    fun `GetUsersFromNetwork Should Work When Requested`() = runTest {
        `when`(api.getUsers()).thenReturn(mockUsersDTO)
        `when`(dao.insertUsers(any())).thenAnswer { invocation ->
            // Simulate successful insertion
            Unit
        }

        val result = runBlocking { repository.getUsersFromNetwork() }

        assertEquals(Resource.Success::class.java, result::class.java)
        assertEquals(mockUsersModel, (result as Resource.Success).data)

        // Verify network interaction
        verify(api).getUsers()
    }

    @Test
    fun `GetUsers Should Come From API When network Available, cacheListFull, lastRequest expired`() = runBlocking {
        `when`(dao.getCachedUsers()).thenReturn(mockedUsersEntity)
        `when`(api.getUsers()).thenReturn(mockUsersDTO)
        `when`(dao.insertUsers(any())).thenAnswer { _ -> }

        val currentTime = System.currentTimeMillis().toInt()
        val lastRequestExpired = currentTime - CACHE_EXPIRATION_TIME

        val result = runBlocking {
            repository.getUsers(lastRequestExpired, true)
        }
        assertEquals(
            Constants.LogMessage.API,
            (result as Resource.Success).message.orEmpty()
        )
    }

    @Test
    fun `GetUsers Should Come From DB When network Available, cacheListFull, lastRequest Not Expired`() = runBlocking {
        `when`(dao.getCachedUsers()).thenReturn(mockedUsersEntity)
        `when`(api.getUsers()).thenReturn(mockUsersDTO)
        `when`(dao.insertUsers(any())).thenAnswer { _ -> }

        val currentTime = System.currentTimeMillis().toInt()

        val result = runBlocking {
            repository.getUsers(currentTime, true)
        }
        assertEquals(
            Constants.LogMessage.CACHE,
            (result as Resource.Success).message.orEmpty()
        )
    }

    @Test
    fun `GetUsers Should Come From DB When network Unavailable, cacheListFull, lastRequest Expired`() = runBlocking {
        `when`(dao.getCachedUsers()).thenReturn(mockedUsersEntity)
        `when`(api.getUsers()).thenReturn(mockUsersDTO)
        `when`(dao.insertUsers(any())).thenAnswer { _ -> }

        val currentTime = System.currentTimeMillis().toInt()
        val lastRequestExpired = currentTime - CACHE_EXPIRATION_TIME

        val result = runBlocking {
            repository.getUsers(lastRequestExpired, false)
        }
        assertEquals(
            Constants.LogMessage.CACHE_WITHOUT_CONNECTION,
            (result as Resource.Success).message.orEmpty()
        )
    }

}