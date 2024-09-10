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

public class ControlPanelFilterPage {

    private View controlPanelFilterPageDecorView;
    private final int filterPageTitle = R.id.filter_news_title_text_view;
    private final int catgrTextInputField = R.id.news_item_category_text_auto_complete_text_view;
    private final int periodStartInputField = R.id.news_item_publish_date_start_text_input_edit_text;
    private final int periodEndInputField = R.id.news_item_publish_date_end_text_input_edit_text;
    private final int applyFilterBtn = R.id.filter_button;
    private final int cancelFilterBtn = R.id.cancel_button;
    private final String clipPopupText = "Paste";
    private final String okWidgetBtnText = "OK";
    private final String wrongPeriodToastText = "Wrong period";

    private final int activeCheckbox = R.id.filter_news_active_material_check_box;
    private final int inactiveCheckbox = R.id.filter_news_inactive_material_check_box;

    public String getWrongPeriodToastText() { return wrongPeriodToastText; }


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
        waitForUItem(withId(periodStartInputField), 1000);

        ClipboardManager clipboard = (ClipboardManager) passedContext
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, dateForInput);

        if (clipboard == null) return;
        clipboard.setPrimaryClip(clip);
        onView(withId(periodStartInputField)).perform(longClick());
        onView(withText(clipPopupText)).inRoot(withDecorView(Matchers.not(controlPanelFilterPageDecorView)))
                .check(matches(isDisplayed()));

        int clickCount = 0;
        while (clipboard.hasPrimaryClip() && clickCount <= 1) {
            try {
                onView(withText(clipPopupText)).inRoot(withDecorView(Matchers.not(controlPanelFilterPageDecorView)))
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
        // по какой-то причине именно в этом поле ввод даты из буфера нестабилен
        // есть подозрение, что дело в некорректной обработке longCLick со стороны Espresso
        waitForUItem(withId(periodEndInputField), 1000);

        ClipboardManager clipboard = (ClipboardManager) passedContext
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, dateForInput);

        if (clipboard == null) return;
        clipboard.setPrimaryClip(clip);
        onView(withId(periodEndInputField)).perform(longClick());
        onView(withText(clipPopupText)).inRoot(withDecorView(Matchers.not(controlPanelFilterPageDecorView)))
                .check(matches(isDisplayed()));

        int clickCount = 0;
        while (clipboard.hasPrimaryClip() && clickCount <= 1) {
            try {
                onView(withText(clipPopupText)).inRoot(withDecorView(Matchers.not(controlPanelFilterPageDecorView)))
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

    public void insertFilterPeriodStartWidgetNoOffset() {
        Allure.step("Insert sorting period start date with calendar widget");
        waitForUItem(withId(periodStartInputField), 1000);
        onView(withId(periodStartInputField)).perform(click());
        onView(withText(okWidgetBtnText))
                .inRoot(withDecorView(Matchers.not(controlPanelFilterPageDecorView)))
                .check(matches(isDisplayed()))
                .perform(click());
    }

    public void insertFilterPeriodEndWidgetNoOffset() {
        Allure.step("Insert sorting period end date with calendar widget");
        waitForUItem(withId(periodStartInputField), 1000);
        onView(withId(periodEndInputField)).perform(click());
        onView(withText(okWidgetBtnText))
                .inRoot(withDecorView(Matchers.not(controlPanelFilterPageDecorView)))
                .check(matches(isDisplayed()))
                .perform(click());
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

    public void toggleActiveCheckbox() {
        Allure.step("Click ACTIVE checkbox to toggle its state");
        waitForUItem(withId(activeCheckbox), 1000);
        onView(withId(activeCheckbox)).perform(click());
    }

    public void toggleInactiveCheckbox() {
        Allure.step("Click INACTIVE checkbox to toggle its state");
        waitForUItem(withId(inactiveCheckbox), 1000);
        onView(withId(inactiveCheckbox)).perform(click());
    }

    public void insertNewsCatgrWithDDList(String catgrToPick) {
        Allure.step("Input news category using drop-down list");
        waitForUItem(withId(catgrTextInputField), 1000);
        onView(withId(catgrTextInputField)).perform(click());
        closeSoftKeyboard();
        // чмок в пупок тому, кто кодил фильтр на этой странице
        onView(withText(catgrToPick)).inRoot(withDecorView(Matchers.not(controlPanelFilterPageDecorView)))
                .check(matches(isDisplayed())).perform(click());
    }


}
