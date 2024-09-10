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

public class CreatingNewsPage {

    private View createNewsDecorView;
    private final int catgrTextField = R.id.news_item_category_text_auto_complete_text_view;
    private final int titleTextField = R.id.news_item_title_text_input_edit_text;
    private final int publDateInputField = R.id.news_item_publish_date_text_input_edit_text;
    private final int publTimeInputField = R.id.news_item_publish_time_text_input_layout;
    private final int descrTextField = R.id.news_item_description_text_input_edit_text;
    private final int saveBtn = R.id.save_button;
    private final int cancelBtn = R.id.cancel_button;
    private final int stateSwitcher = R.id.switcher;
    private final String pageHeaderTitleText = "Creating";
    private final String toastConfirmBtnText = "OK";
    private final String clipPopupText = "Paste";
    private final String emptyFieldsPopupText = "Fill empty fields";
    private final String saveFailText = "Saving failed. Try again later";
    private final String notActiveSwitcherText = "Not active";

    public String getEmptyFieldsPopupText() { return emptyFieldsPopupText; }
    public String getSaveFailText() { return saveFailText; }
    public int getStateSwitcher() { return stateSwitcher; }
    public String getNotActiveSwitcherText() { return notActiveSwitcherText; }

    public void checkPageVisibleByHeaderTitle() {
        Allure.step("Check CREATING NEWS page visibility by header title text");
        waitForUItem(withText(pageHeaderTitleText), 1000);
    }

    public void insertNewsCatgrWithKeybd(String newsCategory) {
        Allure.step("Insert news category - using keyboard (not list)");
        waitForUItem(withId(catgrTextField), 1000);
        onView(withId(catgrTextField)).perform(click()).perform(replaceText(newsCategory));
        closeSoftKeyboard();
    }

    public void insertNewsTitle(String newsTitle) {
        Allure.step("Insert news title with soft keyboard");
        waitForUItem(withId(titleTextField), 1000);
        onView(withId(titleTextField)).perform(click()).perform(replaceText(newsTitle));
        closeSoftKeyboard();
    }

    public void insertNewsPublDateByWidgetNoOffset() {
        Allure.step("Insert news publication date - using calendar widget (yay)");
        waitForUItem(withId(publDateInputField), 1000);
        onView(withId(publDateInputField)).perform(click());
        onView(withText(toastConfirmBtnText)).inRoot(withDecorView(Matchers.not(createNewsDecorView)))
                .check(matches(isDisplayed())).perform(click());
    }

    public void insertNewsPublTimeByWidgetNoOffset() {
        Allure.step("Insert news publication time - using clock widget (yay)");
        waitForUItem(withId(publTimeInputField), 1000);
        onView(withId(publTimeInputField)).perform(click());
        onView(withText(toastConfirmBtnText)).inRoot(withDecorView(Matchers.not(createNewsDecorView)))
                .check(matches(isDisplayed())).perform(click());
    }

    public void insertNewsPublDateFromClip(String dateForInput, Context passedContext) {
        Allure.step("Insert news publication date - using clipboard");
        ClipboardManager clipboard = (ClipboardManager) passedContext
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, dateForInput);
        if (clipboard == null) return;
        clipboard.setPrimaryClip(clip);

        waitForUItem(withId(publDateInputField), 1000);
        onView(withId(publDateInputField)).perform(longClick());
        onView(withText(clipPopupText)).inRoot(withDecorView(Matchers.not(createNewsDecorView)))
                .check(matches(isDisplayed()));
        int clickCount = 0;
        while (clipboard.hasPrimaryClip() && clickCount <= 3) {
            // Потому что по этой кнопке нужно тыкать до посинения,
            // одного клика недостаточно. Если просто отправить клик
            // по вспл. окну вне тестируемого view, Нескафе может показать
            // фигу. Почему? Да чтоб я знал. Возможно, иерархия  нужного view
            // обновляется слишком медленно.
            try {
                onView(withText(clipPopupText)).inRoot(withDecorView(Matchers.not(createNewsDecorView)))
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

    public void insertNewsPublTimeFromClip(String timeForInput, Context passedContext) {
        Allure.step("Insert news publication time - using clipboard");
        ClipboardManager clipboard = (ClipboardManager) passedContext
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, timeForInput);
        if (clipboard == null) return;
        clipboard.setPrimaryClip(clip);

        waitForUItem(withId(publTimeInputField), 1000);
        onView(withId(publTimeInputField)).perform(longClick());
        onView(withText(clipPopupText)).inRoot(withDecorView(Matchers.not(createNewsDecorView)))
                .check(matches(isDisplayed()));
        int clickCount = 0;
        while (clipboard.hasPrimaryClip() && clickCount <= 3) {
            try {
                onView(withText(clipPopupText)).inRoot(withDecorView(Matchers.not(createNewsDecorView)))
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

    public void insertNewsDescription(String newsDescr) {
        Allure.step("Insert news description with soft keyboard");
        waitForUItem(withId(descrTextField), 1000);
        onView(withId(descrTextField)).perform(click()).perform(replaceText(newsDescr));
        closeSoftKeyboard();
    }

    public void pressSaveToCreateNews() {
        Allure.step("Confirm news creation by pressing SAVE button");
        waitForUItem(withId(saveBtn), 1000);
        onView(withId(saveBtn)).perform(click());
    }

    public void pressCancelToExitCreatingNews() {
        Allure.step("Cancel news creation by pressing CANCEL button");
        waitForUItem(withId(cancelBtn), 1000);
        onView(withId(cancelBtn)).perform(click());
        onView(withText(toastConfirmBtnText))
                .inRoot(withDecorView(Matchers.not(createNewsDecorView)))
                .check(matches(isDisplayed()))
                .perform(click());
    }

    public void toggleNewsActivityWithSwitcher() {
        Allure.step("Toggle news item activity state by pressing SWITCHER");
        // но есть нюанс
        waitForUItem(withId(stateSwitcher), 1000);
        onView(withId(stateSwitcher)).perform(click());
    }
}
