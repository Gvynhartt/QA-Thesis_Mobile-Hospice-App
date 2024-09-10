package TestPageObjects;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static TestData.DataHelper.waitForUItem;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;

public class AboutPage {

    private final int aboutHeaderTitleImage = R.id.trademark_image_view;
    private final int appVerView = R.id.about_version_title_text_view;
    private final int appVerVal = R.id.about_version_value_text_view;
    private final int privacyLabel = R.id.about_privacy_policy_label_text_view;
    private final int termsLabel = R.id.about_terms_of_use_label_text_view;
    private final int companyInfoView = R.id.about_company_info_label_text_view;
    private final int headerContainer = R.id.container_custom_app_bar_include_on_fragment_about;
    private final String privacyLinkText = "https://vhospice.org/#/privacy-policy/";
    private final String termsLinkText = "https://vhospice.org/#/terms-of-use";
    private final String privacyLabelText = "Privacy Policy:";
    private final String termsText = "Terms of use:";
    private final String aboutVerText = "Version:";
    private final String appVerText = "1.0.0";
    private final String companyInfoText = "© I-Teco, 2022";

    public String getPrivacyLinkText() { return privacyLinkText; }
    public String getTermsLinkText() { return termsLinkText; }

    public void checkPageVisibleByHeader() {
        Allure.step("Check that ABOUT page is visible by header image");
        waitForUItem(withId(aboutHeaderTitleImage), 1000);
        onView(withId(aboutHeaderTitleImage)).check(matches(isDisplayed()));
    }

    public void checkAboutPageContentShown() {
        Allure.step("Check visibility of text content on page");
        waitForUItem(withId(headerContainer), 1000);
        onView(allOf(withId(appVerView), withText(aboutVerText))).check(matches(isDisplayed()));
        onView(allOf(withId(appVerVal), withText(appVerText))).check(matches(isDisplayed()));
        onView(allOf(withId(privacyLabel), withText(privacyLabelText))).check(matches(isDisplayed()));
        onView(allOf(withId(termsLabel), withText(termsText))).check(matches(isDisplayed()));
        onView(allOf(withId(companyInfoView), withText(companyInfoText))).check(matches(isDisplayed()));
        onView(withSubstring(privacyLinkText)).check(matches(isDisplayed()));
        onView(withSubstring(termsLinkText)).check(matches(isDisplayed()));
    }

    public void clickPrivacyPolicyLink() {
        Allure.step("Click PRIVACY POLICY link to open it in browser");
        waitForUItem(withText(privacyLinkText), 1000);
        onView(withText(privacyLinkText)).perform(click());
    }

    public void clickTermsOfUseLink() {
        Allure.step("Click TERMS OF USE link to open it in browser");
        waitForUItem(withText(termsLinkText), 1000);
        onView(withText(termsLinkText)).perform(click());
    }
}
