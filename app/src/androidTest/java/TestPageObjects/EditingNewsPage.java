package TestPageObjects;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static TestData.DataHelper.waitForUItem;
import static TestUtils.RecyclerCustomMatcher.childAtPosition;

import android.view.KeyEvent;
import android.view.View;

import androidx.test.espresso.action.ViewActions;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;

public class EditingNewsPage {

    private View editingNewsPageDecorView;
    private final int newsStateSwitcher = R.id.switcher;
    private final int saveChangesBtn = R.id.save_button;
    private final String newsSwitcherActiveText = "Active";
    private final String newsSwitcherNotActiveText = "Not active";
    private final String pageHeaderTitleText = "Editing";
    private final String toastConfirmBtnText = "OK";
    private final String andrAppCompatWidget = "androidx.appcompat.widget.AppCompatImageButton";
    private final String andrAppCompatEditText = "androidx.appcompat.widget.AppCompatEditText";
    private final String andDayPickerWidget = "android.widget.DayPickerView";
    private final String andrTimePickerText = "android.widget.TextInputTimePickerView";
    private final String andrWidgetRelLayt = "android.widget.RelativeLayout";
    private final String nextMonthBtnDescr = "Next month";
    private final String switchToTextTimeInput = "Switch to text input mode for the time input.";
    private final String saveFailText = "Saving failed. Try again later";
    private final int editNewsAppHeaderTitle = R.id.custom_app_bar_title_text_view;
    private final int catgrTextInputField = R.id.news_item_category_text_auto_complete_text_view;
    private final int titleTextInputField = R.id.news_item_title_text_input_edit_text;
    private final int descrTextInputField = R.id.news_item_description_text_input_edit_text;
    private final int publDateInputField = R.id.news_item_publish_date_text_input_edit_text;
    private final int publTimeInputField = R.id.news_item_publish_time_text_input_edit_text;

    public int getPublTimeInputField() { return publTimeInputField; }
    public String getSaveFailText() { return saveFailText; }

    public void checkPageVisibleByHeaderTitle() {
        Allure.step("Check EDITING NEWS page visibility by header title text");
        waitForUItem(withText(pageHeaderTitleText), 1000);
        onView(withId(editNewsAppHeaderTitle)).check(matches(withText(pageHeaderTitleText)));
    }

    public void changeNewsActivitySwitcherState() {
        Allure.step("Change news item state with activity switcher");
        waitForUItem(withId(newsStateSwitcher), 1000);
        onView(withId(newsStateSwitcher)).perform(click());
    }

    public void changeNewsCatgryWithKeybd(String newCategory) {
        Allure.step("Change news item category (using keyboard)");
        waitForUItem(withId(catgrTextInputField), 1000);
        onView(withId(catgrTextInputField)).perform(replaceText(newCategory));
        closeSoftKeyboard();
    }

    public void saveChangesToNewsItem() {
        Allure.step("Save changes made to news item by pressing SAVE button");
        waitForUItem(withId(saveChangesBtn), 1000);
        onView(withId(saveChangesBtn)).perform(click());
    }

    public void changeNewsTitleWithKeybd(String newTitle) {
        Allure.step("Change news item title (using keyboard)");
        waitForUItem(withId(titleTextInputField), 1000);
        onView(withId(titleTextInputField)).perform(click()).perform(replaceText(newTitle));
        closeSoftKeyboard();
    }

    public void changeNewsDescrWithKeybd(String newDescr) {
        Allure.step("Change news item title (using keyboard)");
        waitForUItem(withId(descrTextInputField), 1000);
        onView(withId(descrTextInputField)).perform(click()).perform(replaceText(newDescr));
        closeSoftKeyboard();
    }
    public void changeNewsPublDateWithWidget() {
        Allure.step("Insert new publication date with calendar widget");
        waitForUItem(withId(publDateInputField), 1000);
        onView(withId(publDateInputField)).perform(click());
        onView(allOf(withClassName(is(andrAppCompatWidget)),
                withContentDescription(nextMonthBtnDescr))).perform(click());
        onView(withClassName(is(andDayPickerWidget))).inRoot(withDecorView(not(editingNewsPageDecorView)))
                .perform(pressKey(KeyEvent.KEYCODE_DPAD_DOWN),
                        pressKey(KeyEvent.KEYCODE_DPAD_DOWN),
                        pressKey(KeyEvent.KEYCODE_DPAD_DOWN),
                        pressKey(KeyEvent.KEYCODE_ENTER));

        // Суть в том, чтобы поменять число. Потому что вбивать дату в поле для
        // слабых. Этим кодом QAnami мы попадаем на 1 число следующего месяца.

        onView(withText(toastConfirmBtnText))
                .inRoot(withDecorView(not(editingNewsPageDecorView)))
                .perform(click());
    }

    public void changeNewsPublTimeWithWidget(String hrsToSet, String minsToSet) {
        Allure.step("Insert new publication time with clock widget");
        waitForUItem(withId(publTimeInputField), 1000);
        onView(withId(publTimeInputField)).perform(click());
        onView(allOf(withClassName(is(andrAppCompatWidget)),
                withContentDescription(switchToTextTimeInput)))
                .perform(click());
        // далее вводим число часов
        onView(allOf(withClassName(is(andrAppCompatEditText)),
                        childAtPosition(allOf(withClassName(is(andrWidgetRelLayt)),
                                childAtPosition(withClassName(is(andrTimePickerText)),
                                                1)),0)))
                .perform(replaceText(hrsToSet), ViewActions.closeSoftKeyboard());
        // затем число минут
        onView(allOf(withClassName(is(andrAppCompatEditText)),
                childAtPosition(allOf(withClassName(is(andrWidgetRelLayt)),
                                childAtPosition(withClassName(is(andrTimePickerText)), 1)),3)))
                .perform(replaceText(minsToSet), ViewActions.closeSoftKeyboard());
        // подтверждаем и закрываем виджет
        onView(withText(toastConfirmBtnText))
                .inRoot(withDecorView(not(editingNewsPageDecorView)))
                .perform(click());
    }

}
