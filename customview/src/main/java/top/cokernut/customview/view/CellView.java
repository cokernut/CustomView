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

import top.cokernut.customview.R;

/**
 * 电池
 */
public class CellView extends View {
    private int mAmountColor = Color.RED; // 中间的颜色
    private float mLineSize = 4f; // 画笔大小（线的粗细）
    private int mLineColor = Color.WHITE; //边缘线和字体颜色的颜色

    private Paint mPaint;
    private float mWidth;
    private float mHeight;
    private Paint mTextPaint;
    private String mText;
    private ValueAnimator mAnimator;
    private float mValue = 0f;

    public CellView(Context context) {
        super(context);
        init(null, 0);
    }

    public CellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CellView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CellView, defStyle, 0);
        mAmountColor = a.getColor(R.styleable.CellView_amountColor, mAmountColor);
        mLineColor = a.getColor(R.styleable.CellView_amountColor, mLineColor);
        mLineSize = a.getDimension(R.styleable.CellView_lineSize, mLineSize);
        a.recycle();
    }

    /**
     * getWidth()和getMeasuredWidth()的区别
     * getWidth(): View在设定好布局后，整个View的宽度
     * getMeasuredWidth()：对View上的内容进行测量后得到的View内容占据的宽度。
     * 很简单，getWidth()就是View显示之后的width，而getMeasuredWidth，
     * 从前面的源代码就可以看出来其实是在measure里面传入的参数，具体是否一样完全要看程序最后的计算。
     */
    // 如果对View的宽高进行修改了之后，不要调用super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    // 要调用setMeasuredDimension(widthsize,heightsize); 这个函数。
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        if (mWidth/2 > mHeight/2) {
            mPaint.setColor(Color.GREEN);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawRect(mWidth/4, mHeight/4, mWidth/4 + mValue*mWidth/2, mHeight/4*3, mPaint);
            mPaint.setColor(mLineColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(mWidth/4*3, mHeight/2-4*mLineSize, mWidth/4*3+4*mLineSize, mHeight/2+4*mLineSize, mPaint);
            mPaint.setStyle(Paint.Style.STROKE);
            RectF rectF = new RectF(mWidth/4-mLineSize, mHeight/4-mLineSize, mWidth/4*3+mLineSize, mHeight/4*3+mLineSize);
            canvas.drawRoundRect(rectF, mLineSize*2, mLineSize*2, mPaint);
        } else {
            mPaint.setColor(Color.GREEN);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawRect(mWidth/4, mHeight/4*3-mValue*mHeight/2, mWidth/4*3, mHeight/4*3, mPaint);
            mPaint.setColor(mLineColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(mWidth/2-4*mLineSize, mHeight/4-4*mLineSize, mWidth/2+4*mLineSize, mHeight/4, mPaint);
            mPaint.setStyle(Paint.Style.STROKE);
            RectF rectF = new RectF(mWidth/4-mLineSize, mHeight/4-mLineSize, mWidth/4*3+mLineSize, mHeight/4*3+mLineSize);
            canvas.drawRoundRect(rectF, mLineSize*2, mLineSize*2, mPaint);
        }
        String mText = String.valueOf((int) (mValue * 100)) + "%";
        canvas.drawText(mText, mWidth / 2, mHeight / 2 + 4*mLineSize, mTextPaint);
    }

    private void initPaint() {
        mPaint =  new Paint();
        mPaint.setColor(mLineColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mLineSize);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStrokeWidth(mLineSize);
        mTextPaint.setColor(mLineColor);
        mTextPaint.setTextSize(dip2px(20));
    }

    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void startAnim() {
        stopAnim();
        startViewAnim(0f, 1f, 6000);
    }

    public void stopAnim() {
        if (mAnimator != null) {
            clearAnimation();
            mAnimator.setRepeatCount(0);
            mAnimator.cancel();
            mAnimator.end();
            mValue = 0;
            postInvalidate();
        }
    }

    private ValueAnimator startViewAnim(float start, float end, int time) {
        mAnimator = ValueAnimator.ofFloat(start, end);
        mAnimator.setDuration(time);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        if (!mAnimator.isRunning()) {
            mAnimator.start();
        }
        return mAnimator;
    }

    public String getText() {
        return mText;
    }

    public void setText(String exampleString) {
        mText = exampleString;
    }

    public int getAmountColor() {
        return mAmountColor;
    }

    public void setAmountColor(int color) {
        mAmountColor = color;
    }

    public float getLineSize() {
        return mLineSize;
    }

    public void setLineSize(float size) {
        mLineSize = size;
    }

    public int getLineColor() {
        return mLineColor;
    }

    public void setLineColor(int color) {
        mLineColor = color;
    }
}
