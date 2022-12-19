package com.example.tugasbesar


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
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
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class addWisataTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(addWisata::class.java)

    @Test
    fun addWisataTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(500)

        val textInputEditTextCity = onView(
            allOf(
                withId(R.id.et_lokasi),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_lokasi),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditTextCity.perform(scrollTo(), replaceText("Tokyo"))

        val materialButton = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val textInputEditText = onView(
            allOf(
                withId(R.id.et_nama),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_nama),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText.perform(scrollTo(), replaceText("warung pakdhe"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val materialAutoCompleteTextView = onView(
            allOf(
                withId(R.id.ed_jenis),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.jenis_wisata),
                        0
                    ),
                    0
                )
            )
        )
        materialAutoCompleteTextView.perform(scrollTo(),replaceText("Tempat Wisata"))

//        val materialTextView = onData(anything())
//            .inAdapterView(
//                childAtPosition(
//                    withClassName(`is`("android.widget.PopupWindow$PopupBackgroundView")),
//                    0
//                )
//            )
//            .atPosition(0)
//        materialTextView.perform(click())

        val materialButton10 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton10.perform(click())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.et_alamat),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_alamat),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText2.perform(scrollTo(), replaceText(""), closeSoftKeyboard())

        val materialButton3 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.et_alamat),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_alamat),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText3.perform(scrollTo(), replaceText("Ohio"), closeSoftKeyboard())

        val materialButton4 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())


        val textInputEditText4 = onView(
            allOf(
                withId(R.id.et_rating),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_rating),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText4.perform(scrollTo(), replaceText("5.0"), closeSoftKeyboard())

        val materialButton5 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())


        val textInputEditText5 = onView(
            allOf(
                withId(R.id.et_harga),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_harga),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText5.perform(scrollTo(), replaceText("23"), closeSoftKeyboard())

//        val textInputEditText6 = onView(
//            allOf(
//                withId(R.id.et_harga), withText("$0.02"),
//                childAtPosition(
//                    childAtPosition(
//                        withId(R.id.layout_harga),
//                        0
//                    ),
//                    0
//                )
//            )
//        )
//        textInputEditText6.perform(scrollTo(), replaceText("23"),closeSoftKeyboard())

        val materialButton6 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton6.perform(click())


        val textInputEditText8 = onView(
            allOf(
                withId(R.id.et_lokasi),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_lokasi),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText8.perform(scrollTo(), click())

        val textInputEditText9 = onView(
            allOf(
                withId(R.id.et_lat),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_lat),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText9.perform(scrollTo(), replaceText("123"), closeSoftKeyboard())


        val materialButton7 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton7.perform(click())

        val textInputEditText10 = onView(
            allOf(
                withId(R.id.et_long),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_long),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText10.perform(scrollTo(), replaceText("123"), closeSoftKeyboard())

        val materialButton8 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton8.perform(click())
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
