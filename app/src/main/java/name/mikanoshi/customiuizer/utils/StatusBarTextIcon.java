package name.mikanoshi.customiuizer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.StatusIconDisplayable;
import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class StatusBarTextIcon extends TextView implements DarkIconDispatcher.DarkReceiver, StatusIconDisplayable {
    public boolean mBlocked;
    public boolean mShown;
    public CharSequence mText;
    public int mVisibilityByDisableInfo;
    public boolean mVisibleByController;
    public int mVisibleState;
    public String subSlot = "";

    public StatusBarTextIcon(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mShown = false;
        this.mBlocked = false;
        this.mText = "";
        this.mVisibleState = 0;
    }

    @Override // android.view.View
    public void getDrawingRect(Rect rect) {
        super.getDrawingRect(rect);
        float translationX = getTranslationX();
        float translationY = getTranslationY();
        rect.left = (int) (rect.left + translationX);
        rect.right = (int) (rect.right + translationX);
        rect.top = (int) (rect.top + translationY);
        rect.bottom = (int) (rect.bottom + translationY);
    }

    @Override
    public String getSlot() {
        return "custom_text_icon_" + subSlot;
    }

    @Override
    public int getVisibleState() {
        return this.mVisibleState;
    }

    @Override
    public boolean isIconVisible() {
        return this.mVisibleByController && !this.mBlocked;
    }

    @Override
    public void onDarkChanged(ArrayList<Rect> arrayList, float f, int i, int i2, int i3, boolean z) {
        if (z) {
            setTextColor(DarkIconDispatcher.getTint(arrayList, this, i));
            return;
        }
        if (DarkIconDispatcher.getDarkIntensity(arrayList, this, f) > 0.0f) {
            i2 = i3;
        }
        setTextColor(i2);
    }

    @Override
    public void onDensityOrFontScaleChanged() {
//        setTextAppearance(R.style.TextAppearance_StatusBar_Clock);
    }

    @Override
    public void onVisibilityAggregated(boolean z) {
        super.onVisibilityAggregated(z);
        this.mShown = z;
        if (!z || TextUtils.equals(this.mText, getText())) {
            return;
        }
        setText(this.mText);
    }

    @Override
    public void setBlocked(boolean z) {
        if (this.mBlocked != z) {
            this.mBlocked = z;
            updateVisibility();
        }
    }

    @Override
    public void setDecorColor(int i) {
    }

    public void setContent(String str) {
        this.mText = str;
        if (!this.mShown || TextUtils.equals(str, getText())) {
            return;
        }
        setText(this.mText);
    }

    @Override
    public void setStaticDrawableColor(int i) {
    }

    public void setVisibilityByController(boolean z) {
        if (this.mVisibleByController != z) {
            this.mVisibleByController = z;
            updateVisibility();
        }
    }

    public void setVisibilityByDisableInfo(int i) {
        if (this.mVisibilityByDisableInfo != i) {
            this.mVisibilityByDisableInfo = i;
            updateVisibility();
        }
    }

    @Override
    public void setVisibleState(int i, boolean z) {
        if (this.mVisibleState != i) {
            this.mVisibleState = i;
            setWillNotDraw(i != 0);
        }
    }

    public void updateVisibility() {
        if (!this.mVisibleByController || this.mBlocked) {
            setVisibility(View.GONE);
        } else {
            setVisibility(this.mVisibilityByDisableInfo);
        }
    }
}