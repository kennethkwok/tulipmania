package co.uk.kenkwok.tulipmania.ui.main

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiSelector
import co.uk.kenkwok.tulipmania.R
import co.uk.kenkwok.tulipmania.R.id.*
import co.uk.kenkwok.tulipmania.models.ExchangeName
import co.uk.kenkwok.tulipmania.testutils.RecyclerViewItemCountAssertion.Companion.withItemCount
import co.uk.kenkwok.tulipmania.testutils.RecyclerViewMatcher
import co.uk.kenkwok.tulipmania.testutils.WaitForViewMatcher.waitForView
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kwokk on 28/11/2017.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val NUMBER_OF_ITEMS = 7

    @Rule @JvmField val activityRule = ActivityTestRule(MainActivity::class.java)
    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getTargetContext()
    }

    @Test
    fun testTickerListDisplay() {
        // recyclerview
        waitForView(withId(recyclerView), 10).check(matches(isDisplayed()))
        waitForView(withId(recyclerView), 10).check(withItemCount(NUMBER_OF_ITEMS))
    }

    @Test
    fun testTickerSectionHeadingDisplay() {
        onView(withRecyclerView(recyclerView).atPosition(0))
                .check(matches(hasDescendant(allOf(
                        withId(sectionHeading),
                        withEffectiveVisibility(Visibility.VISIBLE),
                        withText(context.getString(R.string.btc_heading))
                ))))
    }

    @Test
    fun testIndividualTickerDisplay() {
        onView(withRecyclerView(recyclerView).atPosition(1))
                .check(matches(hasDescendant(allOf(withId(itemExchangeName), withEffectiveVisibility(Visibility.VISIBLE)))))

        onView(withRecyclerView(recyclerView).atPosition(1))
                .check(matches(hasDescendant(allOf(withId(itemPrice), withEffectiveVisibility(Visibility.VISIBLE)))))

        onView(withRecyclerView(recyclerView).atPosition(1))
                .check(matches(hasDescendant(allOf(withId(item24hourHigh), withEffectiveVisibility(Visibility.VISIBLE)))))

        onView(withRecyclerView(recyclerView).atPosition(1))
                .check(matches(hasDescendant(allOf(withId(item24hourLow), withEffectiveVisibility(Visibility.VISIBLE)))))
    }

    /**
     * Ignore this test currently as it fails on buddybuild but runs perfectly locally
     */
    @Test
    fun testTickerNetworkError() {

        // use UIAutomator to test for snackbar as BuddyBuild cannot match snackbar using espresso
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val snackbarTextView = device.findObject(UiSelector().text(context.getString(R.string.network_error, ExchangeName.BITSTAMP.exchange)))
        snackbarTextView?.text ?: throw RuntimeException("Cannot find snackbar")
        val rowNumber = 3

        // row 3 (bitstamp) should always be - as MockHttpInterceptor returns a HTTP 500
        onView(withRecyclerView(recyclerView).atPosition(rowNumber))
                .check(matches(hasDescendant(allOf(withId(itemExchangeName), withText(ExchangeName.BITSTAMP.exchange)))))

        onView(withRecyclerView(recyclerView).atPosition(rowNumber))
                .check(matches(hasDescendant(allOf(withId(itemPrice), withText("-")))))

        onView(withRecyclerView(recyclerView).atPosition(rowNumber))
                .check(matches(hasDescendant(allOf(withId(item24hourHigh), withText(context.getString(R.string.twenty_four_hour_high, "-"))))))

        onView(withRecyclerView(recyclerView).atPosition(rowNumber))
                .check(matches(hasDescendant(allOf(withId(item24hourLow), withText(context.getString(R.string.twenty_four_hour_low, "-"))))))
    }

    fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }
}