package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import android.content.Context;
import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import TestData.DataHelper;
import TestPageObjects.AboutPage;
import TestPageObjects.AuthorizationPage;
import TestPageObjects.CreatingNewsPage;
import TestPageObjects.CustomAppBar;
import TestPageObjects.EditingNewsPage;
import TestPageObjects.MainPage;
import TestPageObjects.NewsControlPanelPage;
import TestPageObjects.NewsFilterPage;
import TestPageObjects.NewsPage;
import TestUtils.RecyclerCustomMatcher;
import io.qameta.allure.android.rules.ScreenshotRule;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.junit4.DisplayName;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class Test_NewsPage {

    @Rule
    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule(ScreenshotRule.Mode.FAILURE,
            String.valueOf(System.currentTimeMillis()));

    View newsFilterPageDecorView;
    MainPage mainPage = new MainPage();
    NewsPage newsPage = new NewsPage();
    AboutPage aboutPage = new AboutPage();
    NewsControlPanelPage newsControlPanelPage = new NewsControlPanelPage();
    CreatingNewsPage creatingNewsPage = new CreatingNewsPage();
    EditingNewsPage editingNewsPage = new EditingNewsPage();
    NewsFilterPage newsFilterPage = new NewsFilterPage();
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
            customAppBar.goToNewsPageWithMenuBtn();
            newsPage.checkNewsPageVisible();
        } catch (Exception notOnNewsPage) {
            System.out.println(notOnNewsPage.getMessage());
            customAppBar.goToNewsPageWithMenuBtn();
        }

        myContext = InstrumentationRegistry.getInstrumentation().getContext();
        // треубуется для проверок, взаимодействующих с текстом в буфере
    }

    @Test
    @DisplayName("Check NEWS recycler visibility on NEWS page")
    public void shdDisplayNewsRecycler() {

        newsPage.checkNewsPageVisible();
    }

    @Test
    @DisplayName("Check that news item (added with widgets) is displayed on page if published & active")
    public void shdDisplayNewsOnPageIfActiveAndPublished() {

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
        customAppBar.goToNewsPageWithMenuBtn();
        onView(withId(newsPage.getAllNewsRecycList()))
                .check(matches(hasDescendant(withText(testTitle))));
    }

    @Test
    @DisplayName("Check that news item (added with widgets) is NOT displayed on page if published, but set to INACTIVE")
    public void shdNotDisplNewsIfPublButInactive() {

        newsPage.checkNewsPageVisible();
        newsPage.clickEditNewsBtn();
        newsControlPanelPage.checkNewsControlPageVisible(1000);
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
        customAppBar.goToNewsPageWithMenuBtn();
        onView(withId(newsControlPanelPage.getAllNewsCardsBlockList()))
                .check(matches(not(hasDescendant(withText(testTitle)))));
    }

    @Test
    @DisplayName("Check that news item (added with clipboard) is NOT displayed on page if set to ACTIVE, but not yet published")
    public void shdNotDisplNewsIfActiveButNotPublished() {

        newsPage.checkNewsPageVisible();
        newsPage.clickEditNewsBtn();
        newsControlPanelPage.checkNewsControlPageVisible(1000);
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

        onView(withId(newsControlPanelPage.getAllNewsCardsBlockList()))
                .check(matches(hasDescendant(withText(testTitle))));

        customAppBar.goToNewsPageWithMenuBtn();
        onView(withId(newsControlPanelPage.getAllNewsCardsBlockList()))
                .check(matches(not(hasDescendant(withText(testTitle)))));
    }

    @Test
    @DisplayName("Check if news card expands on ( ˅ / ˄ ) button press (from default state)")
    public void shdExpandNewsItemCardWithBtnWhenNormal() {
        int cardPos = 0;
        newsPage.checkNewsPageVisible();
        newsPage.checkNewsRecyclerHasAtLeastPos(1);
        newsPage.toggleNewsCardStateWithBtnOnPos(cardPos);
        onView(RecyclerCustomMatcher.withIndex(withId(newsPage.getNewsItemCard()), cardPos))
                .check(matches(hasDescendant(allOf(withId(newsPage.getNewsCardDescr()), isDisplayed()))));
    }

    @Test
    @DisplayName("Check if news card closes on ( ˅ / ˄ ) button press (from opened state)")
    public void shdCloseNewsItemCardWithBtnWhenExpanded() {
        int cardPos = 0;
        newsPage.checkNewsPageVisible();
        newsPage.checkNewsRecyclerHasAtLeastPos(1);
        newsPage.toggleNewsCardStateWithBtnOnPos(cardPos);
        onView(RecyclerCustomMatcher.withIndex(withId(newsPage.getNewsItemCard()), cardPos))
                .check(matches(hasDescendant(allOf(withId(newsPage.getNewsCardDescr()), isDisplayed()))));

        newsPage.toggleNewsCardStateWithBtnOnPos(cardPos);
        onView(RecyclerCustomMatcher.withIndex(withId(newsPage.getNewsItemCard()), cardPos))
                .check(matches(hasDescendant(allOf(withId(newsPage.getNewsCardDescr()), not(isDisplayed())))));
    }

    @Test
    @DisplayName("Check if news card order is reversed after SORT NEWS LIST button is pressed")
    public void shdReverseNewsItemOrderWhenSortBtnClick() {

        newsPage.checkNewsPageVisible();
        newsPage.checkNewsRecyclerHasAtLeastPos(2);
        // похлопаем стабильности приложения и шытпостингу посреди прогонов
        HashMap<Integer, String> cardMap0 = newsPage.createHashMapForCardsInList();
        newsPage.toggleNewsCardOrderWithSortBtn();
        boolean isListReversed = newsPage.checkIfCardListOrderIsReversed(cardMap0);
        Assert.assertTrue("Check if [isListReversed] yields TRUE", isListReversed == true);
    }

    @Test
    @DisplayName("Check if news card order is reversed to normal after SORT NEWS LIST button is pressed, when reversed previously")
    public void shdReverseNewsItemOrderToNormalWithDoubleSortBtnClick() {
        newsPage.checkNewsPageVisible();
        newsPage.checkNewsRecyclerHasAtLeastPos(2);
        HashMap<Integer, String> cardMap0 = newsPage.createHashMapForCardsInList();
        newsPage.toggleNewsCardOrderWithSortBtn();
        boolean isListReversed = newsPage.checkIfCardListOrderIsReversed(cardMap0);
        Assert.assertTrue("Check if [isListReversed] yields TRUE", isListReversed == true);

        HashMap<Integer, String> cardMap1 = newsPage.createHashMapForCardsInList();
        newsPage.toggleNewsCardOrderWithSortBtn();
        boolean isReversedAgain = newsPage.checkIfCardListOrderIsReversed(cardMap1);
        Assert.assertTrue("Check if [isReversedAgain] yields TRUE", isReversedAgain == true);
    }

    @Test
    @DisplayName("Check if filter page opens after FILTER NEWS button is clicked")
    public void shdOpenFilterPageWithHeaderBtn() {
        newsPage.checkNewsPageVisible();
        newsPage.clickFilterBtnToOpenFilterPage();
        newsFilterPage.checkPageVisibleByTitle();
    }

    @Test
    @DisplayName("Check that category filter shows news of selected category on NEWS page")
    public void shdDisplayNewsOfChosenCategory() {

        newsPage.checkNewsPageVisible();
        newsPage.clickEditNewsBtn();
        newsControlPanelPage.clickAddNewsBtn();

        DataHelper testNewsMassage = new DataHelper();
        String testCategory = "Массаж";
        String testTitle = testNewsMassage.makeTestNewsTitle();
        String testDescr = testNewsMassage.makeTestNewsDescr();

        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsPublDateByWidgetNoOffset();
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        customAppBar.goToNewsPageWithMenuBtn();
        newsPage.clickFilterBtnToOpenFilterPage();
        newsFilterPage.insertNewsCatgryWithKeybd(testCategory);
        newsFilterPage.clickFilterBtn();
        onView(withId(newsPage.getAllNewsRecycList()))
                .check(matches(hasDescendant(withText(testTitle))));
    }

    @Test
    @DisplayName("Check that category filter does NOT show news of other categories on NEWS page")
    public void shdNotDisplayNewsOfOtherCategories() {
        newsPage.checkNewsPageVisible();
        newsPage.clickEditNewsBtn();
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

        // а поскольку способа получить категорию новости из карточки
        // разрабы нам не положили, будем заниматься аутизмом
        // (а не проверять видимость PNG-картинки категории)
        // ДЕНЬ РОЖДЕНИЯ
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
        // ЗАПРЛАТА
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
        // ПРОФСОЮЗ
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
        // ПРАЗДНИК
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
        // МАССАЖ
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
        // БЛАГОДАРНОСТЬ
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
        // НУЖНА ПОМОЩЬ
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

        customAppBar.goToNewsPageWithMenuBtn();
        newsPage.clickFilterBtnToOpenFilterPage();
        newsFilterPage.insertNewsCatgryWithKeybd(cat8);
        newsFilterPage.clickFilterBtn();
        newsPage.checkNewsPageVisible();
        // далее проверяем, скрыто ли всё, кроме 8 категории
        onView(withId(newsPage.getAllNewsRecycList()))
                .check(matches(not(RecyclerCustomMatcher.atPosition(0, hasDescendant(withText(title1))))));
        onView(withId(newsPage.getAllNewsRecycList()))
                .check(matches(not(RecyclerCustomMatcher.atPosition(0, hasDescendant(withText(title2))))));
        onView(withId(newsPage.getAllNewsRecycList()))
                .check(matches(not(RecyclerCustomMatcher.atPosition(0, hasDescendant(withText(title3))))));
        onView(withId(newsPage.getAllNewsRecycList()))
                .check(matches(not(RecyclerCustomMatcher.atPosition(0, hasDescendant(withText(title4))))));
        onView(withId(newsPage.getAllNewsRecycList()))
                .check(matches(not(RecyclerCustomMatcher.atPosition(0, hasDescendant(withText(title5))))));
        onView(withId(newsPage.getAllNewsRecycList()))
                .check(matches(not(RecyclerCustomMatcher.atPosition(0, hasDescendant(withText(title6))))));
        onView(withId(newsPage.getAllNewsRecycList()))
                .check(matches(not(RecyclerCustomMatcher.atPosition(0, hasDescendant(withText(title7))))));
    }

    @Test
    @DisplayName("Check that date filter DOES show news between specified dates")
    public void shdDisplNewsBetweenChosenDates() {

        newsPage.checkNewsPageVisible();
        newsPage.clickEditNewsBtn();
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

        customAppBar.goToNewsPageWithMenuBtn();
        newsPage.clickFilterBtnToOpenFilterPage();
        newsFilterPage.checkPageVisibleByTitle();
        newsFilterPage.insertFilterPeriodStartFromClip(testDateMinus1, myContext);
        newsFilterPage.insertFilterPeriodEndFromClip(testDatePlus1, myContext);
        newsFilterPage.clickFilterBtn();

        onView(withId(newsPage.getAllNewsRecycList()))
                .check(matches(hasDescendant(withText(testTitle))));
    }

    @Test
    @DisplayName("Check that date filter DOES NOT show news outside specified dates")
    public void shdNotDisplNewsOutsideChosenDates() {

        newsPage.checkNewsPageVisible();
        newsPage.clickEditNewsBtn();
        newsControlPanelPage.clickAddNewsBtn();

        DataHelper testNewsData = new DataHelper();
        String testCategory = testNewsData.makeTestNewsCategory();
        String testTitle = testNewsData.makeTestNewsTitle();
        String testDescr = testNewsData.makeTestNewsDescr();
        String testDateOut = testNewsData.makeTestNewsDate(-3);
        // То, что можно создать новость с датой публикации до текущей, отдельный кек,
        // но про это дальше. Были бы руки, а способ найдётся.
        String testDateMinus1 = testNewsData.makeTestNewsDate(-1);
        String testDatePlus1 = testNewsData.makeTestNewsDate(1);

        creatingNewsPage.checkPageVisibleByHeaderTitle();
        creatingNewsPage.insertNewsCatgrWithKeybd(testCategory);
        creatingNewsPage.insertNewsTitle(testTitle);
        creatingNewsPage.insertNewsDescription(testDescr);
        creatingNewsPage.insertNewsPublDateFromClip(testDateOut, myContext);
        creatingNewsPage.insertNewsPublTimeByWidgetNoOffset();
        creatingNewsPage.pressSaveToCreateNews();

        customAppBar.goToNewsPageWithMenuBtn();
        newsPage.clickFilterBtnToOpenFilterPage();
        newsFilterPage.checkPageVisibleByTitle();
        newsFilterPage.insertFilterPeriodStartFromClip(testDateMinus1, myContext);
        newsFilterPage.insertFilterPeriodEndFromClip(testDatePlus1, myContext);
        newsFilterPage.clickFilterBtn();

        onView(withId(newsPage.getAllNewsRecycList()))
                .check(matches(not(hasDescendant(withText(testTitle)))));
    }

    @Test
    @DisplayName("Check that date filter DOES show news when period bounds overlap")
    public void shdDisplayNewsWhenSameBounds() {

        newsPage.checkNewsPageVisible();
        newsPage.clickEditNewsBtn();
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

        customAppBar.goToNewsPageWithMenuBtn();
        newsPage.clickFilterBtnToOpenFilterPage();
        newsFilterPage.checkPageVisibleByTitle();
        // начало и конец периода - один день: когда опубликована новость
        newsFilterPage.insertFilterPeriodStartFromClip(testDate, myContext);
        newsFilterPage.insertFilterPeriodEndFromClip(testDate, myContext);
        newsFilterPage.clickFilterBtn();

        onView(withId(newsPage.getAllNewsRecycList()))
                .check(matches(hasDescendant(withText(testTitle))));
    }

    @Test
    @DisplayName("Check that it is possible to specify one bound of a period (start)")
    // ЭТОТ ТЕСТ ДОЛЖЕН ПАДАТЬ, Т.К. ПОЧЕМУ-ТО ЛИШЬ ОДНУ ГРАНИЦУ ФИЛЬТРАЦИИ УКАЗАТЬ НЕЛЬЗЯ
    public void shdAllowToSpecifyOneBoundStart() {

        newsPage.checkNewsPageVisible();

        DataHelper testNewsData = new DataHelper();
        String testDate = testNewsData.makeTestNewsDate(0);

        newsPage.clickFilterBtnToOpenFilterPage();
        newsFilterPage.checkPageVisibleByTitle();
        newsFilterPage.insertFilterPeriodStartFromClip(testDate, myContext);
        newsFilterPage.clickFilterBtn();

        onView(withText(newsFilterPage.getWrongPeriodToastText()))
                .inRoot(withDecorView(not(newsFilterPageDecorView)))
                .check(matches(not(isDisplayed())));
    }

    @Test
    @DisplayName("Check that it is possible to specify one bound of a period (end)")
    // ЭТОТ ТЕСТ ДОЛЖЕН ПАДАТЬ, Т.К. ПОЧЕМУ-ТО ЛИШЬ ОДНУ ГРАНИЦУ ФИЛЬТРАЦИИ УКАЗАТЬ НЕЛЬЗЯ
    public void shdAllowToSpecifyOneBoundEnd() {

        newsPage.checkNewsPageVisible();

        DataHelper testNewsData = new DataHelper();
        String testDate = testNewsData.makeTestNewsDate(0);

        newsPage.clickFilterBtnToOpenFilterPage();
        newsFilterPage.checkPageVisibleByTitle();
        newsFilterPage.insertFilterPeriodEndFromClip(testDate, myContext);
        newsFilterPage.clickFilterBtn();

        onView(withText(newsFilterPage.getWrongPeriodToastText()))
                .inRoot(withDecorView(not(newsFilterPageDecorView)))
                .check(matches(not(isDisplayed())));
    }

    @Test
    @DisplayName("Check that date filter does NOT allow to specify end date BEFORE start date")
    // ТЕСТ ДОЛЖЕН ПАДАТЬ, Т.К. ПОЧЕМУ, СОБСТВЕННО, ПРИЛОЖЕНИЕ ЭТО ДОПУСКАЕТ?
    public void shdNotAllowToPlaceEndBeforeStart() {

        newsPage.checkNewsPageVisible();

        DataHelper testNewsData = new DataHelper();
        String testDateMinus1 = testNewsData.makeTestNewsDate(-1);
        String testDatePlus1 = testNewsData.makeTestNewsDate(1);

        newsPage.clickFilterBtnToOpenFilterPage();
        newsFilterPage.checkPageVisibleByTitle();
        // конец указывается ДО начала
        newsFilterPage.insertFilterPeriodStartFromClip(testDatePlus1, myContext);
        newsFilterPage.insertFilterPeriodEndFromClip(testDateMinus1, myContext);
        newsFilterPage.clickFilterBtn();

        onView(withText(newsFilterPage.getWrongPeriodToastText()))
                .inRoot(withDecorView(not(newsFilterPageDecorView)))
                .check(matches(isDisplayed())); // фактически этого уведомления нет
    }

    @Test
    @DisplayName("Check that no filters applied when CANCEL button is clicked on FILTER NEWS page")
    public void shdNotApplyFiltersWhenCancelBtnClick() {

        newsPage.checkNewsPageVisible();
        newsPage.clickEditNewsBtn();
        newsControlPanelPage.clickAddNewsBtn();
        // новость выбираемой категории
        DataHelper testNewsData1 = new DataHelper();
        String testTitle1 = testNewsData1.makeTestNewsTitle();
        String testCatgr1 = "Массаж";
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

        customAppBar.goToNewsPageWithMenuBtn();
        newsPage.checkNewsPageVisible();
        newsPage.clickFilterBtnToOpenFilterPage();
        newsFilterPage.checkPageVisibleByTitle();
        newsFilterPage.insertNewsCatgryWithKeybd(testCatgr1);
        newsFilterPage.clickCancelBtn();
        newsPage.checkNewsPageVisible();
        // должны быть видны новости обеих категорий
        onView(withId(newsPage.getAllNewsRecycList())).check(matches(hasDescendant(withText(testTitle1))));
        onView(withId(newsPage.getAllNewsRecycList())).check(matches(hasDescendant(withText(testTitle2))));
    }

    @Test
    @DisplayName("Check if ABOUt page is accessible from MAIN page through MAIN MENU button")
    // этот тест должен падать, т.к. смешное меню в шапке приложения
    public void shdGoToAboutFromMainWithMenuBtn() {
        customAppBar.goToAboutPageWithMenuBtn();
        aboutPage.checkPageVisibleByHeader();
    }
}
