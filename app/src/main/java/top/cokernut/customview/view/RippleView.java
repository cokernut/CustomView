package top.cokernut.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import top.cokernut.customview.R;

/**
 * 波纹效果
 */
public class RippleView extends View {
    private int mLineColor = Color.BLUE; // 线的颜色
    private float mLineSize = 2; // 线的大小

    private Paint mPaint;
    private Path mPath;
    private float mWidth = 0f;
    private float mHeight = 0f;
    private float mLength = 0f;
    private ValueAnimator mAnimator;
    private float mValue = 0f;
    private PointF mStartPoint;
    private PointF mControlOnePoint;
    private PointF mControlTwoPoint;
    private PointF mCenterPoint;
    private PointF mEndPoint;
    private PointF mControlThreePoint;
    private PointF mControlFourPoint;

    public RippleView(Context context) {
        super(context);
        init(null, 0);
    }

    public RippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RippleView, defStyle, 0);
        mLineColor = a.getColor(R.styleable.RippleView_lineColor, mLineColor);
        mLineSize = a.getDimension(R.styleable.RippleView_lineSize, mLineSize);
        a.recycle();
        mPaint = new Paint();
        mPaint.setColor(mLineColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mLineSize);
        mPath = new Path();
        mStartPoint = new PointF(0, 0);
        mCenterPoint = new PointF(0, 0);
        mEndPoint = new PointF(0, 0);
        mControlOnePoint = new PointF(0, 0);
        mControlTwoPoint = new PointF(0, 0);
        mControlThreePoint = new PointF(0, 0);
        mControlFourPoint = new PointF(0, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mLength = mWidth/3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mStartPoint.x = mWidth * (mValue - 1);
        mStartPoint.y = mHeight / 2;
        mCenterPoint.x = mWidth * mValue;
        mCenterPoint.y = mHeight / 2;
        mEndPoint.x = mWidth * (1+mValue);
        mEndPoint.y = mHeight / 2;
        mControlOnePoint.x = mStartPoint.x + mLength;
        mControlOnePoint.y = 0;
        mControlTwoPoint.x = mStartPoint.x + mLength * 2;
        mControlTwoPoint.y = mHeight;
        mControlThreePoint.x = mCenterPoint.x + mLength;
        mControlThreePoint.y = 0;
        mControlFourPoint.x = mCenterPoint.x + mLength * 2;
        mControlFourPoint.y = mHeight;
        mPath.moveTo(mStartPoint.x, mStartPoint.y);
        mPath.cubicTo(mControlOnePoint.x, mControlOnePoint.y, mControlTwoPoint.x, mControlTwoPoint.y,
                mCenterPoint.x, mCenterPoint.y);
        mPath.moveTo(mCenterPoint.x, mCenterPoint.y);
        mPath.cubicTo(mControlThreePoint.x, mControlThreePoint.y, mControlFourPoint.x, mControlFourPoint.y,
                mEndPoint.x, mEndPoint.y);
        mPath.lineTo(mEndPoint.x, mHeight);
        mPath.lineTo(mStartPoint.x, mHeight);
        mPath.lineTo(mStartPoint.x, mStartPoint.y);
        canvas.drawPath(mPath, mPaint);
    }

    public void startAnim() {
        stopAnim();
        startViewAnim(0f, 1f, 2000);
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
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
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

    /**
     * 取得线的大小
     *
     * @return 线的大小
     */
    public int getLineColor() {
        return mLineColor;
    }

    /**
     * 设置线的颜色
     *
     * @param color 颜色
     */
    public void setLineColor(int color) {
        mLineColor = color;
        mPaint.setColor(mLineColor);
    }

    /**
     * 取得线的大小
     *
     * @return 线的大小
     */
    public float getLineSize() {
        return mLineSize;
    }

    /**
     * 设置线的大小
     *
     * @param size 大小
     */
    public void setLineSize(float size) {
        mLineSize = size;
        mPaint.setStrokeWidth(size);
    }
}
