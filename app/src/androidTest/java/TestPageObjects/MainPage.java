package TestPageObjects;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static TestData.DataHelper.waitForUItem;

import androidx.test.espresso.ViewInteraction;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;

public class MainPage {

    private final String newsAdapterTitleText = "News";
    private final int headerLogoutBtnId = R.id.authorization_image_button;
    private final String logoutItemText = "Log out";
    public String getNewsAdapterTitleText() {
        return newsAdapterTitleText;
    }

    @Step("Log out of app through profile button on Main page header")
    public void logOutWtHeaderBtn() {
        waitForUItem(withId(headerLogoutBtnId), 5000);
        onView(withId(headerLogoutBtnId)).perform(click());
        onView(withText(logoutItemText)).perform(click());
    }
}
