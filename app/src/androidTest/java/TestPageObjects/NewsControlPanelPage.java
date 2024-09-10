package TestPageObjects;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static TestData.DataHelper.waitForUItem;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import com.forkingcode.espresso.contrib.DescendantViewActions;

import org.hamcrest.Matchers;
import org.junit.Rule;

import java.util.HashMap;

import TestData.DataHelper;
import TestUtils.GetTextAction;
import TestUtils.RecyclerCustomMatcher;
import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;

public class NewsControlPanelPage {
    private View newsControlPanelDecorView;
    private final int addNewsBtn = R.id.add_news_image_view;
    private final String newsCtrlPanelText = "Control panel";
    private final int allNewsCardsBlockList = R.id.news_list_recycler_view;
    private final int editNewsCardBtn = R.id.edit_news_item_image_view;
    private final int delNewsCardBtn = R.id.delete_news_item_image_view;
    private final int newsCardTitle = R.id.news_item_title_text_view;
    private final int newsCardToggleBtn = R.id.view_news_item_image_view;
    private final int sortNewsHeaderBtn = R.id.sort_news_material_button;
    private final int filterNewsHeaderBtn = R.id.filter_news_material_button;
    private final int newsItemCard = R.id.news_item_material_card_view;
    private final int newsItemCardTitle = R.id.news_item_title_text_view;
    private final int newsCardDescr = R.id.news_item_description_text_view;
    private final int newsItemCreationDate = R.id.news_item_create_date_text_view;
    private final int refreshPageBtn = R.id.control_panel_news_retry_material_button;
    private final String newsItemActiveText = "Active";
    private final String newsItemInactiveText = "Not active";
    private final String confDeletePopupText = "Are you sure you want to permanently delete the document? These changes cannot be reversed in the future";
    private final String okBtnText = "OK";
    private final String cancelBtnText = "CANCEL";
    public int getAllNewsCardsBlockList() {
        return allNewsCardsBlockList;
    }
    public int getNewsItemCard() { return newsItemCard;}
    public int getNewsCardDescr() { return newsCardDescr; }
    public String getConfDeletePopupText() { return confDeletePopupText; }
    public int getNewsItemCreationDate() { return newsItemCreationDate; }
    public String getNewsItemActiveText() {
        return newsItemActiveText;
    }
    public String getNewsItemInactiveText() {
        return newsItemInactiveText;
    }

    public void clickAddNewsBtn() {
        Allure.step("Click ADD NEWS button of Control Panel & open CREATING NEWS page");
        waitForUItem(withId(addNewsBtn), 1000);
        onView(withId(addNewsBtn)).perform(click());
    }

    public void checkNewsControlPageVisible(int timeToWait) {
        Allure.step("Check NEWS CONTROL PANEL page visibility by page header text");
        waitForUItem(withText(newsCtrlPanelText), timeToWait);
    }

    public void checkAllNewsRecycListVisible() {
        Allure.step("Check news recycler list visibility on page");
        waitForUItem(withId(allNewsCardsBlockList), 1000);
        onView(withId(allNewsCardsBlockList)).check(matches(isDisplayed()));
    }

    public void clickEditNewsBtnForCardAtPos(int cardPosition) {
        Allure.step("Click EDIT NEWS button of news item card & open EDITING NEWS page");
        waitForUItem(withId(editNewsCardBtn), 1000);
        onView(withId(allNewsCardsBlockList)).perform(RecyclerViewActions
                .actionOnItemAtPosition(cardPosition, DescendantViewActions
                        .performDescendantAction(withId(editNewsCardBtn), click())));
    }

    public void clickEditOnNewsItemWithMatchingTitle(String titleToMatch) {
        Allure.step("Clicks EDIT NEWS button for news item with matching title in recycler list");

        DataHelper activity = new DataHelper();
        Activity currActivity = activity.getCurrentActivity();
        RecyclerView newsRecycler = currActivity.findViewById(allNewsCardsBlockList);
        RecyclerView.Adapter newsAdapter = newsRecycler.getAdapter();

        int allNewsCount = newsAdapter.getItemCount();
        int matchPos = -1; // если присвоить 0, то может произойти ложное срабатывание метода
        for (int i = 0; i < allNewsCount; i++) {
            GetTextAction action = new GetTextAction();
            onView(withId(allNewsCardsBlockList)).perform(scrollToPosition(i));
            onView(withId(allNewsCardsBlockList)).perform(RecyclerViewActions.actionOnItemAtPosition(i, DescendantViewActions
                    .performDescendantAction(withId(newsCardTitle), action)));
            String cardTitle = action.getText();
            // Я в курсе, что такие манипуляции очень сомнительная практика в тестировании, но более
            // изящного метода с одним самописным matcher'ом, который находит нужный заголовок у карточки
            // из списка, а потом в ней же кликает кнопку редактирования, я выдавить из себя не смог.
            if (cardTitle.equals(titleToMatch)) {
                matchPos = i;
            }
        }
        try {
            onView(withId(allNewsCardsBlockList)).perform(scrollToPosition(matchPos));
            onView(withId(allNewsCardsBlockList)).perform(RecyclerViewActions
                    .actionOnItemAtPosition(matchPos, DescendantViewActions
                            .performDescendantAction(withId(editNewsCardBtn), click())));
        } catch (PerformException noSuchTitle) {
            // поскольку будет произведена попытка обратиться к индексу -1, что даст именно Perform Exc-on
            System.out.println(noSuchTitle.getMessage());
        }
    }

    public void clickDeleteOnNewsItemWithMatchingTitle(String titleToMatch) {
        Allure.step("Clicks DELETE NEWS ITEM button for news item with matching title in recycler list");

        DataHelper activity = new DataHelper();
        Activity currActivity = activity.getCurrentActivity();
        RecyclerView newsRecycler = currActivity.findViewById(allNewsCardsBlockList);
        RecyclerView.Adapter newsAdapter = newsRecycler.getAdapter();

        int allNewsCount = newsAdapter.getItemCount();
        int matchPos = -1; // если присвоить 0, то может произойти ложное срабатывание метода
        for (int i = 0; i < allNewsCount; i++) {
            GetTextAction action = new GetTextAction();
            onView(withId(allNewsCardsBlockList)).perform(scrollToPosition(i));
            onView(withId(allNewsCardsBlockList))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, DescendantViewActions
                    .performDescendantAction(withId(newsCardTitle), action)));
            String cardTitle = action.getText();

            if (cardTitle.equals(titleToMatch)) {
                matchPos = i;
            }
        }
        try {
            onView(withId(allNewsCardsBlockList)).perform(scrollToPosition(matchPos));
            onView(withId(allNewsCardsBlockList)).perform(RecyclerViewActions
                    .actionOnItemAtPosition(matchPos, DescendantViewActions
                            .performDescendantAction(withId(delNewsCardBtn), click())));
        } catch (PerformException noSuchTitle) {
            System.out.println(noSuchTitle.getMessage());
        }
    }

    public int checkIfNewsListHasTitle(boolean isPositiveCheck, String titleToCheck) {
        Allure.step("Check if recycler view has card with specified title");
        // Суть метода в том, что в зависимости от логического параметра он позволяет проводить
        // проверки как на наличие элемента, так и на его отсутствие для списков любой длины.
        // True соответствует положительной проверке, false - отрицательной.
        DataHelper activity = new DataHelper();
        Activity currActivity = activity.getCurrentActivity();
        RecyclerView newsRecycler = currActivity.findViewById(allNewsCardsBlockList);
        RecyclerView.Adapter newsAdapter = newsRecycler.getAdapter();
        int matchPos = -1;
        int allNewsCount = newsAdapter.getItemCount();
        for (int i = 0; i < allNewsCount; i++) {
            GetTextAction action = new GetTextAction();
            onView(withId(allNewsCardsBlockList))
                    .perform(scrollToPosition(i));
            onView(withId(allNewsCardsBlockList))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, DescendantViewActions
                    .performDescendantAction(withId(newsCardTitle), action)));
            String cardTitle = action.getText();
            if (cardTitle.equals(titleToCheck)) {
                matchPos = i;
                break;
            }
        }

        if (isPositiveCheck && ( matchPos >= 0 )) {
            onView(withId(allNewsCardsBlockList))
                    .perform(RecyclerViewActions.scrollToPosition(matchPos));
            onView(withId(allNewsCardsBlockList))
                    .check(matches(RecyclerCustomMatcher.atPosition(matchPos, hasDescendant(withText(titleToCheck)))));
        } else {
            // т.е. когда проверка отрицательная и совпадений не найдено
            if (allNewsCount > 0) {
                // список новостей не пустой
                try {
                    matchPos = 0;
                    onView(withId(allNewsCardsBlockList))
                            .perform(RecyclerViewActions.scrollToPosition(matchPos));
                    onView(withId(allNewsCardsBlockList))
                            .check(matches(RecyclerCustomMatcher.atPosition(matchPos, not(hasDescendant(withText(titleToCheck))))));
                } catch (NoMatchingViewException titleMismatch) {
                    System.out.println(titleMismatch.getMessage());
                }
            } else {
                // в списке новостей ничего не отображается
                onView(withId(allNewsCardsBlockList))
                        .check(matches(hasSibling(allOf(withId(refreshPageBtn), isDisplayed()))));
                // эта проверка нужна, чтобы тесты на отсутствие элемента не падали от пустой страницы
            }

        }
        return matchPos;
    }

    public int checkIfNewsListHasDescr(boolean isPositiveCheck, String descrToCheck) {
        Allure.step("Check if recycler view has card with specified title");
        // Суть метода в том, что в зависимости от логического параметра он позволяет проводить
        // проверки как на наличие элемента, так и на его отсутствие для списков любой длины.
        // True соответствует положительной проверке, false - отрицательной.
        DataHelper activity = new DataHelper();
        Activity currActivity = activity.getCurrentActivity();
        RecyclerView newsRecycler = currActivity.findViewById(allNewsCardsBlockList);
        RecyclerView.Adapter newsAdapter = newsRecycler.getAdapter();
        int matchPos = -1;
        int allNewsCount = newsAdapter.getItemCount();
        for (int i = 0; i < allNewsCount; i++) {
            GetTextAction action = new GetTextAction();
            onView(withId(allNewsCardsBlockList))
                    .perform(scrollToPosition(i))
                    .perform(click());
            onView(withId(allNewsCardsBlockList))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, DescendantViewActions
                            .performDescendantAction(withId(newsCardDescr), action)));
            String cardTitle = action.getText();
            if (cardTitle.equals(descrToCheck)) {
                matchPos = i;
                break;
            }
        }

        if (isPositiveCheck && ( matchPos >= 0 )) {
            onView(withId(allNewsCardsBlockList))
                    .perform(RecyclerViewActions.scrollToPosition(matchPos))
                    .perform(click());
            onView(withId(allNewsCardsBlockList))
                    .check(matches(RecyclerCustomMatcher.atPosition(matchPos, hasDescendant(withText(descrToCheck)))));
        } else {
            // т.е. когда проверка отрицательная и совпадений не найдено
            if (allNewsCount > 0) {
                // список новостей не пустой
                try {
                    matchPos = 0;
                    onView(withId(allNewsCardsBlockList))
                            .perform(RecyclerViewActions.scrollToPosition(matchPos))
                            .perform(click());
                    onView(withId(allNewsCardsBlockList))
                            .check(matches(RecyclerCustomMatcher.atPosition(matchPos, not(hasDescendant(withText(descrToCheck))))));
                } catch (NoMatchingViewException titleMismatch) {
                    System.out.println(titleMismatch.getMessage());
                }
            } else {
                // в списке новостей ничего не отображается
                onView(withId(allNewsCardsBlockList))
                        .check(matches(hasSibling(allOf(withId(refreshPageBtn), isDisplayed()))));
                // эта проверка нужна, чтобы тесты на отсутствие элемента не падали от пустой страницы
            }

        }
        return matchPos;
    }

    public void checkNewsRecyclerHasAtLeastPos(int totalPos) {
        Allure.step("Check that news recycler list has at least one visible position");
        totalPos -= 1;
        waitForUItem(withId(allNewsCardsBlockList), 1000);
        try {
            onView(withId(allNewsCardsBlockList))
                    .check(matches(RecyclerCustomMatcher
                            .atPosition(totalPos, hasDescendant(withId(newsItemCardTitle)))));
        } catch (NoMatchingViewException noCardsInList) {
            System.out.println(noCardsInList.getMessage());
        }
    }

    public void toggleNewsCardAtPosWithBtn(int cardPos) {
        Allure.step("Click ( ˅ / ˄ ) button to toggle news card state");
        waitForUItem(withId(newsCardToggleBtn), 1000);
        onView(withId(allNewsCardsBlockList)).perform(RecyclerViewActions.scrollToPosition(cardPos));
        onView(withId(allNewsCardsBlockList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(cardPos, DescendantViewActions
                        .performDescendantAction(withId(newsCardToggleBtn), click())));
    }

    public void scrollToCardAtPos(int cardPos) {
        Allure.step("Scroll to news card at position");
        waitForUItem(withId(allNewsCardsBlockList), 1000);
        onView(withId(allNewsCardsBlockList))
                .perform(RecyclerViewActions.scrollToPosition(cardPos));
    }

    public void clickFilterNewsBtn() {
        Allure.step("Click FILTER NEWS button to open FILTER NEWS page");
        waitForUItem(withId(filterNewsHeaderBtn), 1000);
        onView(withId(filterNewsHeaderBtn)).perform(click());
    }

    public void toggleNewsCardOrderWithSortBtn() {
        Allure.step("Toggle news item card order with SORT NEWS button");
        waitForUItem(withId(sortNewsHeaderBtn), 1000);
        onView(withId(sortNewsHeaderBtn)).perform(click());
    }

    public HashMap<Integer, String> createHashMapForCardsInList() {
        Allure.step("Create a hash map of structure <[int] Card Position, [String] Card Title> for later checks");

        DataHelper activity = new DataHelper();
        Activity currActivity = activity.getCurrentActivity();
        RecyclerView newsRecycler = currActivity.findViewById(allNewsCardsBlockList);
        RecyclerView.Adapter newsAdapter = newsRecycler.getAdapter();
        int allNewsCount = newsAdapter.getItemCount();
        HashMap<Integer, String> newsCardsMap = new HashMap<>();
        for (int i = 0; i < allNewsCount; i++) {

            GetTextAction action = new GetTextAction();
            onView(withId(allNewsCardsBlockList)).perform(scrollToPosition(i));
            onView(withId(allNewsCardsBlockList)).perform(RecyclerViewActions.actionOnItemAtPosition(i, DescendantViewActions
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

    public void confirmNewsItemDeletion() {
        Allure.step("Click OK on popup to confirm news deletion");
        onView(withText(okBtnText)).inRoot(withDecorView(Matchers.not(newsControlPanelDecorView)))
        .check(matches(isDisplayed())).perform(click());
    }

    public void cancelNewsItemDeletion() {
        Allure.step("Click CANCEL on popup to cancel news deletion");
        onView(withText(cancelBtnText)).inRoot(withDecorView(Matchers.not(newsControlPanelDecorView)))
                .check(matches(isDisplayed())).perform(click());
    }
}
