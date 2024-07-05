package ru.iteco.fmhandroid.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static TestData.DataHelper.waitForUItem;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import TestData.DataHelper;
import TestPageObjects.AuthorizationPage;
import TestPageObjects.MainPage;
import io.qameta.allure.android.rules.ScreenshotRule;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.junit4.DisplayName;


@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class Test_AuthPage {

    @Rule
    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule(ScreenshotRule.Mode.FAILURE,
            String.valueOf(System.currentTimeMillis()));
    private View authDecorView;
    AuthorizationPage authPage = new AuthorizationPage();
    MainPage mainPage = new MainPage();

    @Before
    public void beforeSession() {
        try {
            authPage.checkAuthPageVisible();
        } catch (NoMatchingViewException notFound) {
            System.out.println(notFound.getMessage());
            mainPage.logOutWtHeaderBtn();
            authPage.checkAuthPageVisible();
        }
        mActivityScenarioRule.getScenario().onActivity(new ActivityScenario.ActivityAction<AppActivity>() {
            @Override
            public void perform(AppActivity activity) {
                authDecorView = activity.getWindow().getDecorView();
            }
        });
    }

    @After
    public void afterSession() {
        try {
            mainPage.logOutWtHeaderBtn();
        } catch (Exception generalException) {
            System.out.println(generalException.getMessage());
        }
    }

    @Test
    @DisplayName("Checks successive login with valid data")
    public void shouldLoginWithValidCreds() {

        DataHelper testUser = new DataHelper();
        AuthorizationPage authPage = new AuthorizationPage();
        MainPage mainPage = new MainPage();
        authPage.insertLogin(testUser.getHardcodeUser().getTestLogin());
        authPage.insertPassword(testUser.getHardcodeUser().getTestPassword());
        authPage.pressSignIn();
        waitForUItem(withText(mainPage.getNewsAdapterTitleText()), 5000);
    }

    @Test
    @DisplayName("Checks toast message appearance if invalid auth data is input")
    public void shouldDisplayToastWhenInvalidCreds() {

        DataHelper testUser = new DataHelper();
        AuthorizationPage authPage = new AuthorizationPage();
        authPage.insertLogin(testUser.getErrorUser().getTestLogin());
        authPage.insertPassword(testUser.getErrorUser().getTestPassword());
        authPage.pressSignIn();
        String credsErrorText = authPage.getLoginErrorToastText();
        onView(withText(credsErrorText)).inRoot(withDecorView(Matchers.not(authDecorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Checks (another) toast message for instance of no input in text fields")
    public void shdShowToastIfNoTextInput() {

        DataHelper testUser = new DataHelper();
        AuthorizationPage authPage = new AuthorizationPage();
        String emptyInput = "";
        authPage.insertLogin(emptyInput);
        authPage.insertPassword(emptyInput);
        authPage.pressSignIn();
        String noInputErrorText = authPage.getEmptyLoginToastText();
        onView(withText(noInputErrorText)).inRoot(withDecorView(Matchers.not(authDecorView)))
                .check(matches(isDisplayed()));
    }
}
