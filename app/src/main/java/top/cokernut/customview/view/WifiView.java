package top.cokernut.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import top.cokernut.customview.R;

/**
 * Wifi图标.
 */
public class WifiView extends View {
    private int mWifiColor = Color.WHITE; //颜色

    private Paint mPaint;
    private float mWidth = 0f;
    private ValueAnimator valueAnimator;
    private int mAnimatedValue = 0;
    private RectF mRectF;
    private int r = 0;

    public WifiView(Context context) {
        super(context);
        init(null, 0);
    }

    public WifiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public WifiView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WaterDropView, defStyle, 0);
        mWifiColor = a.getColor(R.styleable.WifiView_wifiColor, mWifiColor);
        a.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mWifiColor);
        mPaint.setStrokeWidth(8);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() > getHeight())
            mWidth = getMeasuredHeight();
        else
            mWidth = getMeasuredWidth();
        r = (int) (mWidth / 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // canvas.save(); //锁画布(为了保存之前的画布状态)
        mRectF = new RectF(mWidth/2-r, mWidth/2-r, mWidth/2+r, mWidth/2+r);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(mRectF, 225, 90, true, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        if (mAnimatedValue > 0 && mAnimatedValue < 5) {
            for (int i = 1; i < mAnimatedValue; i++) {
                mRectF = new RectF(mWidth/2-r*(1+i), mWidth/2-r*(1+i), mWidth/2+r*(1+i), mWidth/2+r*(1+i));
                canvas.drawArc(mRectF, 225, 90, false, mPaint);
            }
        }
      //  canvas.restore();
    }


    public void startAnim() {
        stopAnim();
        startViewAnim(0, 5, 4000);
    }


    public void stopAnim() {
        if (valueAnimator != null) {
            clearAnimation();
            valueAnimator.setRepeatCount(0);
            valueAnimator.cancel();
            valueAnimator.end();
            mAnimatedValue = 0;
            postInvalidate();
        }
    }

    private ValueAnimator startViewAnim(int start, int end, long time) {
        valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.setDuration(time);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
        return valueAnimator;
    }


    /**
     * 取颜色
     *
     * @return 颜色
     */
    public int getWifiColor() {
        return mWifiColor;
    }

    /**
     * 设置颜色
     *
     * @param color 颜色
     */
    public void setWifiColor(int color) {
        mWifiColor = color;
    }
}