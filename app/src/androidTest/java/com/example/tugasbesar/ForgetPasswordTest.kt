package com.example.tugasbesar


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ForgetPasswordTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(ForgetPassword::class.java)

    @Test
    fun forgetPasswordTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(500)

        val materialButton = onView(
            allOf(
                withId(R.id.button_save), withText("SAVE"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val textInputEditText = onView(
            allOf(
                withId(R.id.username),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.userForget),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(click())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.username),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.userForget),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("dave1"), closeSoftKeyboard())

        pressBack()

        val materialButton2 = onView(
            allOf(
                withId(R.id.button_save), withText("SAVE"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.password),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.passwordForget),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("dave"), closeSoftKeyboard())

        pressBack()

        val materialButton3 = onView(
            allOf(
                withId(R.id.button_save), withText("SAVE"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.confirmpassword),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.confirmForget),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("abatukam"), closeSoftKeyboard())

        pressBack()

        val materialButton4 = onView(
            allOf(
                withId(R.id.button_save), withText("SAVE"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.confirmpassword), withText("abatukam"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.confirmForget),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText5.perform(click())

        val textInputEditText6 = onView(
            allOf(
                withId(R.id.confirmpassword), withText("abatukam"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.confirmForget),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText6.perform(replaceText("dave"))

        val textInputEditText7 = onView(
            allOf(
                withId(R.id.confirmpassword), withText("dave"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.confirmForget),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText7.perform(closeSoftKeyboard())

        pressBack()

        val materialButton5 = onView(
            allOf(
                withId(R.id.button_save), withText("SAVE"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())

        val textInputEditText8 = onView(
            allOf(
                withId(R.id.username), withText("dave1"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.userForget),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText8.perform(replaceText("dave"))

        val textInputEditText9 = onView(
            allOf(
                withId(R.id.username), withText("dave"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.userForget),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText9.perform(closeSoftKeyboard())

        pressBack()

        val materialButton6 = onView(
            allOf(
                withId(R.id.button_save), withText("SAVE"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton6.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
