package com.picpay.desafio.android.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.picpay.desafio.android.data.local.UsersDAO
import com.picpay.desafio.android.data.local.UsersDatabase
import com.picpay.desafio.android.data.local.UsersEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
@HiltAndroidTest
class UsersDAOTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database : UsersDatabase
    private lateinit var dao : UsersDAO

    @Before
    fun setup() {
        /*database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UsersDatabase::class.java,
        ).allowMainThreadQueries().build()*/
        hiltAndroidRule.inject()
        dao = database.getYourDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun shouldInsertAUserIntoTheDB() {
        runBlocking {
            val user = UsersEntity(
                idAPI = 1,
                name = "Jhon doe",
                img = "https://randomuser.me/api/portraits/men/1.jpg",
                username = "jhon.doe"
            )
            dao.insertUsers(user)
            val insertedLists = dao.getCachedUsers()
            assertThat(insertedLists.contains(user))
        }
    }

    @Test
    fun shouldDeleteUserFromDB() {
        runBlocking {
            val user = UsersEntity(
                idAPI = 1,
                name = "Jhon doe",
                img = "https://randomuser.me/api/portraits/men/1.jpg",
                username = "jhon.doe"
            )
            dao.insertUsers(user)
            dao.clearCachedUsers()
            val allLists = dao.getCachedUsers()
            assertThat(allLists).doesNotContain(user)
        }
    }

    @Test
    fun shouldReturnInsertedUser() {
        runBlocking {
            val user = UsersEntity(
                idAPI = 1,
                name = "Jhon doe",
                img = "https://randomuser.me/api/portraits/men/1.jpg",
                username = "jhon.doe"
            )
            dao.insertUsers(user)
            val allLists = dao.getCachedUsers()
            assertThat(allLists[0].name).isEqualTo("Jhon doe")
        }
    }
}