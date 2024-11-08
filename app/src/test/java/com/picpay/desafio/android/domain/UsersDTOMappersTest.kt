package com.picpay.desafio.android.domain

import com.picpay.desafio.android.data.local.UsersEntity
import com.picpay.desafio.android.data.remote.UserDTO
import com.picpay.desafio.android.domain.mappers.mapToEntity
import com.picpay.desafio.android.domain.mappers.mapToModel
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UsersDTOMappersTest {
    @Test
    fun `Should correctly map userDTO to userModel`() {
        val userDTO = UserDTO(
            id = 1,
            name = "John Doe",
            username = "jhon.doe",
            img = "https://randomuser.me/api/portraits/men/1.jpg"
        )
        val expectedUser = UsersModel(
            id = 1,
            name =  "John Doe",
            username = "jhon.doe",
            img = "https://randomuser.me/api/portraits/men/1.jpg"
        )

        val mappedUser = userDTO.mapToModel()

        assertEquals(mappedUser, expectedUser)
    }

    @Test
    fun `Should correctly map userDTO to userEntity`() {
        val userDTO = UserDTO(
            id = 1,
            name = "John Doe",
            username = "jhon.doe",
            img = "https://randomuser.me/api/portraits/men/1.jpg"
        )
        val expectedUser = UsersEntity(
            idAPI = 1,
            name =  "John Doe",
            username = "jhon.doe",
            img = "https://randomuser.me/api/portraits/men/1.jpg"
        )

        val mappedUser = userDTO.mapToEntity()

        assertEquals(mappedUser, expectedUser)
    }
}