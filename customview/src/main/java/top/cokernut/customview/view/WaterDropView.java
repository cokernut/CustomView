package top.cokernut.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import top.cokernut.customview.R;

/**
 * 水滴
 */
public class WaterDropView extends View {
    private int mWaterColor = Color.GREEN; //水滴颜色

    private Paint mPaint;

    public WaterDropView(Context context) {
        super(context);
        init(null, 0);
    }

    public WaterDropView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public WaterDropView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WaterDropView, defStyle, 0);
        mWaterColor = a.getColor(R.styleable.WaterDropView_WaterColor, mWaterColor);
        a.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mWaterColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }


    /**
     * 取水滴颜色
     *
     * @return 水滴颜色
     */
    public int getWaterColor() {
        return mWaterColor;
    }

    /**
     * 设置水滴颜色
     *
     * @param color 颜色
     */
    public void setWaterColor(int color) {
        mWaterColor = color;
    }
}
