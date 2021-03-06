package top.cokernut.customview.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

import java.lang.reflect.Field;

/**
 * 当TabLayout的Tab数量少于n个的时候铺满，大于等于n个的时候滑动
 */
public class MyTabLayout extends TabLayout {
    public MyTabLayout(Context context) {
        super(context);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        try {
            if (getTabCount() == 0)
                return;
            Field field = TabLayout.class.getDeclaredField("mTabMinWidth");
            field.setAccessible(true);
            field.set(this, (int) (getMeasuredWidth() / (float) getTabCount()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
