package TestUtils;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

public class GetTextAction implements ViewAction {

    private String text;

    @Override public Matcher<View> getConstraints() {
        return isAssignableFrom(TextView.class);
    }

    @Override public String getDescription() {
        return "get text";
    }

    @Override public void perform(UiController uiController, View view) {
        TextView textView = (TextView) view;
        text = textView.getText().toString();
    }

    @Nullable
    public String getText() {
        return text;
    }

    // USAGE:
//    GetTextAction action = new GetTextAction();
//    onView(allOf(isDescendantOf(...), withId(...), withEffectiveVisibility(...)))
//            .perform(action);
//
//    CharSequence text = action.getText();
}
