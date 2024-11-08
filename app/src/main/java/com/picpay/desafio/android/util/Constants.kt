package com.picpay.desafio.android.util

object Constants {
    const val CACHE_EXPIRATION_TIME = 15000

    object Endpoints {
        const val BASE_URL = "https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/"
        const val USERS_EP = "users"
    }

    object LocalData {
        const val DB_NAME = "users_database"
        const val TB_NAME = "users_table"
    }

    object SharedPrefs {
        const val SHARED_PREFS_FILE = "SHARED_PREFS_FILE"
        const val LAST_REQUEST = "LAST_REQUEST"
    }

    object LogMessage {
        const val API = "data from: API"
        const val CACHE = "data from: cache"
        const val CACHE_WITHOUT_CONNECTION = "data from: cache without internet"
    }
}