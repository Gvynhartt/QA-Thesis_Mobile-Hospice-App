package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import android.content.Context;
import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;


import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import TestData.DataHelper;
import TestPageObjects.AuthorizationPage;
import TestPageObjects.ControlPanelFilterPage;
import TestPageObjects.CreatingNewsPage;
import TestPageObjects.CustomAppBar;
import TestPageObjects.EditingNewsPage;
import TestPageObjects.MainPage;
import TestPageObjects.NewsControlPanelPage;
import TestPageObjects.NewsPage;
import TestUtils.RecyclerCustomMatcher;
import io.qameta.allure.android.rules.ScreenshotRule;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Feature;
import io.qameta.allure.kotlin.Flaky;
import io.qameta.allure.kotlin.junit4.DisplayName;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class Test_NewsControlPanelPage {

    @Rule
    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);
    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule(ScreenshotRule.Mode.FAILURE,
            String.valueOf(System.currentTimeMillis()));
    View controlPanelFilterPageDecorView;
    View newsControlPanelPageDecorView;
    View creatingNewsDecorView;
    View editingNewsPageDecorView;
    MainPage mainPage = new MainPage();
    NewsPage newsPage = new NewsPage();
    NewsControlPanelPage newsControlPanelPage = new NewsControlPanelPage();
    CreatingNewsPage creatingNewsPage = new CreatingNewsPage();
    EditingNewsPage editingNewsPage = new EditingNewsPage();
    ControlPanelFilterPage controlPanelFilterPage = new ControlPanelFilterPage();
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
            newsControlPanelPage.checkNewsControlPageVisible(1000);
        } catch (Exception notOnNewsControlPanelPage) {
            System.out.println(notOnNewsControlPanelPage.getMessage());
            customAppBar.goToNewsPageWithMenuBtn();
            newsPage.clickEditNewsBtn();
        }

        myContext = InstrumentationRegistry.getInstrumentation().getContext();
        // треубуется для проверок, взаимодействующих с текстом в буфере
    }

    @Test
    @Feature("Recycler visibility")
    @DisplayName("Check news recycler visibility on NEWS CONTROL PANEL page")
    public void shdDisplayNewsRecycList() {

        newsControlPanelPage.checkAllNewsRecycListVisible();
    }

    @Test
    @Feature("Card visibility")
    @DisplayName("Check that news item is displayed on page if published & active")
    public void shdDisplayNewsOnPageIfActiveAndPublished() {

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

        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle);
    }

    @Test
    @Feature("Card visibility")
    @DisplayName("Check that news item is displayed on page if published, but set to INACTIVE")
    public void shdDisplNewsIfPublButInactive() {

        newsControlPanelPage.clickAddNewsBtn();
        creatingNewsPage.checkPageVisibleByHeaderTitle();

        DataHelper testNewsData0 = new DataHelper();

        String testCategory = testNewsData0.makeTestNewsCategory();
        String testTitle = testNewsData0.makeTestNewsTitle();
        String testDescr = testNewsData0.makeTestNewsDescr();
        // создание новости
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
        // редактирование новости
        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle);
        editingNewsPage.checkPageVisibleByHeaderTitle();
        editingNewsPage.changeNewsActivitySwitcherState();
        editingNewsPage.saveChangesToNewsItem();

        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle);
    }

    @Test
    @Feature("Card visibility")
    @DisplayName("Check that news item is displayed on page if set to ACTIVE, but not yet published")
    public void shdDisplNewsIfActiveButNotPublished() {

        newsControlPanelPage.clickAddNewsBtn();
        creatingNewsPage.checkPageVisibleByHeaderTitle();

        DataHelper testNewsData0 = new DataHelper();

        String testCategory = testNewsData0.makeTestNewsCategory();
        String testTitle = testNewsData0.makeTestNewsTitle();
        String testDescr = testNewsData0.makeTestNewsDescr();
        String testPublDate = testNewsData0.makeTestNewsDate(3);

        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsPublDateFromClip(testPublDate, myContext);
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle);
    }

    @Test
    @Feature("Card visibility")
    @DisplayName("Check that news item is displayed on page if INACTIVE and not yet published")
    public void shdDisplNewsWhenInactiveAndUnpubl() {

        newsControlPanelPage.clickAddNewsBtn();
        creatingNewsPage.checkPageVisibleByHeaderTitle();

        DataHelper testNewsData0 = new DataHelper();

        String testCategory = testNewsData0.makeTestNewsCategory();
        String testTitle = testNewsData0.makeTestNewsTitle();
        String testDescr = testNewsData0.makeTestNewsDescr();
        String testPublDate = testNewsData0.makeTestNewsDate(2);

        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsPublDateFromClip(testPublDate, myContext);
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle);
        editingNewsPage.checkPageVisibleByHeaderTitle();
        editingNewsPage.changeNewsActivitySwitcherState();
        editingNewsPage.saveChangesToNewsItem();

        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle);
    }

    @Test
    @Feature("Expand card")
    @DisplayName("Check that news card expands when ( ˅ / ˄ ) button is clicked in default state")
    public void shdExpandNewsItemCardWithBtn() {
        int cardPos = 0;
        newsControlPanelPage.checkNewsRecyclerHasAtLeastPos(1);
        newsControlPanelPage.toggleNewsCardAtPosWithBtn(cardPos);
        onView(RecyclerCustomMatcher.withIndex(withId(newsControlPanelPage.getNewsItemCard()), cardPos))
                .check(matches(hasDescendant(allOf(withId(newsControlPanelPage.getNewsCardDescr()), isDisplayed()))));
    }

    @Test
    @Feature("Expand card")
    @DisplayName("Check if news card closes on ( ˅ / ˄ ) button press (from opened state)")
    public void shdCloseNewsItemCardWithBtnWhenExpanded() {
        int cardPos = 0;
        newsControlPanelPage.checkNewsRecyclerHasAtLeastPos(1);
        newsControlPanelPage.toggleNewsCardAtPosWithBtn(cardPos);
        onView(RecyclerCustomMatcher.withIndex(withId(newsControlPanelPage.getNewsItemCard()), cardPos))
                .check(matches(hasDescendant(allOf(withId(newsControlPanelPage.getNewsCardDescr()), isDisplayed()))));

        newsControlPanelPage.toggleNewsCardAtPosWithBtn(cardPos);
        onView(RecyclerCustomMatcher.withIndex(withId(newsControlPanelPage.getNewsItemCard()), cardPos))
                .check(matches(hasDescendant(allOf(withId(newsControlPanelPage.getNewsCardDescr()), not(isDisplayed())))));
    }

    @Test
    @Feature("Sorting")
    @DisplayName("Check if news card order is reversed after SORT NEWS LIST button is pressed")
    public void shdReverseNewsItemOrderWhenSortBtnClick() {

        newsControlPanelPage.checkNewsRecyclerHasAtLeastPos(2);
        HashMap<Integer, String> cardMap0 = newsControlPanelPage.createHashMapForCardsInList();
        newsControlPanelPage.toggleNewsCardOrderWithSortBtn();
        boolean isListReversed = newsControlPanelPage.checkIfCardListOrderIsReversed(cardMap0);
        Assert.assertTrue("Check if [isListReversed] yields TRUE", isListReversed == true);
    }

    @Test
    @Feature("Sorting")
    @DisplayName("Check if news card order is reversed to normal after SORT NEWS button is pressed, when reversed previously")
    public void shdReverseNewsItemOrderToNormalWithDoubleSortBtnClick() {
        newsControlPanelPage.checkNewsRecyclerHasAtLeastPos(2);
        HashMap<Integer, String> cardMap0 = newsControlPanelPage.createHashMapForCardsInList();
        newsControlPanelPage.toggleNewsCardOrderWithSortBtn();
        boolean isListReversed = newsControlPanelPage.checkIfCardListOrderIsReversed(cardMap0);
        Assert.assertTrue("Check if [isListReversed] yields TRUE", isListReversed == true);

        HashMap<Integer, String> cardMap1 = newsControlPanelPage.createHashMapForCardsInList();
        newsControlPanelPage.toggleNewsCardOrderWithSortBtn();
        boolean isReversedAgain = newsControlPanelPage.checkIfCardListOrderIsReversed(cardMap1);
        Assert.assertTrue("Check if [isReversedAgain] yields TRUE", isReversedAgain == true);
    }

    @Test
    @Feature("Filters: window access")
    @DisplayName("Check that FILTER NEWS page opens when FILTER NEWS button clicked")
    public void shdOpenFilterPageWhenFilterBtnClick() {

        newsControlPanelPage.clickFilterNewsBtn();
        controlPanelFilterPage.checkPageVisibleByTitle();
    }

    @Test
    @Feature("Filters: category")
    @DisplayName("Check that filter shows item of selected category on page")
    public void shdShowItemOfChosenCatgr() {

        newsControlPanelPage.clickAddNewsBtn();

        DataHelper testNewsData = new DataHelper();
        String testCategory = testNewsData.makeTestNewsCategory();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testDescr = testNewsData.makeTestNewsDescr();

        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
        newsControlPanelPage.clickFilterNewsBtn();
        controlPanelFilterPage.insertNewsCatgrWithDDList(testCategory);
        controlPanelFilterPage.clickFilterBtn();
        newsControlPanelPage.checkAllNewsRecycListVisible();
        newsControlPanelPage.checkNewsRecyclerHasAtLeastPos(1);

        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle);
    }

    @Test
    @Feature("Filters: category")
    @DisplayName("Check that category filter does NOT show news of other categories on NEWS CONTROL PANEL page")
    public void shdNotDisplayNewsOfOtherCategories() {

        // ОБЪЯВЛЕНИЕ
        newsControlPanelPage.clickAddNewsBtn();

        DataHelper newsAnnounce = new DataHelper();
        String cat1 = "Объявление";
        String title1 = newsAnnounce.makeTestNewsTitle();
        String descr1 = newsAnnounce.makeTestNewsDescr();

        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(cat1);
        creatingNewsPage.insertNewsTitle(title1);
        creatingNewsPage.insertNewsDescription(descr1);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

//        // ДЕНЬ РОЖДЕНИЯ
        newsControlPanelPage.clickAddNewsBtn();

        DataHelper newsBirthday = new DataHelper();
        String cat2 = "День рождения";
        String title2 = newsBirthday.makeTestNewsTitle();
        String descr2 = newsBirthday.makeTestNewsDescr();
        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(cat2);
        creatingNewsPage.insertNewsTitle(title2);
        creatingNewsPage.insertNewsDescription(descr2);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
//        // ЗАПРЛАТА
        newsControlPanelPage.clickAddNewsBtn();

        DataHelper newsSalary = new DataHelper();
        String cat3 = "Зарплата";
        String title3 = newsSalary.makeTestNewsTitle();
        String descr3 = newsSalary.makeTestNewsDescr();

        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(cat3);
        creatingNewsPage.insertNewsTitle(title3);
        creatingNewsPage.insertNewsDescription(descr3);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
//        // ПРОФСОЮЗ
        newsControlPanelPage.clickAddNewsBtn();

        DataHelper newsUnion = new DataHelper();
        String cat4 = "Профсоюз";
        String title4 = newsUnion.makeTestNewsTitle();
        String descr4 = newsUnion.makeTestNewsDescr();
        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(cat4);
        creatingNewsPage.insertNewsTitle(title4);
        creatingNewsPage.insertNewsDescription(descr4);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
//        // ПРАЗДНИК
        newsControlPanelPage.clickAddNewsBtn();

        DataHelper newsHoliday = new DataHelper();
        String cat5 = "Праздник";
        String title5 = newsHoliday.makeTestNewsTitle();
        String descr5 = newsHoliday.makeTestNewsDescr();
        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(cat5);
        creatingNewsPage.insertNewsTitle(title5);
        creatingNewsPage.insertNewsDescription(descr5);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
//        // МАССАЖ
        newsControlPanelPage.clickAddNewsBtn();

        DataHelper newsMassage = new DataHelper();
        String cat6 = "Массаж";
        String title6 = newsMassage.makeTestNewsTitle();
        String descr6 = newsMassage.makeTestNewsDescr();
        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(cat6);
        creatingNewsPage.insertNewsTitle(title6);
        creatingNewsPage.insertNewsDescription(descr6);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
        newsControlPanelPage.clickAddNewsBtn();
//        // БЛАГОДАРНОСТЬ
        DataHelper newsGratitude = new DataHelper();
        String cat7 = "Благодарность";
        String title7 = newsGratitude.makeTestNewsTitle();
        String descr7 = newsGratitude.makeTestNewsDescr();
        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(cat7);
        creatingNewsPage.insertNewsTitle(title7);
        creatingNewsPage.insertNewsDescription(descr7);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
//        // НУЖНА ПОМОЩЬ
        newsControlPanelPage.clickAddNewsBtn();

        DataHelper newsAssNeeded = new DataHelper();
        String cat8 = "Нужна помощь";
        String title8 = newsAssNeeded.makeTestNewsTitle();
        String descr8 = newsAssNeeded.makeTestNewsDescr();
        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(cat8);
        creatingNewsPage.insertNewsTitle(title8);
        creatingNewsPage.insertNewsDescription(descr8);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.clickFilterNewsBtn();
        controlPanelFilterPage.insertNewsCatgrWithDDList(cat4);
        controlPanelFilterPage.clickFilterBtn();
        // далее проверяем, скрыто ли всё, кроме 4 категории
        newsControlPanelPage.checkNewsRecyclerHasAtLeastPos(1);
        newsControlPanelPage.checkIfNewsListHasTitle(false, title1);
        newsControlPanelPage.checkIfNewsListHasTitle(false, title2);
        newsControlPanelPage.checkIfNewsListHasTitle(false, title3);
        newsControlPanelPage.checkIfNewsListHasTitle(false, title5);
        newsControlPanelPage.checkIfNewsListHasTitle(false, title6);
        newsControlPanelPage.checkIfNewsListHasTitle(false, title7);
        newsControlPanelPage.checkIfNewsListHasTitle(false, title8);

    }

    @Test
    @Flaky
    @Feature("Filters: publication date")
    @DisplayName("Check that date filter DOES show news between specified dates")
    public void shdDisplNewsBetweenChosenDates() {

        newsControlPanelPage.clickAddNewsBtn();

        DataHelper testNewsData = new DataHelper();
        String testCategory = testNewsData.makeTestNewsCategory();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testDescr = testNewsData.makeTestNewsDescr();
        String testDate = testNewsData.makeTestNewsDate(0);
        String testDateMinus1 = testNewsData.makeTestNewsDate(-1);
        String testDatePlus1 = testNewsData.makeTestNewsDate(1);

        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsPublDateFromClip(testDate, myContext);
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.clickFilterNewsBtn();
        controlPanelFilterPage.insertFilterPeriodStartFromClip(testDateMinus1, myContext);
        controlPanelFilterPage.insertFilterPeriodEndFromClip(testDatePlus1, myContext);
        controlPanelFilterPage.clickFilterBtn();

        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle);
    }

    @Test
    @Flaky
    @Feature("Filters: publication date")
    @DisplayName("Check that date filter DOES NOT show news outside specified dates")
    public void shdNotDisplNewsOutsideChosenDates() {

        newsControlPanelPage.clickAddNewsBtn();

        DataHelper testNewsData = new DataHelper();
        String testCategory = testNewsData.makeTestNewsCategory();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testDescr = testNewsData.makeTestNewsDescr();
        String testDateOut = testNewsData.makeTestNewsDate(-3);

        String testDateMinus1 = testNewsData.makeTestNewsDate(-1);
        String testDatePlus1 = testNewsData.makeTestNewsDate(1);

        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsPublDateFromClip(testDateOut, myContext);
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.clickFilterNewsBtn();
        controlPanelFilterPage.insertFilterPeriodStartFromClip(testDateMinus1, myContext);
        controlPanelFilterPage.insertFilterPeriodEndFromClip(testDatePlus1, myContext);
        controlPanelFilterPage.clickFilterBtn();

        onView(withId(newsControlPanelPage.getAllNewsCardsBlockList()))
                .check(matches(not(hasDescendant(withText(testTitle)))));
    }

    @Test
    @Feature("Filters: publication date")
    @DisplayName("Check that date filter DOES show news when period bounds overlap")
    public void shdDisplayNewsWhenSameBounds() {

        newsControlPanelPage.clickAddNewsBtn();

        DataHelper testNewsData = new DataHelper();
        String testCategory = testNewsData.makeTestNewsCategory();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testDescr = testNewsData.makeTestNewsDescr();
        String testDate = testNewsData.makeTestNewsDate(0);
        String testTime = testNewsData.makeTestNewsTime(-1);

        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeFromClip(testTime, myContext);
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.clickFilterNewsBtn();
        // начало и конец периода - один день: когда опубликована новость
//        controlPanelFilterPage.insertFilterPeriodStartFromClip(testDate, myContext);
//        controlPanelFilterPage.insertFilterPeriodEndFromClip(testDate, myContext);
        controlPanelFilterPage.insertFilterPeriodStartWidgetNoOffset();
        controlPanelFilterPage.insertFilterPeriodEndWidgetNoOffset();
        controlPanelFilterPage.clickFilterBtn();

        onView(withId(newsControlPanelPage.getAllNewsCardsBlockList()))
                .check(matches(hasDescendant(withText(testTitle))));
    }

    @Test
    @Feature("Filters: publication date")
    @DisplayName("Check that it is possible to specify one bound of a period (start)")
    // ЭТОТ ТЕСТ ДОЛЖЕН ПАДАТЬ, Т.К. ПОЧЕМУ-ТО ЛИШЬ ОДНУ ГРАНИЦУ ФИЛЬТРАЦИИ УКАЗАТЬ НЕЛЬЗЯ
    public void shdAllowToSpecifyOneBoundStart() {

        DataHelper testNewsData = new DataHelper();
        String testDate = testNewsData.makeTestNewsDate(0);

        newsControlPanelPage.clickFilterNewsBtn();
        controlPanelFilterPage.insertFilterPeriodStartFromClip(testDate, myContext);
        controlPanelFilterPage.clickFilterBtn();

        onView(withText(controlPanelFilterPage.getWrongPeriodToastText()))
                .inRoot(withDecorView(not(controlPanelFilterPageDecorView)))
                .check(matches(not(isDisplayed())));
    }

    @Test
    @Feature("Filters: publication date")
    @DisplayName("Check that it is possible to specify one bound of a period (end)")
    // ЭТОТ ТЕСТ ДОЛЖЕН ПАДАТЬ, Т.К. ПОЧЕМУ-ТО ЛИШЬ ОДНУ ГРАНИЦУ ФИЛЬТРАЦИИ УКАЗАТЬ НЕЛЬЗЯ
    public void shdAllowToSpecifyOneBoundEnd() {

        DataHelper testNewsData = new DataHelper();
        String testDate = testNewsData.makeTestNewsDate(0);

        newsControlPanelPage.clickFilterNewsBtn();
        controlPanelFilterPage.insertFilterPeriodEndFromClip(testDate, myContext);
        controlPanelFilterPage.clickFilterBtn();

        onView(withText(controlPanelFilterPage.getWrongPeriodToastText()))
                .inRoot(withDecorView(not(controlPanelFilterPageDecorView)))
                .check(matches(not(isDisplayed())));
    }

    @Test
    @Feature("Filters: publication date")
    @DisplayName("Check that date filter does NOT allow to specify end date BEFORE start date")
    // ТЕСТ ДОЛЖЕН ПАДАТЬ, Т.К. ПОЧЕМУ, СОБСТВЕННО, ПРИЛОЖЕНИЕ ЭТО ДОПУСКАЕТ?
    public void shdNotAllowToPlaceEndBeforeStart() {

        DataHelper testNewsData = new DataHelper();
        String testDateMinus1 = testNewsData.makeTestNewsDate(-1);
        String testDatePlus1 = testNewsData.makeTestNewsDate(1);

        newsControlPanelPage.clickFilterNewsBtn();
        // конец указывается ДО начала
        controlPanelFilterPage.insertFilterPeriodStartFromClip(testDatePlus1, myContext);
        controlPanelFilterPage.insertFilterPeriodEndFromClip(testDateMinus1, myContext);
        controlPanelFilterPage.clickFilterBtn();

        onView(withText(controlPanelFilterPage.getWrongPeriodToastText()))
                .inRoot(withDecorView(not(controlPanelFilterPageDecorView)))
                .check(matches(isDisplayed())); // фактически этого уведомления нет
    }

    @Test
    @Feature("Filters: activity")
    @DisplayName("Check if activity filters are applied when both states are selected")
    public void shdDisplAllStatesWithBothActFilters() {
        // создаём активную новость
        newsControlPanelPage.clickAddNewsBtn();
        DataHelper testNewsData1 = new DataHelper();
        String testCategory = "Массаж";
        String testTitle1 = testNewsData1.makeTestNewsTitle();
        String testDescr1 = testNewsData1.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle1);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsDescription(testDescr1);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.clickAddNewsBtn();
        // создаём вторую новость

        DataHelper testNewsData2 = new DataHelper();
        String testTitle2 = testNewsData2.makeTestNewsTitle();
        String testDescr2 = testNewsData2.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle2);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsDescription(testDescr2);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
        // переводим вторую новость в неактивные
        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle2);
        editingNewsPage.changeNewsActivitySwitcherState();
        editingNewsPage.saveChangesToNewsItem();

        newsControlPanelPage.checkNewsControlPageVisible(1000);
        newsControlPanelPage.clickFilterNewsBtn();
        controlPanelFilterPage.insertNewsCatgrWithDDList(testCategory);
        // галки не снимаем
        controlPanelFilterPage.clickFilterBtn();

        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle1);
        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle2);
    }

    @Test
    @Feature("Filters: activity")
    @DisplayName("Check if activity filters are applied when one state (active) is selected")
    public void shdDisplActStateWithActFilterOnly() {
        // создаём активную новость
        newsControlPanelPage.clickAddNewsBtn();
        DataHelper testNewsData1 = new DataHelper();
        String testCategory = "Профсоюз";
        String testTitle1 = testNewsData1.makeTestNewsTitle();
        String testDescr1 = testNewsData1.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle1);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsDescription(testDescr1);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.clickAddNewsBtn();
        // создаём вторую новость

        DataHelper testNewsData2 = new DataHelper();
        String testTitle2 = testNewsData2.makeTestNewsTitle();
        String testDescr2 = testNewsData2.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle2);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsDescription(testDescr2);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
        // переводим вторую новость в неактивные
        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle2);
        editingNewsPage.changeNewsActivitySwitcherState();
        editingNewsPage.saveChangesToNewsItem();

        newsControlPanelPage.checkNewsControlPageVisible(1000);
        newsControlPanelPage.clickFilterNewsBtn();
        controlPanelFilterPage.insertNewsCatgrWithDDList(testCategory);
        controlPanelFilterPage.toggleInactiveCheckbox();
        controlPanelFilterPage.clickFilterBtn();

        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle1);
        newsControlPanelPage.checkIfNewsListHasTitle(false, testTitle2);
    }

    @Test
    @Feature("Filters: activity")
    @DisplayName("Check if activity filters are applied when one state (inactive) is selected")
    public void shdDisplActStateWithInactFilterOnly() {
        // создаём активную новость
        newsControlPanelPage.clickAddNewsBtn();
        DataHelper testNewsData1 = new DataHelper();
        String testCategory = "Благодарность";
        String testTitle1 = testNewsData1.makeTestNewsTitle();
        String testDescr1 = testNewsData1.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle1);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsDescription(testDescr1);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.clickAddNewsBtn();
        // создаём вторую новость

        DataHelper testNewsData2 = new DataHelper();
        String testTitle2 = testNewsData2.makeTestNewsTitle();
        String testDescr2 = testNewsData2.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle2);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsDescription(testDescr2);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
        // переводим вторую новость в неактивные
        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle2);
        editingNewsPage.changeNewsActivitySwitcherState();
        editingNewsPage.saveChangesToNewsItem();

        newsControlPanelPage.checkNewsControlPageVisible(1000);
        newsControlPanelPage.clickFilterNewsBtn();
        controlPanelFilterPage.insertNewsCatgrWithDDList(testCategory);
        controlPanelFilterPage.toggleActiveCheckbox();
        controlPanelFilterPage.clickFilterBtn();

        newsControlPanelPage.checkIfNewsListHasTitle(false, testTitle1);
        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle2);
    }

    @Test
    @Feature("Filters: activity")
    @DisplayName("Check if activity filters are applied when both options unchecked")
    public void shdDisplAllStatesWithActFiltersOff() {
        // создаём активную новость
        newsControlPanelPage.clickAddNewsBtn();
        DataHelper testNewsData1 = new DataHelper();
        String testCategory = "Благодарность";
        String testTitle1 = testNewsData1.makeTestNewsTitle();
        String testDescr1 = testNewsData1.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle1);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsDescription(testDescr1);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.clickAddNewsBtn();
        // создаём вторую новость
        DataHelper testNewsData2 = new DataHelper();
        String testTitle2 = testNewsData2.makeTestNewsTitle();
        String testDescr2 = testNewsData2.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle2);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsDescription(testDescr2);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
        // переводим вторую новость в неактивные
        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle2);
        editingNewsPage.changeNewsActivitySwitcherState();
        editingNewsPage.saveChangesToNewsItem();

        newsControlPanelPage.checkNewsControlPageVisible(1000);
        newsControlPanelPage.clickFilterNewsBtn();
        controlPanelFilterPage.insertNewsCatgrWithDDList(testCategory);
        controlPanelFilterPage.toggleActiveCheckbox();
        controlPanelFilterPage.toggleInactiveCheckbox();
        controlPanelFilterPage.clickFilterBtn();

        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle1);
        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle2);
    }

    @Test
    @Feature("Filters: cancellation")
    @DisplayName("Check that no filters applied when CANCEL button is clicked on FILTER NEWS page")
    public void shdNotApplyFiltersWhenCancelBtnClick() {

        newsControlPanelPage.clickAddNewsBtn();
        // новость выбираемой категории
        DataHelper testNewsData1 = new DataHelper();
        String testTitle1 = testNewsData1.makeTestNewsTitle();
        String testCatgr1 = "Зарплата";
        String testDescr1 = testNewsData1.makeTestNewsDescr();

        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr1);
        creatingNewsPage.insertNewsTitle(testTitle1);
        creatingNewsPage.insertNewsDescription(testDescr1);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.clickAddNewsBtn();
        // новость другой категории (по её видимости определяется применение фильтра)
        DataHelper testNewsData2 = new DataHelper();
        String testTitle2 = testNewsData2.makeTestNewsTitle();
        String testCatgr2 = "Нужна помощь";
        String testDescr2 = testNewsData2.makeTestNewsDescr();

        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr2);
        creatingNewsPage.insertNewsTitle(testTitle2);
        creatingNewsPage.insertNewsDescription(testDescr2);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.clickFilterNewsBtn();
        controlPanelFilterPage.insertNewsCatgryWithKeybd(testCatgr1);
        controlPanelFilterPage.clickCancelBtn();
        newsControlPanelPage.checkNewsControlPageVisible(1000);

        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle1);
        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle2);
    }

    @Test
    @Feature("News deletion")
    @DisplayName("Check that deletion choice popup opens when DELETE NEWS ITEM button is clicked on card")
    public void shdOpenPopupIfDelBtnClick() {

        newsControlPanelPage.clickAddNewsBtn();
        DataHelper testNewsData1 = new DataHelper();
        String testTitle1 = testNewsData1.makeTestNewsTitle();
        String testCatgr1 = testNewsData1.makeTestNewsCategory();
        String testDescr1 = testNewsData1.makeTestNewsDescr();

        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr1);
        creatingNewsPage.insertNewsTitle(testTitle1);
        creatingNewsPage.insertNewsDescription(testDescr1);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.clickDeleteOnNewsItemWithMatchingTitle(testTitle1);
        onView(withSubstring(newsControlPanelPage.getConfDeletePopupText()))
                .inRoot(withDecorView(Matchers.not(newsControlPanelPageDecorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Feature("News deletion")
    @DisplayName("Check that news item is not deleted when CANCEL is chosen in delete popup")
    public void shdNotDelNewsAfterDeletionCancel() {

        newsControlPanelPage.clickAddNewsBtn();
        DataHelper testNewsData1 = new DataHelper();
        String testTitle1 = testNewsData1.makeTestNewsTitle();
        String testCatgr1 = testNewsData1.makeTestNewsCategory();
        String testDescr1 = testNewsData1.makeTestNewsDescr();

        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr1);
        creatingNewsPage.insertNewsTitle(testTitle1);
        creatingNewsPage.insertNewsDescription(testDescr1);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.clickDeleteOnNewsItemWithMatchingTitle(testTitle1);
        newsControlPanelPage.cancelNewsItemDeletion();
        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle1);

    }

    @Test
    @Feature("News deletion")
    @DisplayName("Check that news item is deleted when OK is chosen in delete popup")
    public void shdDelNewsAfterDeletionConfirm() {

        newsControlPanelPage.clickAddNewsBtn();
        DataHelper testNewsData1 = new DataHelper();
        String testTitle1 = testNewsData1.makeTestNewsTitle();
        String testCatgr1 = testNewsData1.makeTestNewsCategory();
        String testDescr1 = testNewsData1.makeTestNewsDescr();

        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr1);
        creatingNewsPage.insertNewsTitle(testTitle1);
        creatingNewsPage.insertNewsDescription(testDescr1);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.clickDeleteOnNewsItemWithMatchingTitle(testTitle1);
        newsControlPanelPage.confirmNewsItemDeletion();
        newsControlPanelPage.checkIfNewsListHasTitle(false, testTitle1);
    }

    @Test
    @Feature("News editing")
    @DisplayName("Check that EDITING NEWS page opens when EDIT NEWS button is clicked on a card")
    public void shdOpenEditNewsPageOnCardBtnClick() {

        newsControlPanelPage.clickEditNewsBtnForCardAtPos(0);
        editingNewsPage.checkPageVisibleByHeaderTitle();
    }

    @Test
    @Feature("News editing")
    @DisplayName("Check that news category changes to new one after edit")
    public void shdApplyNewCatgryAfterEdit() {

        newsControlPanelPage.clickAddNewsBtn();
        // создание новости
        DataHelper testNewsData = new DataHelper();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testCatgr0 = "Массаж";
        String testDescr = testNewsData.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr0);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
        // редактирование новости
        String testCatgr1 = "Благодарность";
        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle);
        editingNewsPage.changeNewsCatgryWithKeybd(testCatgr1);
        editingNewsPage.saveChangesToNewsItem();
        // а поскольку прописать буковами категорию в карточке сложна,
        // воспользуемся фильтром: спрва смотрим старую категорию
        newsControlPanelPage.clickFilterNewsBtn();
        controlPanelFilterPage.insertNewsCatgrWithDDList(testCatgr0);
        controlPanelFilterPage.clickFilterBtn();
        // проверяем, что в старой категории новость не отображается
        newsControlPanelPage.checkIfNewsListHasTitle(false, testTitle);
        // теперь проверяем в новой категории
        newsControlPanelPage.clickFilterNewsBtn();
        controlPanelFilterPage.insertNewsCatgrWithDDList(testCatgr1);
        controlPanelFilterPage.clickFilterBtn();
        newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle);

        // Кто-то мог бы сказать, что тестер еретик и слепил две проверки в одну.
        // А тестер вам скажет, что еретик на руководителе проекта, не почесавшийся
        // на тему тестопригодности категории в карточках. ID и буковы в студию.
    }

    @Test
    @Feature("News editing")
    @DisplayName("Check that news title changes to new one after edit")
    public void shdApplyNewTitleAfterEdit() {

        newsControlPanelPage.clickAddNewsBtn();
        // создание новости
        DataHelper testNewsData = new DataHelper();
        String testTitle0 = testNewsData.makeTestNewsTitle();
        String testCatgr = testNewsData.makeTestNewsCategory();
        String testDescr = testNewsData.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle0);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
        // редактирование новости
        String testTitle1 = testNewsData.makeTestNewsTitle();
        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle0);
        editingNewsPage.changeNewsTitleWithKeybd(testTitle1);
        editingNewsPage.saveChangesToNewsItem();

        int cardPos = newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle1);
        newsControlPanelPage.scrollToCardAtPos(cardPos);
        onView(withId(newsControlPanelPage.getAllNewsCardsBlockList()))
                .check(matches(RecyclerCustomMatcher
                        .atPosition(cardPos, allOf(hasDescendant(withSubstring(testTitle1)),
                                hasDescendant(withSubstring(testDescr))))));
        // логика тут в том, что после изменения заголовка новость остаётся на той же
        // позиции, т.к. карточки в списке упорядочены по дате публикации
    }

    @Test
    @Feature("News editing")
    @DisplayName("Check that news description changes to new one after edit")
    public void shdApplyNewDescrAfterEdit() {

        newsControlPanelPage.clickAddNewsBtn();
        // создание новости
        DataHelper testNewsData = new DataHelper();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testCatgr = testNewsData.makeTestNewsCategory();
        String testDescr0 = testNewsData.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr0);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
        // редактирование новости
        String testDescr1 = testNewsData.makeTestNewsDescr();
        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle);
        editingNewsPage.changeNewsDescrWithKeybd(testDescr1);
        editingNewsPage.saveChangesToNewsItem();

        int cardPos = newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle);
        newsControlPanelPage.toggleNewsCardAtPosWithBtn(cardPos);
        onView(withId(newsControlPanelPage.getAllNewsCardsBlockList()))
                .check(matches(RecyclerCustomMatcher
                        .atPosition(cardPos, hasDescendant(withSubstring(testDescr1)))));
        // после изменения описания новость опять же остаётся на той же
        // позиции, т.к. карточки здесь упорядочены по дате публикации
    }

    @Test
    @Feature("News editing")
    @DisplayName("Check that news publication date changes to new one after edit")
    // этот тест максимально топорный, но в окне редактирования слишком мемные longClick
    public void shdApplyNewPublDateAfterEdit() {

        newsControlPanelPage.clickAddNewsBtn();
        // создание новости
        DataHelper testNewsData = new DataHelper();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testCatgr = testNewsData.makeTestNewsCategory();
        String testDescr0 = testNewsData.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr0);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
        // редактирование новости
        String testPublDate = testNewsData.makeTestNewsDate(0);
        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle);
        editingNewsPage.changeNewsPublDateWithWidget();
        editingNewsPage.saveChangesToNewsItem();

        int cardPos = newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle);
        newsControlPanelPage.scrollToCardAtPos(cardPos);
        onView(withId(newsControlPanelPage.getAllNewsCardsBlockList()))
                .check(matches(RecyclerCustomMatcher
                        .atPosition(cardPos, not(hasDescendant(withSubstring(testPublDate))))));
        // проверяем, что у карточки с нужным названием другая дата публикации
    }

    @Test
    @Feature("News editing")
    @DisplayName("Check that news publication time changes to new one after edit")
    public void shdApplyNewPublTimeAfterEdit() {

        newsControlPanelPage.clickAddNewsBtn();
        // создание новости
        DataHelper testNewsData = new DataHelper();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testCatgr = testNewsData.makeTestNewsCategory();
        String testDescr = testNewsData.makeTestNewsDescr();
        String testPublTime = testNewsData.makeTestNewsTime(0);

        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        DataHelper.WidgetDateTime timeForEdit = new DataHelper.WidgetDateTime(0, 410);
        String newHours = timeForEdit.getHoursForWidget();
        String newMinutes = timeForEdit.getMinsForWidget();
        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle);
        editingNewsPage.changeNewsPublTimeWithWidget(newHours, newMinutes);
        editingNewsPage.saveChangesToNewsItem();
        newsControlPanelPage.checkAllNewsRecycListVisible();
        // т.к. время публикации в карточке не отображается, открываем страницу редактирования
        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle);
        onView(withId(editingNewsPage.getPublTimeInputField()))
                .check(matches(not(withText(testPublTime))));
    }

    @Test
    @Feature("News editing")
    @DisplayName("Check that news activity state changes to new one (inactive) after edit")
    public void shdChangeFromActToInactAfterEdit() {

        newsControlPanelPage.clickAddNewsBtn();
        // создание новости
        DataHelper testNewsData = new DataHelper();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testCatgr = testNewsData.makeTestNewsCategory();
        String testDescr = testNewsData.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle);
        editingNewsPage.changeNewsActivitySwitcherState();
        editingNewsPage.saveChangesToNewsItem();
        newsControlPanelPage.checkAllNewsRecycListVisible();

        int cardPos = newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle);
        newsControlPanelPage.scrollToCardAtPos(cardPos);
        onView(withId(newsControlPanelPage.getAllNewsCardsBlockList()))
                .check(matches(RecyclerCustomMatcher
                        .atPosition(cardPos, hasDescendant(withText(newsControlPanelPage.getNewsItemInactiveText())))));
    }

    @Test
    @Feature("News editing")
    @DisplayName("Check that news activity state changes to new one (inactive) after edit")
    public void shdChangeFromInactToActAfterEdit() {

        newsControlPanelPage.clickAddNewsBtn();
        // создание новости
        DataHelper testNewsData = new DataHelper();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testCatgr = testNewsData.makeTestNewsCategory();
        String testDescr = testNewsData.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
        // переводим новость в неактивные
        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle);
        editingNewsPage.changeNewsActivitySwitcherState();
        editingNewsPage.saveChangesToNewsItem();
        newsControlPanelPage.checkAllNewsRecycListVisible();
        // переводим новость в активные
        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle);
        editingNewsPage.changeNewsActivitySwitcherState();
        editingNewsPage.saveChangesToNewsItem();

        int cardPos = newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle);
        newsControlPanelPage.scrollToCardAtPos(cardPos);
        onView(withId(newsControlPanelPage.getAllNewsCardsBlockList()))
                .check(matches(RecyclerCustomMatcher
                        .atPosition(cardPos, hasDescendant(withText(newsControlPanelPage.getNewsItemActiveText())))));
    }

    @Test
    @Feature("News editing")
    @DisplayName("Check that invalid category is not applied when editing news")
    public void shdNotApplyInvaldCatgryWheEditNews() {
        String wrongCategory = "A category is fine too";
        newsControlPanelPage.checkNewsRecyclerHasAtLeastPos(1);
        newsControlPanelPage.clickEditNewsBtnForCardAtPos(0);
        editingNewsPage.changeNewsCatgryWithKeybd(wrongCategory);
        editingNewsPage.saveChangesToNewsItem();
        onView(withSubstring(editingNewsPage.getSaveFailText()))
                .inRoot(withDecorView(Matchers.not(editingNewsPageDecorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Feature("News creation")
    @DisplayName("Check that news item has the title it was created with")
    public void shdCreateNewsWithCorrectTitle() {

        newsControlPanelPage.clickAddNewsBtn();
        // создание новости
        DataHelper testNewsData = new DataHelper();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testCatgr = testNewsData.makeTestNewsCategory();
        String testDescr = testNewsData.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        int cardPos = newsControlPanelPage.checkIfNewsListHasDescr(true, testDescr);
        newsControlPanelPage.scrollToCardAtPos(cardPos);
        onView(withId(newsControlPanelPage.getAllNewsCardsBlockList()))
                .check(matches(RecyclerCustomMatcher
                        .atPosition(cardPos, hasDescendant(withSubstring(testTitle)))));

    }

    @Test
    @Feature("News creation")
    @DisplayName("Check that news item has the description it was created with")
    public void shdCreateNewsWithCorrectDescr() {

        newsControlPanelPage.clickAddNewsBtn();
        // создание новости
        DataHelper testNewsData = new DataHelper();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testCatgr = testNewsData.makeTestNewsCategory();
        String testDescr = testNewsData.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.checkAllNewsRecycListVisible();
        int cardPos = newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle);
        newsControlPanelPage.toggleNewsCardAtPosWithBtn(cardPos);
        onView(withId(newsControlPanelPage.getAllNewsCardsBlockList()))
                .check(matches(RecyclerCustomMatcher
                        .atPosition(cardPos, hasDescendant(withSubstring(testDescr)))));
    }

    @Test
    @Feature("News creation")
    @DisplayName("Check that news item has the category it was created with")
    public void shdCreateNewsWithCorrectCatgry() {

        newsControlPanelPage.clickAddNewsBtn();
        // создание новости
        DataHelper testNewsData = new DataHelper();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testCatgr = testNewsData.makeTestNewsCategory();
        String testDescr = testNewsData.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();
        // применение категории
        newsControlPanelPage.clickFilterNewsBtn();
        controlPanelFilterPage.insertNewsCatgrWithDDList(testCatgr);
        controlPanelFilterPage.clickFilterBtn();

        int cardPos = newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle);
        newsControlPanelPage.scrollToCardAtPos(cardPos);
        onView(withId(newsControlPanelPage.getAllNewsCardsBlockList()))
                .check(matches(RecyclerCustomMatcher
                        .atPosition(cardPos, hasDescendant(withSubstring(testTitle)))));

    }

    @Test
    @Feature("News creation")
    @DisplayName("Check that news item has the publication date it was created with")
    public void shdCreateNewsWithCorrectPublDate() {

        newsControlPanelPage.clickAddNewsBtn();
        // создание новости
        DataHelper testNewsData = new DataHelper();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testCatgr = testNewsData.makeTestNewsCategory();
        String testDescr = testNewsData.makeTestNewsDescr();
        String testPublDate = testNewsData.makeTestNewsDate(0);

        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        int cardPos = newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle);
        newsControlPanelPage.scrollToCardAtPos(cardPos);
        onView(withId(newsControlPanelPage.getAllNewsCardsBlockList()))
                .check(matches(RecyclerCustomMatcher.atPosition(cardPos, hasDescendant(withText(testTitle)))));

    }

    @Test
    @Feature("News creation")
    @DisplayName("Check that news item has the publication time it was created with")
    public void shdCreateNewsWithCorrectPublTime() {

        newsControlPanelPage.clickAddNewsBtn();
        // создание новости
        DataHelper testNewsData = new DataHelper();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testCatgr = testNewsData.makeTestNewsCategory();
        String testDescr = testNewsData.makeTestNewsDescr();
        String testPublTime = testNewsData.makeTestNewsTime(13);

        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeFromClip(testPublTime, myContext);
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.checkAllNewsRecycListVisible();
        newsControlPanelPage.clickEditOnNewsItemWithMatchingTitle(testTitle);

        onView(withId(editingNewsPage.getPublTimeInputField()))
                .check(matches(withText(testPublTime)));
    }

    @Test
    @Feature("News creation")
    @DisplayName("Check that news item displays correct creation date in card")
    // ЭТОТ ТЕСТ ДОЛЖЕН ПАДАТЬ, Т.К. ДАТА СОЗДАНИЯ ОТОБРАЖАЕТСЯ НЕКОРРЕКТНО
    public void shdSpecifyCorrectCreationDateForItem() {

        newsControlPanelPage.clickAddNewsBtn();
        // создание новости
        DataHelper testNewsData = new DataHelper();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testCatgr = testNewsData.makeTestNewsCategory();
        String testDescr = testNewsData.makeTestNewsDescr();
        String testCreateDate = testNewsData.makeTestNewsDate(0);

        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        newsControlPanelPage.checkAllNewsRecycListVisible();
        int cardPos = newsControlPanelPage.checkIfNewsListHasTitle(true, testTitle);
        newsControlPanelPage.scrollToCardAtPos(cardPos);
        onView(withId(newsControlPanelPage.getAllNewsCardsBlockList()))
                .check(matches(RecyclerCustomMatcher.atPosition(cardPos,
                        hasDescendant(allOf(withSubstring(testCreateDate),
                                withId(newsControlPanelPage.getNewsItemCreationDate()))))));
    }

    @Test
    @Feature("News creation")
    @DisplayName("Check if a popup is shown when SAVE is pressed with null input in any field")
    public void shdNotAllowToCreateNewsWithNoInput() {

        newsControlPanelPage.clickAddNewsBtn();
        creatingNewsPage.pressSaveToCreateNews();
        onView(withSubstring(creatingNewsPage.getEmptyFieldsPopupText()))
                .inRoot(withDecorView(Matchers.not(creatingNewsDecorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Feature("News creation")
    @DisplayName("Check that news item is not added to page when news creation is cancelled")
    public void shdNotCreateNewsWhenCancel() {

        newsControlPanelPage.clickAddNewsBtn();
        DataHelper testNewsData = new DataHelper();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testCatgr = testNewsData.makeTestNewsCategory();
        String testDescr = testNewsData.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressCancelToExitCreatingNews(); // отмена создания

        newsControlPanelPage.checkIfNewsListHasTitle(false, testTitle);
    }

    @Test
    @Feature("News creation")
    @DisplayName("Check that input error popup is shown when invalid category is specified")
    public void shdDisplErrorPopupWhenInvalidCatgry() {

        newsControlPanelPage.clickAddNewsBtn();
        DataHelper testNewsData = new DataHelper();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testCatgr = "Новость категории B";
        String testDescr = testNewsData.makeTestNewsDescr();

        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsCatgrWithKeybd(testCatgr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        onView(withSubstring(creatingNewsPage.getSaveFailText()))
                .inRoot(withDecorView(Matchers.not(creatingNewsDecorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Feature("News creation")
    @DisplayName("Check if CREATING NEWS window allows to set news item to NOT ACTIVE on creation")
    // ЗАЧЕМ ТУТ ЭТОТ ПЕРЕКЛЮЧАТЕЛЬ, ЕСЛИ ОН НИЧЕГО НЕ ДЕЛАЕТ? Д - ДИЗАЙН. ПОЭТОМУ ТЕСТ ПАДАЕТ
    public void shdAllowToCreateInactiveNewsWithSwitcher() {

        newsControlPanelPage.clickAddNewsBtn();
        creatingNewsPage.toggleNewsActivityWithSwitcher();
        // ожидается, что переклюлючатель будет кликабелен и поменяет текст при нажатии
        onView(withId(creatingNewsPage.getStateSwitcher()))
                .check(matches(withText(creatingNewsPage.getNotActiveSwitcherText())));
    }
}
