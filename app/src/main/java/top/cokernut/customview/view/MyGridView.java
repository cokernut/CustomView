package top.cokernut.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 解决GridView在ScrollView中显示不全的问题
 */

public class MyGridView extends GridView {
	 
    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle); 
        // TODO Auto-generated constructor stub 
    } 
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs); 
        // TODO Auto-generated constructor stub 
    } 
    public MyGridView(Context context) {
        super(context); 
        // TODO Auto-generated constructor stub 
    } 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
        // TODO Auto-generated method stub 
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);  
 
    } 
    
    
    //下面的两个函数注释掉了，使用默认的就好，不然获取不到这个Gridview的Item
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//            return true;// true 拦截事件自己处理，禁止向下传递
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//            return false;// false 自己不处理此次事件以及后续的事件，那么向上传递给外层view
//    }

} 
