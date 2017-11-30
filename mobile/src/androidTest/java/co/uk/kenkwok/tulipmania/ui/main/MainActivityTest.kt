package co.uk.kenkwok.tulipmania.ui.main

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import co.uk.kenkwok.tulipmania.R.id.*
import co.uk.kenkwok.tulipmania.testutils.RecyclerViewItemCountAssertion.Companion.withItemCount
import co.uk.kenkwok.tulipmania.testutils.RecyclerViewMatcher
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



/**
 * Created by kwokk on 28/11/2017.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule @JvmField val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testTickerListDisplay() {
        // recyclerview
        onView(withId(recyclerView)).check(matches(isDisplayed()))
        onView(withId(recyclerView)).check(withItemCount(3))
    }

    @Test
    fun testIndividualTickerDisplay() {
        onView(withRecyclerView(recyclerView).atPosition(0))
                .check(matches(hasDescendant(allOf(withId(itemExchangeName), withEffectiveVisibility(Visibility.VISIBLE)))))

        onView(withRecyclerView(recyclerView).atPosition(0))
                .check(matches(hasDescendant(allOf(withId(itemPrice), withEffectiveVisibility(Visibility.VISIBLE)))))

        onView(withRecyclerView(recyclerView).atPosition(0))
                .check(matches(hasDescendant(allOf(withId(item24hourHigh), withEffectiveVisibility(Visibility.VISIBLE)))))

        onView(withRecyclerView(recyclerView).atPosition(0))
                .check(matches(hasDescendant(allOf(withId(item24hourLow), withEffectiveVisibility(Visibility.VISIBLE)))))
    }

    fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }
}