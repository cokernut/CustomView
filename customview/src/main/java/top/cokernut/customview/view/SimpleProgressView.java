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
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

import top.cokernut.customview.R;

/**
 * 刷新View
 */
public class SimpleProgressView extends View {
    private ArrayList<Integer> mColors; // 默认颜色

    private Paint mPaint;
    private float mWidth = 0f;
    private float mPadding = 0f;
    private float startAngle = 0f;
    private float angle = 120f;
    private RectF rectF;
    private ValueAnimator valueAnimator;

    public SimpleProgressView(Context context) {
        super(context);
        init(null, 0);
    }

    public SimpleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SimpleProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.SimpleProgressView, defStyle, 0);
        mColors = new ArrayList<>();
        mColors.add(Color.GREEN);
        int color = a.getColor(R.styleable.SimpleProgressView_progressColor, mColors.get(0));
        mColors.set(0, color);
        a.recycle(); //回收TypedArray,用于后续调用时可复用

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
    }

    /**
     * getWidth()和getMeasuredWidth()的区别
     * getWidth(): View在设定好布局后，整个View的宽度
     * getMeasuredWidth()：对View上的内容进行测量后得到的View内容占据的宽度。
     * 很简单，getWidth()就是View显示之后的width，而getMeasuredWidth，
     * 从前面的源代码就可以看出来其实是在measure里面传入的参数，具体是否一样完全要看程序最后的计算。
     * getMeasuredHeight，getMeasuredWidth 测量的高度，宽度
     * getHeight，getWidth 显示的高度，宽度
     * 实际上在当屏幕可以包裹内容的时候，他们的值是相等的，只有当view超出屏幕后，
     * 才能看出他们的区别。当超出屏幕后，getMeasuredHeight()等于getHeight()加上屏幕之外没有显示的高度。
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() > getHeight()) {
            mWidth = getMeasuredHeight();
        } else {
            mWidth = getMeasuredWidth();
        }
        mPadding = 10f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.argb(50, 255, 255, 255));
        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2 - mPadding, mPaint);
        rectF = new RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding);
        if (mColors.size() < 2) {
            mPaint.setColor(mColors.get(0));
            canvas.drawArc(rectF, startAngle, angle, false, mPaint);
        } else {
            angle = 360f / mColors.size();
            for (int i = 0; i < mColors.size(); i++) {
                mPaint.setColor(mColors.get(i));
                canvas.drawArc(rectF, (startAngle + angle * i) % 360, angle, false, mPaint);
            }
        }
    }

    public void startAnim() {
        stopAnim();
        startViewAnim(0f, 1f, 1000);
    }

    private ValueAnimator startViewAnim(float startF, float endF, int time) {
        valueAnimator = ValueAnimator.ofFloat(startF, endF);    //Value值的伴随动画的过渡范围
        valueAnimator.setDuration(time);                        //一遍动画的时间
        valueAnimator.setInterpolator(new LinearInterpolator());//插值器，动画形式，LinearInterpolator代表均匀变化
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);   //循环次数
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);     //循环模式

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                startAngle = 360 * value;
                //刷新视图
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
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

    public void stopAnim() {
        if (valueAnimator != null) {
            clearAnimation();
            valueAnimator.setRepeatCount(0);
            valueAnimator.cancel();
            valueAnimator.end();
        }
    }


    /**
     * 取得颜色
     *
     * @return 一个int类型的数组，其中的值为16进制的颜色
     */
    public ArrayList<Integer> getProgressColors() {
        return mColors;
    }

    /**
     * 设置颜色
     *
     * @param colors 可变长参数，每一个值代表一种颜色
     */
    public void setProgressColors(int... colors) {
        if (colors.length > 0) {
            mColors.clear();
            for (int i = 0; i < colors.length; i++) {
                mColors.add(colors[i]);
            }
        }
    }
}
