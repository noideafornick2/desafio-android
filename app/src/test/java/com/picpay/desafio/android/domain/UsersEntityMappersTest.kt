package com.picpay.desafio.android.domain

import com.picpay.desafio.android.data.local.UsersEntity
import com.picpay.desafio.android.data.remote.UserDTO
import com.picpay.desafio.android.domain.mappers.mapToEntity
import com.picpay.desafio.android.domain.mappers.mapToModel
import junit.framework.TestCase
import org.junit.Test

class UsersEntityMappersTest {
    @Test
    fun `Should correctly map userEntity to userModel`() {
        val userEntity = UsersEntity(
            idAPI = 1,
            name =  "John Doe",
            username = "jhon.doe",
            img = "https://randomuser.me/api/portraits/men/1.jpg"
        )
        val expectedUser = UsersModel(
            id = 1,
            name =  "John Doe",
            username = "jhon.doe",
            img = "https://randomuser.me/api/portraits/men/1.jpg"
        )

        val mappedUser = userEntity.mapToModel()

        TestCase.assertEquals(mappedUser, expectedUser)
    }
}