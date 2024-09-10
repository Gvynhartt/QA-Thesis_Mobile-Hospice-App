package TestPageObjects;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static TestData.DataHelper.waitForUItem;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import org.hamcrest.Matchers;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
public class NewsFilterPage {

    private View newsFilterPageDecorView;
    private final int filterPageTitle = R.id.filter_news_title_text_view;
    private final int catgrTextInputField = R.id.news_item_category_text_auto_complete_text_view;
    private final int applyFilterBtn = R.id.filter_button;
    private final int cancelFilterBtn = R.id.cancel_button;
    private final int dateStartInputField = R.id.news_item_publish_date_start_text_input_edit_text;
    private final int dateEndInputField = R.id.news_item_publish_date_end_text_input_edit_text;
    private final String clipPopupText = "Paste";
    private final String wrongPeriodToastText = "Wrong period";

    public String getWrongPeriodToastText() {
        return wrongPeriodToastText;
    }

    public void checkPageVisibleByTitle() {
        Allure.step("Check that page is displayed by title");
        waitForUItem(withId(filterPageTitle), 1000);
        onView(withId(filterPageTitle)).check(matches(isDisplayed()));
    }

    public void insertNewsCatgryWithKeybd(String newsCategory) {
        Allure.step("Insert news category in text field with keyboard");
        waitForUItem(withId(catgrTextInputField), 1000);
        onView(withId(catgrTextInputField)).perform(click()).perform(replaceText(newsCategory));
        closeSoftKeyboard();
    }

    public void insertFilterPeriodStartFromClip(String dateForInput, Context passedContext) {
        Allure.step("Insert sorting period start date from clipboard");
        waitForUItem(withId(dateStartInputField), 1000);

        ClipboardManager clipboard = (ClipboardManager) passedContext
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, dateForInput);

        if (clipboard == null) return;
        clipboard.setPrimaryClip(clip);
        onView(withId(dateStartInputField)).perform(longClick());
        onView(withText(clipPopupText)).inRoot(withDecorView(Matchers.not(newsFilterPageDecorView)))
                .check(matches(isDisplayed()));

        int clickCount = 0;
        while (clipboard.hasPrimaryClip() && clickCount <= 2) {
            try {
                onView(withText(clipPopupText)).inRoot(withDecorView(Matchers.not(newsFilterPageDecorView)))
                        .perform(click());
            } catch (Exception clipPopupException) {
                System.out.println(clipPopupException.getMessage());
            }
            clickCount += 1;
        }

        if (clipboard.hasPrimaryClip()) {
            clipboard.clearPrimaryClip();
        }
    }

    public void insertFilterPeriodEndFromClip(String dateForInput, Context passedContext) {
        Allure.step("Insert sorting period end date from clipboard");
        waitForUItem(withId(dateEndInputField), 1000);

        ClipboardManager clipboard = (ClipboardManager) passedContext
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, dateForInput);

        if (clipboard == null) return;
        clipboard.setPrimaryClip(clip);
        onView(withId(dateEndInputField)).perform(longClick());
        onView(withText(clipPopupText)).inRoot(withDecorView(Matchers.not(newsFilterPageDecorView)))
                .check(matches(isDisplayed()));

        int clickCount = 0;
        while (clipboard.hasPrimaryClip() && clickCount <= 2) {
            try {
                onView(withText(clipPopupText)).inRoot(withDecorView(Matchers.not(newsFilterPageDecorView)))
                        .perform(click());
            } catch (Exception clipPopupException) {
                System.out.println(clipPopupException.getMessage());
            }
            clickCount += 1;
        }

        if (clipboard.hasPrimaryClip()) {
            clipboard.clearPrimaryClip();
        }
    }

    public void clickFilterBtn() {
        Allure.step("Click FILTER button to apply filters");
        waitForUItem(withId(applyFilterBtn), 1000);
        onView(withId(applyFilterBtn)).perform(click());
    }

    public void clickCancelBtn() {
        Allure.step("Click CANCEL button to cancel filters");
        waitForUItem(withId(cancelFilterBtn), 1000);
        onView(withId(cancelFilterBtn)).perform(click());
    }
}
