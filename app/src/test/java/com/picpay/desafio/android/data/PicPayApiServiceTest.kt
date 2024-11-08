package com.picpay.desafio.android.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.picpay.desafio.android.util.Constants
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.util.concurrent.TimeUnit


class PicPayApiServiceTest {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    @Test
    fun `Should Have a correct json format`() = runBlocking {
        val mockedResponse = File("src/test/resources/users_response.json").readText()

        val request2 = Request.Builder()
            .url(Constants.Endpoints.BASE_URL+Constants.Endpoints.USERS_EP)
            .build()
        val response = client.newCall(request2).execute()
        val responseBody = response.body?.string()

        val mapper = ObjectMapper()
        val node1 = mapper.readTree(responseBody)
        val node2 = mapper.readTree(mockedResponse)
        assertTrue(node1.equals(node2))
    }

    @Test
    fun `Given valid url When fetching users Then returns returns 200`() {
        val request = Request.Builder()
            .url(Constants.Endpoints.BASE_URL+Constants.Endpoints.USERS_EP)
            .build()
        val response = client.newCall(request).execute()

        TestCase.assertEquals(200, response.code)
    }

    @Test
    fun `Given an invalid url When fetching users Then returns returns 404`() {
        val request = Request.Builder()
            .url(Constants.Endpoints.BASE_URL+"p")
            .build()
        val response = client.newCall(request).execute()

        TestCase.assertEquals(404, response.code)
    }
}