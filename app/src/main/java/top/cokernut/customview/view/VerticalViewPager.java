package top.cokernut.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;

import top.cokernut.customview.R;

/**
 * 竖向的ViewPager
 */
public class VerticalViewPager extends ViewGroup {
    private int mColor = Color.RED; //

    public VerticalViewPager(Context context) {
        super(context);
        init(null, 0);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public VerticalViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.VerticalViewPager, defStyle, 0);

        mColor = a.getColor(
                R.styleable.VerticalViewPager_bgColor,
                mColor);

        a.recycle();


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;


    }

    /**
     * 取颜色
     *
     * @return 颜色
     */
    public int getColor() {
        return mColor;
    }

    /**
     * 设置颜色
     *
     * @param color 颜色
     */
    public void setColor(int color) {
        mColor = color;
    }
}
