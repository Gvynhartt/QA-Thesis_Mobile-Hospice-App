package TestPageObjects;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static TestData.DataHelper.waitForUItem;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.contrib.RecyclerViewActions;

import com.forkingcode.espresso.contrib.DescendantViewActions;

import java.util.HashMap;

import TestData.DataHelper;
import TestUtils.GetTextAction;
import TestUtils.RecyclerCustomMatcher;
import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;

public class NewsPage {

    private final int editNewsBtn = R.id.edit_news_material_button;
    private final String newsPageHeaderText = "News";
    private final int allNewsRecycList = R.id.news_list_recycler_view;
    private final int toggleNewsCardBtn = R.id.view_news_item_image_view;
    private final int newsCardDescr = R.id.news_item_description_text_view;
    private final int newsItemCard = R.id.news_item_material_card_view;
    private final int newsItemCardTitle = R.id.news_item_title_text_view;
    private final int sortNewsHeaderBtn = R.id.sort_news_material_button;
    private final int filterNewsHeaderBtn = R.id.filter_news_material_button;

    public int getAllNewsRecycList() {
        return allNewsRecycList;
    }
    public int getNewsCardDescr() {
        return newsCardDescr;
    }
    public int getNewsItemCard() {
        return newsItemCard;
    }

    public void clickEditNewsBtn() {
        Allure.step("Click EDIT NEWS button of page header & open NEWS CONTROL PANEL page");
        waitForUItem(withId(editNewsBtn), 3000);
        onView(withId(editNewsBtn)).perform(click());
    }

    public void checkNewsPageVisible() {
        Allure.step("Check NEWS page visibility by page header text");
        waitForUItem(withText(newsPageHeaderText), 3000);
    }

    public void checkNewsRecyclerHasAtLeastPos(int totalPos) {
        Allure.step("Check that news recycler list has at least one visible position");
        totalPos -= 1;
        waitForUItem(withId(allNewsRecycList), 1000);
        try {
            onView(withId(allNewsRecycList))
                    .check(matches(RecyclerCustomMatcher
                            .atPosition(totalPos, hasDescendant(withId(newsItemCardTitle)))));
        } catch (NoMatchingViewException noCardsInList) {
            System.out.println(noCardsInList.getMessage());
        }
    }

    public void toggleNewsCardStateWithBtnOnPos(int cardPos) {
        Allure.step("Toggle state of news item card on position with ( ˅ / ˄ ) button");
        waitForUItem(withId(toggleNewsCardBtn), 1000);
        onView(withId(allNewsRecycList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(cardPos, DescendantViewActions
                        .performDescendantAction(withId(toggleNewsCardBtn), click())));
    }

    public void clickFilterBtnToOpenFilterPage() {
        Allure.step("Open FILTER NEWS page with FILTER button in page header");
        waitForUItem(withId(filterNewsHeaderBtn), 1000);
        onView(withId(filterNewsHeaderBtn)).perform(click());
    }

    public void toggleNewsCardOrderWithSortBtn() {
        Allure.step("Toggle news item card order with SORT NEWS LIST button");
        waitForUItem(withId(sortNewsHeaderBtn), 1000);
        onView(withId(sortNewsHeaderBtn)).perform(click());
    }

    public HashMap<Integer, String> createHashMapForCardsInList() {
        Allure.step("Create a hash map of structure <[int] Card Position, [String] Card Title> for later checks");

        DataHelper activity = new DataHelper();
        Activity currActivity = activity.getCurrentActivity();
        RecyclerView newsRecycler = currActivity.findViewById(allNewsRecycList);
        RecyclerView.Adapter newsAdapter = newsRecycler.getAdapter();
        int allNewsCount = newsAdapter.getItemCount();
        HashMap<Integer, String> newsCardsMap = new HashMap<>();
        for (int i = 0; i < allNewsCount; i++) {

            GetTextAction action = new GetTextAction();
            onView(withId(allNewsRecycList)).perform(scrollToPosition(i));
            onView(withId(allNewsRecycList)).perform(RecyclerViewActions.actionOnItemAtPosition(i, DescendantViewActions
                    .performDescendantAction(withId(newsItemCardTitle), action)));
            String cardTitle = action.getText();
            newsCardsMap.put(i, cardTitle);
        }
        return newsCardsMap;
    }

    public boolean checkIfCardListOrderIsReversed(HashMap<Integer, String> mapToCheck) {
        Allure.step("Check that the order of items in map provided is reversed");
        boolean isReversed = false;
        try {
            HashMap<Integer, String> refMap = createHashMapForCardsInList();
            int mapSize = mapToCheck.size();
            int matchCount = 0;
            for (int i = 0; i < mapSize; i++) {
                String refTitle = refMap.get(mapSize - i - 1);
                String checkTitle = mapToCheck.get(i);
                if (refTitle.equals(checkTitle)) {
                    matchCount += 1;
                }
            }

            if (matchCount == mapSize) {
                isReversed = true;
            }
        } catch (Exception mapsNonComparable) {
            System.out.println(mapsNonComparable.getMessage());
        }
        return isReversed;
    }
}
