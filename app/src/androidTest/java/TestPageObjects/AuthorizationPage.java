package TestPageObjects;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static TestData.DataHelper.waitForUItem;

import android.view.KeyEvent;
import android.view.View;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;

import androidx.test.espresso.NoActivityResumedException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

public class AuthorizationPage {

    private final String loginHint = "Login";
    private final String passwdHint = "Password";
    private final int signInButton = R.id.enter_button;
    private final String loginErrorToastText = "Something went wrong. Try again later.";
    private final String emptyLoginToastText = "Login and password cannot be empty";
    private final String pageHeaderText = "Authorization";

    public String getLoginErrorToastText() {
        return loginErrorToastText;
    }
    public String getEmptyLoginToastText() {
        return emptyLoginToastText;
    }


    public void checkAuthPageVisible() {
        Allure.step("Check auth page visibility by header text");
        waitForUItem(withText(pageHeaderText), 10000);
        onView(withText(pageHeaderText)).check(matches(isDisplayed()));
    }

    public void insertLogin(String login) {
        Allure.step("Insert login with soft keyboard");
        waitForUItem(withHint(loginHint), 5000);
        onView(withHint(loginHint)).perform(click()).perform(typeText(login));
        closeSoftKeyboard();
    }

    public void insertPassword(String password) {
        Allure.step("Insert password with soft keyboard");
        waitForUItem(withHint(passwdHint), 5000);
        onView(withHint(passwdHint)).perform(click()).perform(typeText(password));
        closeSoftKeyboard();
    }

    public void pressSignIn() {
        Allure.step("Press SIGN IN button");
        waitForUItem(withId(signInButton), 5000);
        onView(withId(signInButton)).perform(click());
    }

    public void pressBackSysBtn() throws NoActivityResumedException{
        Allure.step("Press BACK system button");
        onView(withText(pageHeaderText)).perform(pressKey(KeyEvent.KEYCODE_BACK));
        throw new NoActivityResumedException("App closed on BACK button press");
    }
}
