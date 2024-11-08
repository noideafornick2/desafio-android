package com.picpay.desafio.android.app


import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import com.picpay.desafio.android.R
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mainActivityRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun shouldDisplayTitle() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.picpay.desafio.android", appContext.packageName)
    }

    @Test
    fun whenSearchedAValidUserShouldReturnOneResult() {
        Thread.sleep(4000)
        onView(withId(R.id.search_button)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.search_src_text)).perform(typeTextIntoFocusedView("Tod86"))
        Thread.sleep(2000)
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.recyclerView)).check(matches(hasChildCount(1)))
        Thread.sleep(2000)
    }

    @Test
    fun whenSearchedAnInvalidUserShouldReturnZeroResults() {
        Thread.sleep(4000)
        onView(withId(R.id.search_button)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.search_src_text)).perform(typeTextIntoFocusedView("user 404"))
        Thread.sleep(2000)
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.recyclerView)).check(matches(hasChildCount(0)))
        Thread.sleep(2000)
    }

}