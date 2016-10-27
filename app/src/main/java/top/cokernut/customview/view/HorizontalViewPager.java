package top.cokernut.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 类似ViewPager功能
 * /**
 * 可以水平滑动的ViewGroup
 * 总有有以下几个知识点:
 * 1. VelocityTracker:用来检测速度,如果水平速度达到一定阈值则切换页面
 * 2. Scroller:弹性滑动,水平滑动到一半的时候,放开以后View滑动到标准的某个页面的位置
 * 3. onInterceptTouchEvent:拦截机制,也是解决滑动冲突,当水平位移大于垂直位移的时候就有当前组件处理触摸事件而不是子View
 * 4. 自定义ViewGroup：需要自己实现onMeasure（尤其要处理wrap_content的情况），自己去实现onLayout
 * （需要去调用每一个子View的layout函数去布局子View）
 */
public class HorizontalViewPager extends ViewGroup {

    public HorizontalViewPager(Context context) {
        super(context);
        init();
    }

    public HorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private Scroller scroller; //Scroller，弹性滑动
    private VelocityTracker tracker; //VelocityTracker，用来测试滑动速度的。

    private void init() {
        //1.2 在构造函数中初始化Scroller
        scroller = new Scroller(getContext());
        tracker=VelocityTracker.obtain();
    }

    //处理滑动冲突,也就是什么时候返回true的问题
    //规则:开始滑动时水平距离超过垂直距离的时候
    int lastInterceptX; //记录上一次触摸的位置
    int lastInterceptY;

    /**
     * 这里ViewGroup是水平滑动，里面的ListView是垂直滑动，一般我们设置的逻辑是：
     1. 如果我们检测到的滑动方向是水平的话，就让父View拦截用来进行View的滑动切换
     2. 如果检测到方向是垂直的话，就不进行拦截，交给子View去处理，比如ListView的垂直滑动
     当然，也有些逻辑是触摸起始位置在边缘且是水平方向才会进行页面的切换，因为子View可能也需要水平方向的滑动事件。
     */
    /**
     * 当我们快速向左滑动切换到下一个页面的情况，在手指释放（ACTION_UP）以后，页面会弹性滑动到下一个页面，
     * 可能需要一秒才完成滑动，这个时间内，我们再次触摸屏幕，希望能拦截这次滑动，然后再次去操作页面。
     这部分的方法如下：
     要实现在弹性滑动过程中再次触摸拦截，肯定要在onInterceptTouchEvent中的ACTION_DOWN中去判断，
     如果在ACTION_DOWN的时候，scroller还没有完成，说明上一次的滑动还正在进行中，则直接终端scroller并且返回true，
     表示在DOWN的时候就拦截事件，那么后续的MOVE，UP都不会传递到子View中去了。
     */
    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        boolean intercept = false;
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //DOWN返回false,导致onTouchEvent中无法获取到DOWN
                intercept = false;
                if (!scroller.isFinished()) { //如果动画还没有执行完成,则打断,这种情况肯定还是由父组件处理触摸事件所以返回true
                    scroller.abortAnimation();
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - lastInterceptX; //水平方向滑动的距离（有正有负）
                int deltaY = y - lastInterceptY; //垂直方向滑动的距离（有正有负）
                //水平方向距离长  MOVE中返回true一次,后续的MOVE和UP都不会收到此请求
                if (Math.abs(deltaX) - Math.abs(deltaY) > 0) {
                    //水平方向距离更长，说明用户是想水平滑动的，所以拦截
                    intercept = true;
                } else {
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        //因为一个滑动事件是先经过的DOWN,所以在MOVE的时候,这两个值已经设置过了
        lastInterceptX = x;
        lastInterceptY = y;
        //因为DOWN返回true,所以onTouchEvent中无法获取DOWN事件,所以这里要负责设置lastX,lastY
        lastX = x;
        lastY = y;
        return intercept;
    }

    int lastX; //记录上一次触摸事件的位置
    int lastY;
    int currentIndex = 0; //当前子元素
    int childWidth = 0; //子元素的宽度，这个我们可以在onLayout或者onMeasure中进行赋值 ，
    // 只要保证在measure结束之后，把他设置为第一个子View的宽度即可（本例中在下面的`onLayout`中进行设置）
    /**
     * 如果你执行当前状态的代码去测试的话，你会发现又一个问题。
     * 我按下滑动的时候，刚按下，整个View瞬间跳到了我手指按下的地方然后开始跟随手指滑动，这是为什么呢？
     * 其实原因很简单，我这里执行了scrollBy()让水平方向移动了deltaX个单位，说明问题就出在计算这个deltaX上了。
     * 其实原因在于lastX的值的设置上，这个和ViewGroup的拦截机制相关，我们前面在MOVE的时候拦截了触摸事件，
     * 但是在DOWN的时候是返回false的，所以DOWN时候的触摸事件被子View消耗掉了，所以onTouchEvent中是无法看到DOWN的，
     * 所以lastX就不会像在onInterceptTouchEvent中一样，在DOWN的时候被赋值，
     * 然后第一个MOVE的时候就可以判断当前的滑动式水平还是垂直了。由于onTouchEvent中没有DOWN事件了，第一个MOVE的时候，
     * lastX=0，而第一个MOVE的getX就是当前鼠标的位置，两个值相减得到的还是x,而不是一个TouchSlop，
     * 所以View内容就立即滑动到了当前手指的位置，解决办法就是将lastX和lastY在onInterceptTouchEvent中也进行赋值，
     * 因为DOWN事件在onInterceptTouchEvent中也可以得到，这种，第一个MOVE拿到的也是一个TouchSlop：
     */
    /**
     * 这里增加一个逻辑，就是我们不需要滑动超过一般才切换到上/下一个页面，如果滑动速度很快的话，我们也可以判定为用户想要滑
     * 动到其他页面，这样的体验也是好的。这部分也是在onTouchEvent中的ACTION_UP部分，处理逻辑如下：
     1. 检测当前滑动的速度，如果超过一定的阈值则就算滑动没有超过一半也进行页面切换；
     2. 向左快速滑动则切换到下一个子View；
     3. 向右快速滑动则切换到上一个子View。
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //得到本次触摸的位置
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - lastX; //手指滑动的距离，跟随手指滑动
                //调用该方法让View也对应的移动指定的距离，这样就实现了跟随手指滑动的效果，垂直方向不移动
                scrollBy(-deltaX, 0);
                break;
            case MotionEvent.ACTION_UP: //释放手指以后开始自动滑动到目标位置
                int distance = getScrollX() - currentIndex * childWidth; //相对于当前View滑动的距离,正为向左,负为向右
                if (Math.abs(distance) > childWidth / 2) {//必须滑动的距离要大于1/2个宽度,否则不会切换到其他页面
                    if (distance > 0) {
                        currentIndex++;
                    } else {
                        currentIndex--;
                    }
                } else {
                    //调用该方法计算1000ms内滑动的平均速度
                    tracker.computeCurrentVelocity(1000);
                    float xV = tracker.getXVelocity(); //获取到水平方向上的速度
                    if (Math.abs(xV) > 50) { //如果速度的绝对值大于50的话，就认为是快速滑动，就执行切换页面
                        if (xV > 0) { //大于0切换上一个页面
                            currentIndex--;
                        } else { //小于0切换到下一个页面（子View）
                            currentIndex++;
                        }
                    }
                }
                currentIndex = currentIndex < 0 ? 0 : currentIndex > getChildCount() - 1 ?
                        getChildCount() - 1 : currentIndex;
                smoothScrollTo(currentIndex * childWidth, 0);
                //最后要进行的是VelocityTracker#clear重置速度计算器
                tracker.clear();
                break;
        }
        lastX = x; //存储当前位置为上一次位置
        lastY = y;
        //return super.onTouchEvent(event);
        // 这里不能调用父类的方法,因为ViewGroup是没有去实现onTouchEvent的方法的,所以super调用的是View的实现;
        // 而View的默认实现是没有点击长按等事件导致在DOWN的时候就直接返回false了,导致没有执行MOVE和UP方法
        return true;
    }

    //这里需要测量这个ViewGroup的宽和高
    // 如果对View的宽高进行修改了，不要调用super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    // 要调用setMeasuredDimension(widthsize,heightsize); 这个函数。
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      //  super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //得到宽和高的MODE和SIZE
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //测量所有子元素，先执行，不然后面拿不到第一个子元素的测量宽/高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //处理wrap_content的情况
        //如果没有子元素，就设置宽高都为0（简化处理）
        if (getChildCount() == 0) {
            setMeasuredDimension(0, 0);
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            //宽和高都是AT_MOST，则设置宽为第一个子元素的宽度乘以子元素的个数（这里默认每个子元素都和第一个元素一样的宽度）；
            //高度设置为第一个元素的高度；
            //当然，我们最后用的时候子元素的宽度和高度就是屏幕的宽度和高度
            View childOne = getChildAt(0);
            int childWidth = childOne.getMeasuredWidth();
            int childHeight = childOne.getMeasuredHeight();
            setMeasuredDimension(childWidth * getChildCount(), childHeight);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //这里只有宽度是AT_MOST，那就设置高度为系统测量的高度，宽度和第一个if中的一样
            View childOne = getChildAt(0);
            int childWidth = childOne.getMeasuredWidth();
            setMeasuredDimension(childWidth * getChildCount(), heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //这里只有高度是AT_MOST，那就设置宽度为系统测量的宽度，高度和第一个if中的一样
            int childHeight = getChildAt(0).getMeasuredHeight();
            setMeasuredDimension(widthSize, childHeight);
        }
        //宽度和高度都不是AT_MOST的情况在super方法中已经设置了：宽高都是系统测量的结果；
    }

    //这里实现布局，主要是对子View进行布局，因为每一种布局方式子View的布局都是不同的，
    //所以这个是ViewGroup唯一一个抽象方法，需要我们自己去实现
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount(); //子元素的个数
        int left = 0;    //左边的距离
        View child;
        //遍历布局子元素
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            int width = child.getMeasuredWidth();
            childWidth = width; //**新代码： 赋值为子元素的宽度**
            //调用每个子元素的layout方法去布局这个子元素，这里相当于默认第一个子元素占满了屏幕，
            // 后面的子元素就是在第一个屏幕后面紧挨着和屏幕一样大小的后续元素，所以left是一直累加的，
            // top保持0，bottom保持第一个元素的高度，right就是left+元素的宽度
            child.layout(left, 0, left + width, child.getMeasuredHeight());
            left += width;
        }
    }

    /**
     * 处理逻辑：
     1. 如果当前页面是第一个页面，向右滑动任意单位后均弹性滑动回当前页面
     2. 如果当前页面是最后一个页面，向左滑动任意单位均弹性滑动回当前页面
     3. 如果向左滑动滑动超过宽度（也就是屏幕）的一半则跳转到下一个页面
     4. 如果向右滑动超过宽度的一般则跳转到上一个页面
     方法是在ACTION_UP中进行处理，因为只有在滑动完成释放的时候，我们才会让页面去自动滑动到下/上一个View或者滑动回当前View。
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        //2.1 先计算当前Scroller的偏移
        if (scroller.computeScrollOffset()) {
            //2.2 然后调用我们熟悉的scrollTo将View移动到getCurrX,getCurrY的位置
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            //2.3 通知刷新界面
            postInvalidate();
        }
    }
    //3. 这个是工具方法，弹性滑动到指定位置
    public void smoothScrollTo(int destX, int destY) {
        //3.1 调用startScroll
        scroller.startScroll(getScrollX(), getScrollY(), destX - getScrollX(), destY - getScrollY(), 1000);
        //3.2 刷新
        invalidate();
    }
}