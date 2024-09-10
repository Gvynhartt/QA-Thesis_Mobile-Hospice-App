package TestPageObjects;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static TestData.DataHelper.waitForUItem;

import androidx.test.espresso.contrib.RecyclerViewActions;

import com.forkingcode.espresso.contrib.DescendantViewActions;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;

public class MainPage {

    private final String newsAdapterTitleText = "News";
    private final int headerLogoutBtnId = R.id.authorization_image_button;
    private final int allNewsBtn = R.id.all_news_text_view;
    private final int expandNewsListBtn = R.id.expand_material_button;
    private final int allNewsCardsBlock = R.id.news_list_recycler_view;
    private final int toggleNewsCardBtn = R.id.view_news_item_image_view;
    private final int newsCardDescr = R.id.news_item_description_text_view;

    public int getNewsCardView() {
        return newsCardView;
    }

    private final int newsCardView = R.id.news_item_material_card_view;

    public int getAllNewsCardsBlock() {
        return allNewsCardsBlock;
    }

    public String getNewsAdapterTitleText() {
        return newsAdapterTitleText;
    }
    public int getAllNewsBtn() { return allNewsBtn; }
    public int getNewsCardDescr() { return newsCardDescr; }

    public void checkMainPageVisible() {
        Allure.step("Check page visibility with news adapter title text");
        waitForUItem(withText(newsAdapterTitleText), 10000);
        onView(withText(newsAdapterTitleText)).check(matches(isDisplayed()));
    }

    public void checkLogoutBtnVisible() {
        Allure.step("Check LOGOUT button visibility for NEWS page header");
        waitForUItem(withId(headerLogoutBtnId), 5000);
    }

    public void toggleNewsListAdapter() {
        Allure.step("Toggle NEWS adapter with ( ˅ / ˄ ) button");
        waitForUItem(withId(expandNewsListBtn), 5000);
        onView(withId(expandNewsListBtn)).perform(click());
    }

    public void goToNewsPageWithAllNewsBtn() {
        Allure.step("Navigate to NEWS page with ALL NEWS button");
        waitForUItem(withId(allNewsBtn), 1500);
        onView(withId(allNewsBtn)).perform(click());
    }

    public void toggleNewsItemCard(int posToCLick) {
        Allure.step("Toggle news item card with ( ˅ / ˄ ) button");
        waitForUItem(withId(toggleNewsCardBtn), 1000);
        onView(withId(allNewsCardsBlock)).perform(RecyclerViewActions
                .actionOnItemAtPosition(posToCLick, DescendantViewActions
                        .performDescendantAction(withId(toggleNewsCardBtn), click())));
    }

}
