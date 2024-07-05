package TestData;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

import android.view.View;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;

import org.hamcrest.Matcher;

import java.util.concurrent.TimeoutException;

public class DataHelper {

    public static class TestUser {

        private final String testLogin;
        private final String testPassword;

        public TestUser(String testLogin, String testPassword) {
            this.testLogin = testLogin;
            this.testPassword = testPassword;
        }

        public String getTestLogin() {
            return testLogin;
        }

        public String getTestPassword() {
            return testPassword;
        }
    }

    public TestUser getHardcodeUser() {
        return new TestUser("login2", "password2");
    }

    public TestUser getErrorUser() {
        return new TestUser("aboba", "3450d");
    }

    public static ViewAction waitTillElementDisplay(final Matcher elViewMatcher, final long waitTime) {

        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a certain matching element for " + waitTime + "milliseconds";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + waitTime;
                final Matcher<View> viewMatcher = elViewMatcher;
                final Matcher<View> matchDisplayed = isDisplayed();

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        try {
                            if (viewMatcher.matches(child) && matchDisplayed.matches(child)) {
                                return;
                            }
                        } catch (NoMatchingViewException viewNotFound) { }
                        uiController.loopMainThreadForAtLeast(100);
                    }
                }
                while (System.currentTimeMillis() < endTime);

                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }

    public static void waitForUItem(Matcher itemMatcher, long waitMlsec) {
        onView(isRoot()).perform(waitTillElementDisplay(itemMatcher, waitMlsec));
    }
}
