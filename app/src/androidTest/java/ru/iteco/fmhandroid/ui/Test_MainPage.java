package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import static TestUtils.RecyclerCustomMatcher.withIndex;

import android.content.Context;

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
import TestPageObjects.NewsPage;
import TestUtils.RecyclerCustomMatcher;
import io.qameta.allure.android.rules.ScreenshotRule;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.junit4.DisplayName;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class Test_MainPage {

    @Rule
    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule(ScreenshotRule.Mode.FAILURE,
            String.valueOf(System.currentTimeMillis()));
    MainPage mainPage = new MainPage();
    NewsPage newsPage = new NewsPage();
    NewsControlPanelPage newsControlPanelPage = new NewsControlPanelPage();
    CreatingNewsPage creatingNewsPage = new CreatingNewsPage();
    EditingNewsPage editingNewsPage = new EditingNewsPage();
    AuthorizationPage authPage = new AuthorizationPage();
    DataHelper testUser = new DataHelper();
    CustomAppBar customAppBar = new CustomAppBar();
    private Context myContext;

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
            mainPage.checkMainPageVisible();
        } catch (Exception notOnMainPage) {
            System.out.println(notOnMainPage.getMessage());
            customAppBar.goToMainPageWithMenuBtn();
        }

        myContext = InstrumentationRegistry.getInstrumentation().getContext();
        // треубуется для проверок, взаимодействующих с текстом в буфере
    }

    @Test
    @DisplayName("Check NEWS recycler visibility on MAIN page")
    public void shdDisplayNewsRecycler() {

        mainPage.checkMainPageVisible();
    }

    @Test
    @DisplayName("Check if added news (created with calendar & clock widgets) is visible on page")
    public void shdDisplayNewsItemAddedByWidget() {

        mainPage.goToNewsPageWithAllNewsBtn();
        newsPage.checkNewsPageVisible();
        newsPage.clickEditNewsBtn();
        newsControlPanelPage.checkNewsControlPageVisible(1000);
        newsControlPanelPage.clickAddNewsBtn();
        creatingNewsPage.checkPageVisibleByHeaderTitle();

        DataHelper testNewsData0 = new DataHelper();

        String testCategory = testNewsData0.makeTestNewsCategory();
        String testTitle = testNewsData0.makeTestNewsTitle();
        String testDescr = testNewsData0.makeTestNewsDescr();

        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
        customAppBar.goToMainPageWithMenuBtn();
        onView(withId(mainPage.getAllNewsCardsBlock()))
                .check(matches(allOf(hasDescendant(withText(testTitle)), isDisplayed())));
    }

    @Test
    @DisplayName("Check if 3 added news items (created with widgets) sre visible on page in right order")
    // МОЖЕТ ПАДАТЬ, Т.К. ПО ФАКТУ НОВОСТИ РАЗМЕЩАЮТСЯ КАК ПОПАЛО, ЕСЛИ ДОБАВЛЕНЫ В ОДМУ МИНУТУ
    // (судя по запросам в logcat, время добавления отражается не до секунд, а до минут)
    public void shdDisplayThreeNewsItemsAddedWithClipbrdInRightOrder() {
        // FIRST NEWS ITEM
        mainPage.goToNewsPageWithAllNewsBtn();
        newsPage.checkNewsPageVisible();
        newsPage.clickEditNewsBtn();
        newsControlPanelPage.checkNewsControlPageVisible(1000);
        newsControlPanelPage.clickAddNewsBtn();
        creatingNewsPage.checkPageVisibleByHeaderTitle();

        DataHelper testNewsData0 = new DataHelper();

        String testCategory0 = testNewsData0.makeTestNewsCategory();
        String testTitle0 = testNewsData0.makeTestNewsTitle();
        String testDescr0 = testNewsData0.makeTestNewsDescr();

        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory0);
        creatingNewsPage.insertNewsTitle(testTitle0);

        creatingNewsPage.insertNewsDescription(testDescr0);
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        // SECOND NEWS ITEM
        newsControlPanelPage.checkNewsControlPageVisible(10000);
        newsControlPanelPage.clickAddNewsBtn();
        creatingNewsPage.checkPageVisibleByHeaderTitle();

        DataHelper testNewsData1 = new DataHelper();

        String testCategory1 = testNewsData1.makeTestNewsCategory();
        String testTitle1 = testNewsData1.makeTestNewsTitle();
        String testDescr1 = testNewsData1.makeTestNewsDescr();

        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory1);
        creatingNewsPage.insertNewsTitle(testTitle1);

        creatingNewsPage.insertNewsDescription(testDescr1);
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        // THIRD NEWS ITEM
        newsControlPanelPage.checkNewsControlPageVisible(10000);
        newsControlPanelPage.clickAddNewsBtn();
        creatingNewsPage.checkPageVisibleByHeaderTitle();

        DataHelper testNewsData2 = new DataHelper();

        String testCategory2 = testNewsData2.makeTestNewsCategory();
        String testTitle2 = testNewsData2.makeTestNewsTitle();
        String testDescr2 = testNewsData2.makeTestNewsDescr();

        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory2);
        creatingNewsPage.insertNewsTitle(testTitle2);

        creatingNewsPage.insertNewsDescription(testDescr2);
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        customAppBar.goToMainPageWithMenuBtn();
        mainPage.checkMainPageVisible();
        // тут нужно помнить, что новости добавляются на страницу "снизу вверх"
        onView(withId(mainPage.getAllNewsCardsBlock()))
                .check(matches(RecyclerCustomMatcher.atPosition(0,
                        hasDescendant(withText(testTitle2)))));
        onView(withId(mainPage.getAllNewsCardsBlock()))
                .check(matches(RecyclerCustomMatcher.atPosition(1,
                        hasDescendant(withText(testTitle1)))));
        onView(withId(mainPage.getAllNewsCardsBlock()))
                .check(matches(RecyclerCustomMatcher.atPosition(2,
                        hasDescendant(withText(testTitle0)))));
    }

    @Test
    @DisplayName("Check if added news (created with clipboard paste) is NOT visible on page if it is not yet published")
    public void shdNotDisplayNewsItemIfYetToPublish() {

        mainPage.goToNewsPageWithAllNewsBtn();
        newsPage.checkNewsPageVisible();
        newsPage.clickEditNewsBtn();
        newsControlPanelPage.checkNewsControlPageVisible(1000);
        newsControlPanelPage.clickAddNewsBtn();
        creatingNewsPage.checkPageVisibleByHeaderTitle();

        DataHelper testNewsData0 = new DataHelper();

        String testCategory = testNewsData0.makeTestNewsCategory();
        String testTitle = testNewsData0.makeTestNewsTitle();
        String testDescr = testNewsData0.makeTestNewsDescr();
        String testPublDate = testNewsData0.makeTestNewsDate(1); // добавлен 1 день
        String testPublTime = testNewsData0.makeTestNewsTime(0);

        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);

        creatingNewsPage.insertNewsPublDateFromClip(testPublDate, myContext);
        creatingNewsPage.insertNewsPublTimeFromClip(testPublTime, myContext);

        creatingNewsPage.pressSaveToCreateNews();
        customAppBar.goToMainPageWithMenuBtn();
        onView(withId(mainPage.getAllNewsCardsBlock()))
                .check(matches(not(RecyclerCustomMatcher.atPosition(0,
                        hasDescendant(withText(testTitle))))));
    }

    @Test
    @DisplayName("Check if added news (created with clipboard paste) is NOT visible on page if it is switched to INACTIVE")
    public void shdNotDisplayNewsItemIfSetToInactive() {

        mainPage.goToNewsPageWithAllNewsBtn();
        newsPage.checkNewsPageVisible();
        newsPage.clickEditNewsBtn();
        newsControlPanelPage.checkNewsControlPageVisible(1000);
        newsControlPanelPage.clickAddNewsBtn();
        creatingNewsPage.checkPageVisibleByHeaderTitle();

        DataHelper testNewsData0 = new DataHelper();

        String testCategory = testNewsData0.makeTestNewsCategory();
        String testTitle = testNewsData0.makeTestNewsTitle();
        String testDescr = testNewsData0.makeTestNewsDescr();
        String testPublDate = testNewsData0.makeTestNewsDate(0);
        String testPublTime = testNewsData0.makeTestNewsTime(0);

        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);

        creatingNewsPage.insertNewsPublDateFromClip(testPublDate, myContext);
        creatingNewsPage.insertNewsPublTimeFromClip(testPublTime, myContext);

        creatingNewsPage.pressSaveToCreateNews();
        customAppBar.goToMainPageWithMenuBtn();
        // проверка отображения новости
        onView(withId(mainPage.getAllNewsCardsBlock()))
                .check(matches(RecyclerCustomMatcher.atPosition(0,
                        hasDescendant(withText(testTitle)))));
        // редактирование новости
        customAppBar.goToNewsPageWithMenuBtn();
        newsPage.clickEditNewsBtn();
        newsControlPanelPage.checkNewsControlPageVisible(1000);
        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle);
        // в этом методе очень сомнительная логика, но я не виноват, что в приложение постят другие
        // новости когда попало по 100 раз на дню, и для редактирования нужной новости нельзя просто
        // ткнуть в кнопку блока на фиксированной позициии в списке
        editingNewsPage.checkPageVisibleByHeaderTitle();
        editingNewsPage.changeNewsActivitySwitcherState();
        editingNewsPage.saveChangesToNewsItem();
        // переход обратно на глагне
        customAppBar.goToMainPageWithMenuBtn();
        // проверка видимости
        onView(withId(mainPage.getAllNewsCardsBlock()))
                .check(matches(not(RecyclerCustomMatcher.atPosition(0,
                        hasDescendant(withText(testTitle))))));
    }

    @Test
    @DisplayName("Check if NEWS block folds when [v] button is clicked")
    public void shdFoldNewsBlockWithArrowButton() {

        mainPage.checkMainPageVisible();
        mainPage.toggleNewsListAdapter();
        onView(withId(mainPage.getAllNewsBtn())).check(matches(not(isDisplayed())));
    }

    @Test
    @DisplayName("Check if NEWS block expands when [v] button is clicked in closed state")
    public void shdExpandNewsBlockWithArrowButton() {

        mainPage.toggleNewsListAdapter();
        onView(withId(mainPage.getAllNewsBtn())).check(matches(not(isDisplayed())));
        mainPage.toggleNewsListAdapter();
        onView(withId(mainPage.getAllNewsBtn())).check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Check if news item card expands when ( ˅ / ˄ ) button is clicked in default state")
    public void shdExpandNewsItemCardWithArrowBtn() {

        mainPage.goToNewsPageWithAllNewsBtn();
        newsPage.checkNewsPageVisible();
        newsPage.clickEditNewsBtn();
        newsControlPanelPage.checkNewsControlPageVisible(1000);
        newsControlPanelPage.clickAddNewsBtn();
        creatingNewsPage.checkPageVisibleByHeaderTitle();
        // создание новости на всякий случай
        DataHelper testNewsData0 = new DataHelper();
        String testCategory = testNewsData0.makeTestNewsCategory();
        String testTitle = testNewsData0.makeTestNewsTitle();
        String testDescr = testNewsData0.makeTestNewsDescr();
        String testPublDate = testNewsData0.makeTestNewsDate(0);
        String testPublTime = testNewsData0.makeTestNewsTime(0);

        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsPublDateFromClip(testPublDate, myContext);
        creatingNewsPage.insertNewsPublTimeFromClip(testPublTime, myContext);
        creatingNewsPage.pressSaveToCreateNews();

        customAppBar.goToMainPageWithMenuBtn();
        mainPage.checkMainPageVisible();

        mainPage.toggleNewsItemCard(0);
        onView(withIndex(withId(mainPage.getNewsCardView()), 0))
                .check(matches(hasDescendant(allOf(withId(mainPage.getNewsCardDescr()), isDisplayed()))));
    }

    @Test
    @DisplayName("Check if news item card closes when ( ˅ / ˄ ) button is clicked in expanded state")
    public void shdCloseNewsItemCardWithArrowBtn() {

        mainPage.goToNewsPageWithAllNewsBtn();
        newsPage.checkNewsPageVisible();
        newsPage.clickEditNewsBtn();
        newsControlPanelPage.checkNewsControlPageVisible(1000);
        newsControlPanelPage.clickAddNewsBtn();
        creatingNewsPage.checkPageVisibleByHeaderTitle();
        // создание новости на всякий случай
        DataHelper testNewsData0 = new DataHelper();
        String testCategory = testNewsData0.makeTestNewsCategory();
        String testTitle = testNewsData0.makeTestNewsTitle();
        String testDescr = testNewsData0.makeTestNewsDescr();
        String testPublDate = testNewsData0.makeTestNewsDate(0);
        String testPublTime = testNewsData0.makeTestNewsTime(0);

        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsPublDateFromClip(testPublDate, myContext);
        creatingNewsPage.insertNewsPublTimeFromClip(testPublTime, myContext);
        creatingNewsPage.pressSaveToCreateNews();

        customAppBar.goToMainPageWithMenuBtn();
        mainPage.checkMainPageVisible();
        mainPage.toggleNewsItemCard(0);
        onView(withIndex(withId(mainPage.getNewsCardView()), 0))
                .check(matches(hasDescendant(allOf(withId(mainPage.getNewsCardDescr()), isDisplayed()))));
        mainPage.toggleNewsItemCard(0);
        onView(withIndex(withId(mainPage.getNewsCardView()), 0))
                .check(matches(hasDescendant(allOf(withId(mainPage.getNewsCardDescr()), not(isDisplayed())))));
    }
}
