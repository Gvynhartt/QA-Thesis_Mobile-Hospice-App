package TestPageObjects;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static TestData.DataHelper.waitForUItem;

import android.view.View;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

public class AuthorizationPage {

    private final String loginHint = "Login";
    private final String passwdHint = "Password";
    private final String headerText = "Authorization";
    private final int signInButton = R.id.enter_button;
    public ActivityScenarioRule<AppActivity> activityScenarioRule = new ActivityScenarioRule<>(AppActivity.class);
    private View decorView;
    private final String loginErrorToastText = "Something went wrong. Try again later.";
    private final String emptyLoginToastText = "Login and password cannot be empty";
    private final String pageHeaderText = "Authorization";

    @Step("Check auth page visibility by header text")
    public void checkAuthPageVisible() {

        waitForUItem(withText(pageHeaderText), 10000);
    }

    @Step("Insert login with soft keyboard")
    public void insertLogin(String login) {
        waitForUItem(withHint(loginHint), 5000);
        onView(withHint(loginHint)).perform(click()).perform(typeText(login));
        closeSoftKeyboard();
    }

    @Step("Insert password with soft keyboard")
    public void insertPassword(String password) {

        waitForUItem(withHint(passwdHint), 5000);
        onView(withHint(passwdHint)).perform(click()).perform(typeText(password));
        closeSoftKeyboard();
    }

    @Step("Press SIGN IN button")
    public void pressSignIn() {

        waitForUItem(withId(signInButton), 5000);
        onView(withId(signInButton)).perform(click());
    }

    public String getLoginErrorToastText() {
        return loginErrorToastText;
    }

    public String getEmptyLoginToastText() {
        return emptyLoginToastText;
    }

}
