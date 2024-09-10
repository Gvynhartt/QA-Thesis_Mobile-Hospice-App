package TestPageObjects;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static TestData.DataHelper.waitForUItem;

import android.view.View;

import org.hamcrest.Matchers;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;

public class CustomAppBar {
    private View customAppBarDecorView;
    private final int mainMenuBtn = R.id.main_menu_image_button;
    private final int appTrademark = R.id.trademark_image_view;
    private final int appLogoBtn = R.id.our_mission_image_button;
    private final int aboutBackBtn = R.id.about_back_image_button;
    private final int headerLogoutBtnId = R.id.authorization_image_button;
    private final String logoutItemText = "Log out";
    private final String menuListItemMainText = "Main";
    private final String menuListItemNewsText = "News";
    private final String menuListItemAboutText = "About";

    public int getAppTrademark() { return appTrademark; }

    public void checkHeaderVisibleWithBarTitle() {

        Allure.step("Check app bar visibility with trademark drawable");
        waitForUItem(withId(appTrademark), 3000);
        onView(withId(appTrademark)).check(matches(isDisplayed()));
    }
    public void goToMainPageWithMenuBtn() {
        Allure.step("Navigate to MAIN page with MAIN MENU list");
        waitForUItem(withId(mainMenuBtn), 3000);
        onView(withId(mainMenuBtn)).perform(click());
        onView(withText(menuListItemMainText))
                .inRoot(withDecorView(Matchers.not(customAppBarDecorView)))
                .perform(click());
    }

    public void goToNewsPageWithMenuBtn() {
        Allure.step("Navigate to MAIN page with MAIN MENU list");
        waitForUItem(withId(mainMenuBtn), 1000);
        onView(withId(mainMenuBtn)).perform(click());
        onView(withText(menuListItemNewsText))
                .inRoot(withDecorView(Matchers.not(customAppBarDecorView)))
                .perform(click());
    }

    public void goToQuotesPageWithLogoBtn() {
        Allure.step("Navigate to OUR MISSION page with OUR MISSION app bar button");
        waitForUItem(withId(appLogoBtn), 1000);
        onView(withId(appLogoBtn)).perform(click());
    }

    public void goToAboutPageWithMenuBtn() {
        Allure.step("Navigate to ABOUT page with MAIN MENU list");
        waitForUItem(withId(mainMenuBtn), 1000);
        onView(withId(mainMenuBtn)).perform(click());
        onView(withText(menuListItemAboutText))
                .inRoot(withDecorView(Matchers.not(customAppBarDecorView)))
                .perform(click());
    }

    public void leaveAboutPageWithBackBtn() {
        Allure.step("Go back to MAIN page with BACK header button");
        waitForUItem(withId(aboutBackBtn), 1000);
        onView(withId(aboutBackBtn)).perform(click());
    }

    public void logOutWtHeaderBtn() {
        Allure.step("Log out of app through profile button on Main page header");
        waitForUItem(withId(headerLogoutBtnId), 1500);
        onView(withId(headerLogoutBtnId)).perform(click());
        onView(withText(logoutItemText)).perform(click());
    }
}
