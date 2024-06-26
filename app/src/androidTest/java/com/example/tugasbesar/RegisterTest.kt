package com.example.tugasbesar


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegisterTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(RegisterView::class.java)

    @Test
    fun registerTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(500)

        val circleImageView = onView(
            allOf(
                withId(R.id.profileView),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        circleImageView.perform(click())

        val materialTextView = onView(
            allOf(
                withId(R.id.defaultOption), withText("From NihonGo Image"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.custom),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(click())

        val circleImageView2 = onView(
            allOf(
                withId(R.id.cjView),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.custom),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        circleImageView2.perform(click())

        val materialButton = onView(
            allOf(
                withId(R.id.button_saveImage), withText("SAVE"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.custom),
                        0
                    ),
                    12
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())
            onView(isRoot()).perform(waitFor(3000))


        val materialButton2 = onView(
            allOf(
                withId(R.id.regisButton), withText("Register Now"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())
            onView(isRoot()).perform(waitFor(3000))

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.username),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.userRegis),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("admin"), closeSoftKeyboard())


        val materialButton3 = onView(
            allOf(
                withId(R.id.regisButton), withText("Register Now"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())
            onView(isRoot()).perform(waitFor(3000))

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.password),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.passwordRegis),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText5.perform(replaceText("123"), closeSoftKeyboard())

        val materialButton4 = onView(
            allOf(
                withId(R.id.regisButton), withText("Register Now"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())
            onView(isRoot()).perform(waitFor(3000))

        val textInputEditText6 = onView(
            allOf(
                withId(R.id.confirmpassword),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.confirmRegis),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText6.perform(replaceText("345"), closeSoftKeyboard())

        val materialButton5 = onView(
            allOf(
                withId(R.id.regisButton), withText("Register Now"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())
            onView(isRoot()).perform(waitFor(3000))


        val textInputEditText8 = onView(
            allOf(
                withId(R.id.confirmpassword), withText("345"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.confirmRegis),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText8.perform(replaceText("123"))

        val materialButton6 = onView(
            allOf(
                withId(R.id.regisButton), withText("Register Now"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton6.perform(click())
            onView(isRoot()).perform(waitFor(3000))

        val textInputEditText10 = onView(
            allOf(
                withId(R.id.email),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.emailRegis),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText10.perform(replaceText("davehabelpaprindey@gmail.com"), closeSoftKeyboard())

        val materialButton7 = onView(
            allOf(
                withId(R.id.regisButton), withText("Register Now"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton7.perform(click())
            onView(isRoot()).perform(waitFor(3000))

        val textInputEditText14 = onView(
            allOf(
                withId(R.id.phone),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.noRegis),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText14.perform(replaceText("123"), closeSoftKeyboard())


        val materialButton8 = onView(
            allOf(
                withId(R.id.regisButton), withText("Register Now"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton8.perform(click())
            onView(isRoot()).perform(waitFor(3000))

        val textInputEditText20 = onView(
            allOf(
                withId(R.id.tgl),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tglRegis),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText20.perform(replaceText("17, Dec, 2022"))

        val materialButton10 = onView(
            allOf(
                withId(R.id.regisButton), withText("Register Now"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton10.perform(click())
            onView(isRoot()).perform(waitFor(3000))

        val textInputEditText15 = onView(
            allOf(
                withId(R.id.username), withText("admin"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.userRegis),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText15.perform(replaceText("dave"))

        val materialButton11 = onView(
            allOf(
                withId(R.id.regisButton), withText("Register Now"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton11.perform(click())
            onView(isRoot()).perform(waitFor(3000))
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
        fun waitFor(delay:Long): ViewAction?{
                return object : ViewAction {
                        override fun getConstraints(): Matcher<View> {
                                return isRoot()
                        }

                        override fun getDescription(): String {
                                return "wait for "+delay+" miliseconds"
                        }

                        override fun perform(uiController: UiController, view: View) {
                                uiController.loopMainThreadForAtLeast(delay)
                        }
                }
        }
}
