package co.uk.kenkwok.tulipmania.testutils

import android.graphics.drawable.ColorDrawable
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.test.internal.util.Checks
import android.view.View
import android.widget.LinearLayout
import org.hamcrest.Description
import org.hamcrest.Matcher


/**
 * Created by kekwok on 22/01/2018.
 */
class CustomMatcher {
    companion object {
        fun withBackgroundColour(color: Int): Matcher<View> {
            Checks.checkNotNull(color)
            return object : BoundedMatcher<View, LinearLayout>(LinearLayout::class.java) {
                public override fun matchesSafely(row: LinearLayout): Boolean {
                    return color == (row.background as ColorDrawable).color
                }

                override fun describeTo(description: Description) {
                    description.appendText("with background colour: ")
                }
            }
        }
    }
}