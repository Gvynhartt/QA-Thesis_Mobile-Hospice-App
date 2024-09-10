package TestPageObjects;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static org.hamcrest.Matchers.allOf;
import static TestData.DataHelper.waitForUItem;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.contrib.RecyclerViewActions;

import com.forkingcode.espresso.contrib.DescendantViewActions;

import TestUtils.RecyclerCustomMatcher;
import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;

public class OurMissionPage {
    private final String[] allQuotesArr = {
            "«Хоспис для меня - это то, каким должен быть мир.",
            "Хоспис в своем истинном понимании - это творчество",
            "В хосписе не работают плохие люди” В.В. Миллионщикова",
            "«Хоспис – это философия, из которой следует сложнейшая наука медицинской помощи умирающим и искусство ухода, в котором сочетается компетентность и любовь» С. Сандерс",
            "Служение человеку с теплом, любовью и заботой",
            "\"Хоспис продлевает жизнь, дает надежду, утешение и поддержку.\"",
            "\"Двигатель хосписа - милосердие плюс профессионализм\"\n" +
                    "А.В. Гнездилов, д.м.н., один из пионеров хосписного движения.",
            "Важен каждый!"
            };
    private final String[] allDescrArr = {
            "\"Ну, идеальное устройство мира в моих глазах. Где никто не оценивает, никто не осудит, где говоришь, и тебя слышат, где, если страшно, тебя обнимут и возьмут за руку, а если холодно тебя согреют.” Юля Капис, волонтер",
            "Нет шаблона и стандарта, есть только дух, который живет в разных домах по-разному. Но всегда он добрый, любящий и помогающий.",
            "Все сотрудники хосписа - это адвокаты пациента, его прав и потребностей. Поиск путей решения различных задач - это и есть хосписный индивидуальный подход к паллиативной помощи.",
            "“Творчески и осознанно подойти к проектированию опыта умирания. Создать пространство физическое и психологическое, чтобы позволить жизни отыграть себя до конца. И тогда человек не просто уходит с дороги. Тогда старение и умирание могут стать процессом восхождения до самого конца” \n" +
                    "Би Джей Миллер, врач, руководитель проекта \"Дзен-хоспис\"",
            "\"Если пациента нельзя вылечить, это не значит, что для него ничего нельзя сделать. То, что кажется мелочью, пустяком в жизни здорового человека - для пациента имеет огромный смысл.\"",
            "\" Хоспис - это мои новые друзья. Полная перезагрузка жизненных ценностей. В хосписе нет страха и одиночества.\"\n" +
                    "Евгения Белоусова, дочь пациентки Ольги Васильевны",
            "\"Делай добро... А добро заразительно. По-моему, все люди милосердны. Нужно просто говорить с ними об этом, суметь разбудить в них чувство сострадания, заложенное от рождения\" - В.В. Миллионщикова",
            "\"Каждый, кто оказывается в стенах хосписа, имеет огромное значение в жизни хосписа и его подопечных\""
    };
    private final int loveIsGumHeader = R.id.our_mission_title_text_view;
    private final int quoteListRecycler = R.id.our_mission_item_list_recycler_view;
    private final int quoteTitle = R.id.our_mission_item_title_text_view;
    private final int totalQuotesInList = 8;
    private final int toggleQuoteBtn = R.id.our_mission_item_open_card_image_button;
    private final int quoteDescr = R.id.our_mission_item_description_text_view;

    public int getTotalQuotesInList() { return totalQuotesInList; }
    public int getQuoteListRecycler() { return quoteListRecycler; }
    public int getQuoteDescr() { return quoteDescr;}

    public void checkPageVisibleByHeader(int timeToWait) {
        Allure.step("Check OUR MISSION page visibility by header title");
        waitForUItem(withId(loveIsGumHeader), timeToWait);
    }

    public void checkQuoteRecyclerHasAtLeastPos(int totalPos) {
        Allure.step("Check that news recycler list has at least [n] visible positions");
        totalPos -= 1;
        waitForUItem(withId(quoteListRecycler), 1000);
        try {
            onView(withId(quoteListRecycler))
                    .perform(RecyclerViewActions.scrollToPosition(totalPos));
            onView(withId(quoteListRecycler))
                    .check(matches(RecyclerCustomMatcher
                            .atPosition(totalPos, hasDescendant(withId(quoteTitle)))));
        } catch (NoMatchingViewException noCardsInList) {
            System.out.println(noCardsInList.getMessage());
        }
    }

    public void toggleQuoteAtPosWithBtn(int quotePos) {
        Allure.step("CLick ( ˅ / ˄ ) button to switch card state");
        waitForUItem(withId(toggleQuoteBtn), 1000);
        onView(withId(quoteListRecycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(quotePos, DescendantViewActions
                        .performDescendantAction(withId(toggleQuoteBtn), click())));
    }

    public void checkAllQuotesAndDescrVisibleOnPage() {
        Allure.step("Iterate through quote recycler checking titles & descriptions");
        waitForUItem(withId(quoteListRecycler), 1000);
        for (int i = 0; i < totalQuotesInList; i++) {
            onView(withId(quoteListRecycler)).perform(RecyclerViewActions.scrollToPosition(i));
            try {
                onView(withId(quoteListRecycler))
                        .check(matches(RecyclerCustomMatcher
                                .atPosition(i, hasDescendant(allOf(withId(quoteTitle), withSubstring(allQuotesArr[i]))))));
            } catch (NoMatchingViewException textMismatch) {
                System.out.println(textMismatch.getMessage());
            }
            try {
                onView(withId(quoteListRecycler))
                        .perform(RecyclerViewActions.actionOnItemAtPosition(i, DescendantViewActions
                                .performDescendantAction(withId(toggleQuoteBtn), click())));
                onView(withId(quoteListRecycler))
                        .check(matches(RecyclerCustomMatcher
                                .atPosition(i, hasDescendant(allOf(withId(quoteDescr), withSubstring(allDescrArr[i]))))));
            } catch (NoMatchingViewException descrMismatch) {
                System.out.println(descrMismatch.getMessage());
            }
        }
    }
}
