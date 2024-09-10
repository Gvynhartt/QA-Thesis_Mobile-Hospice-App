package TestData;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static java.lang.System.currentTimeMillis;

import android.app.Activity;
import android.view.View;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import com.github.javafaker.Faker;

import org.hamcrest.Matcher;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class DataHelper {

    public static class TestUser {

        private final String testLogin;
        private final String testPassword;

        public TestUser(String testLogin, String testPassword) {
            this.testLogin = testLogin;
            this.testPassword = testPassword;
        }

        public String getTestLogin() {
            return testLogin;
        }

        public String getTestPassword() {
            return testPassword;
        }
    }

    public static class WidgetDateTime {
        private String dateForWidget;
        private String yearForWidget;
        private String hoursForWidget;
        private String minsForWidget;

        public WidgetDateTime(int addDays, int addMinutes) {

            long currTimeMlsc = System.currentTimeMillis();
            long timePlusDays = currTimeMlsc + addDays * 86_400_000;
            String outpDate = Instant.ofEpochMilli(timePlusDays).atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("d"));
            String outpYear = Instant.ofEpochMilli(timePlusDays).atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyy"));
            long timePlusMinutes = currTimeMlsc + addMinutes * 60 * 1000;
            String outpHours = Instant.ofEpochMilli(timePlusMinutes).atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("HH"));
            String outpMins = Instant.ofEpochMilli(timePlusMinutes).atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("mm"));

            this.dateForWidget = outpDate;
            this.yearForWidget = outpYear;
            this.hoursForWidget = outpHours;
            this.minsForWidget = outpMins;
        }

        public String getHoursForWidget() {
            return hoursForWidget;
        }
        public String getMinsForWidget() {
            return minsForWidget;
        }
    }

    public TestUser getHardcodeUser() {
        return new TestUser("login2", "password2");
    }

    public TestUser getErrorUser() {
        return new TestUser("aboba", "3450d");
    }

    public static ViewAction waitTillElementDisplay(final Matcher elViewMatcher, final long waitTime) {

        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a certain matching element for " + waitTime + "milliseconds";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = currentTimeMillis();
                final long endTime = startTime + waitTime;
                final Matcher<View> viewMatcher = elViewMatcher;
                final Matcher<View> matchDisplayed = isDisplayed();

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        try {
                            if (viewMatcher.matches(child) && matchDisplayed.matches(child)) {
                                return;
                            }
                        } catch (NoMatchingViewException viewNotFound) {
                        }
                        uiController.loopMainThreadForAtLeast(100);
                    }
                }
                while (currentTimeMillis() < endTime);

                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }

    public static void waitForUItem(Matcher itemMatcher, long waitMlsec) {
        onView(isRoot()).perform(waitTillElementDisplay(itemMatcher, waitMlsec));
    }

    public Activity getCurrentActivity() {
        final Activity[] currentActivity = new Activity[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Collection<Activity> allActivities = ActivityLifecycleMonitorRegistry.getInstance()
                        .getActivitiesInStage(Stage.RESUMED);
                if (!allActivities.isEmpty()) {
                    currentActivity[0] = allActivities.iterator().next();
                }
            }
        });
        return currentActivity[0];
    }
    public static String makeTestNewsCategory() {
        // наличие этих 5 методов-генераторов обусловлено тем, что проверки работы с новостями
        // предполагают предварительное создание собственных новостей с уникальными заголовками
        // т.к. уникальных ID для тестирования у нас в приложение не положили, выкручиваемся
        // со смищными заголовками, которые точно не будут повторяться.

        String[] arrCategory = {
                "Объявление",
                "День рождения",
                "Зарплата",
                "Профсоюз",
                "Праздник",
                "Массаж",
                "Благодарность",
                "Нужна помощь"
        };

        Random randIndex = new Random();
        int pickIndex = randIndex.nextInt(arrCategory.length);
        return arrCategory[pickIndex];
    }

    public static String makeTestNewsTitle() {

        Faker faker = new Faker(new Locale("en"));
        String outputTitle = faker.hitchhikersGuideToTheGalaxy().specie() + " "
                + faker.hacker().ingverb() + " "
                + faker.medical().diseaseName() + " " + faker.number().numberBetween(300, 5928)
                + "1 time";
        return  outputTitle;
    }

    public static String makeTestNewsDescr() {

        Faker faker = new Faker(new Locale("en"));
        String outputDescr = capitalize(faker.hacker().verb()) + " " +
                faker.demographic().demonym() + " " + faker.food().dish() + ", " +
                faker.pokemon().name() + "!";
        return outputDescr;
    }

    public static String makeTestNewsDate(int addDays) {
        // генерация даты нужна затем, что с ней можно сделать больше проверок, например, на фильтры
        // плюс вводить текст из буфера быстрее (ещё бы сразу его с клавы набирать... м-да)
        // а ещё виджет календаря помойка, для которой автотесты пишут на отдельном кругу ада
        long currTimeMlsc = System.currentTimeMillis();
        long timePlusDays = currTimeMlsc + addDays * 86_400_000;
        String outpDate = Instant.ofEpochMilli(timePlusDays).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return outpDate;
    }

    public static String makeTestNewsTime(int addMinutes) {
        long currTimeMlsc = System.currentTimeMillis();
        long timePlusMinutes = currTimeMlsc + addMinutes * 60 * 1000;
        String outpTime = Instant.ofEpochMilli(timePlusMinutes).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm"));
        return outpTime;
    }


}
