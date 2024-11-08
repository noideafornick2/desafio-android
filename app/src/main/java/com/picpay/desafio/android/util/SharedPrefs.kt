package com.picpay.desafio.android.util

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

object SharedPrefs{
    private lateinit var sharedPreferences: SharedPreferences

    fun init(ctx: Context) {
        sharedPreferences =
            ctx.getSharedPreferences(
                Constants.SharedPrefs.SHARED_PREFS_FILE,
                Context.MODE_PRIVATE
            )
    }

    fun load(key: String, value: Int): Int {
        return sharedPreferences.getInt(key, value)
    }

    fun save(key: String, value: Int) {
        val editor: Editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }
}
