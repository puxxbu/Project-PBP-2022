package com.example.tubes_pbp.webapi.pesananapi


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.tubes_pbp.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class FormAddPesananActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(FormAddPesananActivity::class.java)

    @Test
    fun formAddPesananActivityTest() {
        val materialButton = onView(
            allOf(
                withId(R.id.btn_add), withText("Simpan Data"),
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
        materialButton.perform(click())
        onView(isRoot()).perform(waitFor(6000))

        val textInputEditText = onView(
            allOf(
                withId(R.id.txt_nama),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_nama),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("Hotel Nusa"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btn_add), withText("Simpan Data"),
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
        onView(isRoot()).perform(waitFor(4000))

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.txt_wilayah),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_wilayah),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("Nusa Tenggara"), closeSoftKeyboard())

        val materialButton3 = onView(
            allOf(
                withId(R.id.btn_add), withText("Simpan Data"),
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
        onView(isRoot()).perform(waitFor(4000))

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.txt_alamat),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_alamat),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("Jl. Van Baru"), closeSoftKeyboard())

        val materialButton4 = onView(
            allOf(
                withId(R.id.btn_add), withText("Simpan Data"),
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
        onView(isRoot()).perform(waitFor(6000))
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

    fun waitFor(delay: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for " + delay + "milliseconds"
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }
}
