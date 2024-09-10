package ru.iteco.fmhandroid.ui;

import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import TestData.DataHelper;
import TestPageObjects.AboutPage;
import TestPageObjects.AuthorizationPage;
import TestPageObjects.CustomAppBar;
import TestPageObjects.MainPage;
import io.qameta.allure.android.rules.ScreenshotRule;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.junit4.DisplayName;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class Test_AboutPage {

    @Rule
    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);
    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule(ScreenshotRule.Mode.FAILURE,
            String.valueOf(System.currentTimeMillis()));
    MainPage mainPage = new MainPage();
    AboutPage aboutPage = new AboutPage();
    AuthorizationPage authPage = new AuthorizationPage();
    DataHelper testUser = new DataHelper();
    CustomAppBar customAppBar = new CustomAppBar();

    @Before
    public void setUp() {
        try {
            mainPage.checkLogoutBtnVisible();
        } catch (Exception notLoggedIn) {
            System.out.println(notLoggedIn.getMessage());
            authPage.checkAuthPageVisible();
            authPage.insertLogin(testUser.getHardcodeUser().getTestLogin());
            authPage.insertPassword(testUser.getHardcodeUser().getTestPassword());
            authPage.pressSignIn();
        }

        try {
            customAppBar.checkHeaderVisibleWithBarTitle();
            customAppBar.goToAboutPageWithMenuBtn();
            aboutPage.checkPageVisibleByHeader();
        } catch (Exception notOnAboutPage) {
            System.out.println(notOnAboutPage.getMessage());
            customAppBar.goToAboutPageWithMenuBtn();
        }
    }

    @Test
    @DisplayName("Check ABOUT page visibility")
    public void shdDisplayAboutPage() {

        aboutPage.checkPageVisibleByHeader();
    }

    @Test
    @DisplayName("Check page text content visibility")
    public void shdDisplayAboutPageTextContent() {

        aboutPage.checkAboutPageContentShown();
    }

    @Test
    @DisplayName("Check if PRIVACY POLICY link is passed as an intent to a browser")
    public void shdPassPrivacyLinkIntentToBrowser() {
        Intents.init();
        aboutPage.clickPrivacyPolicyLink();
        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_VIEW));
        Intents.intended(IntentMatchers.hasData(aboutPage.getPrivacyLinkText()));
        Intents.release();
    }

    @Test
    @DisplayName("Check if TERMS OF USE link is passed as an intent to a browser")
    public void shdPassTermsLinkIntentToBrowser() {
        Intents.init();
        aboutPage.clickTermsOfUseLink();
        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_VIEW));
        Intents.intended(IntentMatchers.hasData(aboutPage.getTermsLinkText()));
        Intents.release();
    }

    @Test
    @DisplayName("Check that BACK header button transitions back to main page")
    public void shdLeaveAboutPageWithBackBtn() {
        customAppBar.leaveAboutPageWithBackBtn();
        mainPage.checkMainPageVisible();
    }
}
