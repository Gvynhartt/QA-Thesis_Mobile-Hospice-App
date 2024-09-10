package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import TestData.DataHelper;
import TestPageObjects.AuthorizationPage;
import TestPageObjects.CreatingNewsPage;
import TestPageObjects.CustomAppBar;
import TestPageObjects.EditingNewsPage;
import TestPageObjects.MainPage;
import TestPageObjects.NewsControlPanelPage;
import TestPageObjects.NewsFilterPage;
import TestPageObjects.NewsPage;
import TestPageObjects.OurMissionPage;
import TestUtils.RecyclerCustomMatcher;
import io.qameta.allure.android.rules.ScreenshotRule;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.junit4.DisplayName;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class Test_OurMissionPage {

    @Rule
    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule(ScreenshotRule.Mode.FAILURE,
            String.valueOf(System.currentTimeMillis()));

    MainPage mainPage = new MainPage();
    OurMissionPage ourMissionPage = new OurMissionPage();
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
            customAppBar.goToQuotesPageWithLogoBtn();
            ourMissionPage.checkPageVisibleByHeader(1000);
        } catch (Exception notOnQuotesPage) {
            System.out.println(notOnQuotesPage.getMessage());
            customAppBar.goToQuotesPageWithLogoBtn();
        }
    }

    @Test
    @DisplayName("Check OUR MISSION page visibility by header title")
    public void shdDisplayQuotesPage() {

        ourMissionPage.checkPageVisibleByHeader(1000);
    }

    @Test
    @DisplayName("Check if quote recycler is visible on page")
    public void shdDisplayQuotesRecycList() {

        onView(withId(ourMissionPage.getQuoteListRecycler())).check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Check if quote recycler list has the necessary number of items")
    public void shdDiaplayNmbOfItemsInQuoteList() {

        ourMissionPage.checkQuoteRecyclerHasAtLeastPos(ourMissionPage.getTotalQuotesInList());
    }

    @Test
    @DisplayName("Check if quote recycler has all titles & descriptions")
    public void shdDiaplyAllQuoteContent() {

        ourMissionPage.checkAllQuotesAndDescrVisibleOnPage();
    }

    @Test
    @DisplayName("Check if quote expands from default state when ( ˅ / ˄ ) button is clicked")
    public void shdExpandQuoteCardWithArrow() {
        int cardPos = 0;
        ourMissionPage.toggleQuoteAtPosWithBtn(cardPos);
        onView(withId(ourMissionPage.getQuoteListRecycler()))
                .check(matches(RecyclerCustomMatcher
                        .atPosition(cardPos, hasDescendant(allOf(withId(ourMissionPage.getQuoteDescr()), isDisplayed())))));
    }

    @Test
    @DisplayName("Check if quote minimized from expanded state when ( ˅ / ˄ ) button is clicked again")
    public void shdMinimizeQuoteCardWithArrow() {
        int cardPos = 7;
        ourMissionPage.toggleQuoteAtPosWithBtn(cardPos);
        ourMissionPage.toggleQuoteAtPosWithBtn(cardPos);
        onView(withId(ourMissionPage.getQuoteListRecycler()))
                .check(matches(RecyclerCustomMatcher
                        .atPosition(cardPos,
                                hasDescendant(allOf(withId(ourMissionPage.getQuoteDescr()), not(isDisplayed()))))));
    }
}
