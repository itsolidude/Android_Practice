package algonquin.cst2335.kadv0001;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Tests that a password without an uppercase letter is correctly identified.
     */
    @Test
    public void testFindMissingUpperCase() {
        // Find the EditText and type in the password without an uppercase letter
        ViewInteraction appCompatEditText = onView(withId(R.id.textInputPassword));
        appCompatEditText.perform(replaceText("password123#$*"));

        // Find the button and click it
        ViewInteraction materialButton = onView(withId(R.id.LoginButton));
        materialButton.perform(click());

        // Find the TextView/check that the text matches "You shall not pass!"
        ViewInteraction textView = onView(withId(R.id.ViewPasswordPrompt));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Tests that a password without a lowercase letter is correctly identified.
     */
    @Test
    public void testFindMissingLowerCase() {
        onView(withId(R.id.textInputPassword)).perform(replaceText("PASSWORD123#$*"));
        onView(withId(R.id.LoginButton)).perform(click());
        onView(withId(R.id.ViewPasswordPrompt)).check(matches(withText("You shall not pass!")));
    }

    /**
     * Tests that a password without a digit is correctly identified.
     */
    @Test
    public void testFindMissingDigit() {
        onView(withId(R.id.textInputPassword)).perform(replaceText("Password!@#"));
        onView(withId(R.id.LoginButton)).perform(click());
        onView(withId(R.id.ViewPasswordPrompt)).check(matches(withText("You shall not pass!")));
    }

    /**
     * Tests that a password without a special character is correctly identified.
     */
    @Test
    public void testFindMissingSpecialChar() {
        onView(withId(R.id.textInputPassword)).perform(replaceText("Password123"));
        onView(withId(R.id.LoginButton)).perform(click());
        onView(withId(R.id.ViewPasswordPrompt)).check(matches(withText("You shall not pass!")));
    }

    /**
     * Tests that a valid password is accepted.
     */
    @Test
    public void testValidPassword() {
        onView(withId(R.id.textInputPassword)).perform(replaceText("Password123#$*"));
        onView(withId(R.id.LoginButton)).perform(click());
        onView(withId(R.id.ViewPasswordPrompt)).check(matches(withText("Your password meets the requirements")));
    }

    @Test
    public void mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5146);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.textInputPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.textInputPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("12345"), closeSoftKeyboard());

        //pressBack();

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.LoginButton), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.ViewPasswordPrompt), withText("You shall not pass!"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("You shall not pass!")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
