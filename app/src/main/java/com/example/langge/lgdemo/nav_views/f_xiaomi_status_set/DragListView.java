package com.example.langge.lgdemo.nav_views.f_xiaomi_status_set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

/**
 * Created by langge on 18-3-27.
 */
public class DragListView extends ListView {


//    private DragItemListener mDragItemListener;


    private DragAdapter adapter;

    private boolean isDragStatus;

    private View mItemView;

    private Bitmap mBitmap = null;

    private int mCurrentPosition;
    private int mLastPosition;

    private int mDragViewOffset=0; // 触摸点在itemView中的高度

    private int mLastX=0, mLastY=0;
    private int mDownX=0, mDownY=0;

    Paint mPaint;
    BlurMaskFilter bf;
    int radius = 10;

    public DragListView(Context context) {
        this(context,null);
    }

    public DragListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        this.setOnItemLongClickListener(new OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("fdafadfwefw", "pos="+position);
//                return false;
//            }
//        });

        init(context);


    }

    void init(Context context){
        // 实例化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(Color.DKGRAY);
        //outer solid normal 都可以  但是inner不行
        bf = new BlurMaskFilter(radius, BlurMaskFilter.Blur.OUTER);
        mPaint.setMaskFilter(bf);
        mPaint.setAlpha(120);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

//    @Override
//    public void setOnLongClickListener(OnLongClickListener l) {
//        Log.e("fdsafdasfa", "fdasfafasfa");
//        super.setOnLongClickListener(l);
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        switch (action){

            case MotionEvent.ACTION_DOWN:

                stopDrag();
                mLastX = mDownX = (int) ev.getX();
                mLastY = mDownY = (int) ev.getY();

                if(startDrag(false, mDownX, mDownY)){
                    return true;
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if(isDragStatus) {
                    mLastX = (int) ev.getX();
                    mLastY = (int) ev.getY();
                    onDrag(mLastY);
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
                isDragStatus = false;
                if (mBitmap != null) {
                    mLastX = (int) ev.getX();
                    mLastY = (int) ev.getY();
                    onDrop(mLastY);
                    invalidate();
                    return true;
                }
                break;

            default:
//                isDragStatus = false;
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mBitmap!=null&&!mBitmap.isRecycled()){
            // 先绘制阴影
            Bitmap bitmap = mBitmap.extractAlpha(mPaint, null);
            mPaint.setAlpha(180);
            canvas.drawBitmap(bitmap, 0, mLastY - mDragViewOffset-radius, mPaint);
            // 再绘制位图
            Paint paint = new Paint();
            paint.setAlpha(180);
            canvas.drawBitmap(mBitmap, 0, mLastY - mDragViewOffset, paint);

        }

    }


    public boolean startDragOnLong(){
        return startDrag(true, 0, mLastY);
    }

    public boolean startDrag(boolean isLong, int x, int y){

        int tempPos = pointToPosition(0, y);
        if(tempPos==INVALID_POSITION){
            return false;
        }

        mLastPosition = mCurrentPosition = tempPos;
        ViewGroup downView = (ViewGroup) getChildAt(mCurrentPosition - getFirstVisiblePosition());
        if (downView != null) {
            if(adapter==null){
                adapter = (DragAdapter) getAdapter();
            }
            if(isLong){
                if (adapter==null || adapter.isTag(tempPos)){
                    return false;
                }
            }else {
                if (adapter==null || !adapter.canDrag(tempPos, downView, x, y)){
                    return false;
                }
            }

            // 触摸点在item项中的高度
            mDragViewOffset = mDownY - downView.getTop();

            downView.setDrawingCacheEnabled(true);
            mBitmap = Bitmap.createBitmap(downView.getDrawingCache());
            downView.setDrawingCacheEnabled(false);

            mLastY = mDownY;
            mLastX = mDownX;
            mItemView = downView;
            if (mItemView!=null){
                mItemView.setVisibility(INVISIBLE);
            }
            isDragStatus = true;
            return true;

        }
        return false;
    }

    public void onDrag(int moveY){

        Log.e("dfafda", "movePos="+moveY);

        if (moveY < 0) { // 限制触摸范围在ＬｉｓｔＶｉｅｗ中
            moveY = 0;
        } else if (moveY > getHeight()) {
            moveY = getHeight();
        }
        int movePos = pointToPosition(0,moveY);
        if(movePos==INVALID_POSITION){
            checkScroller(moveY); // listview移动.
            return;
        }

        mLastY = moveY;

        int dirCell = movePos > mLastPosition? 1 : -1; //1从上往下移动, -1反之

        for (int i=mLastPosition;(dirCell>0)?i<=movePos:i>=movePos;  i+=dirCell){

            int index = i-getFirstVisiblePosition();
            if(index>getChildCount() || index<0){
                continue;
            }

            int y = getChildAt(index).getTop();
            int temPos = pointToPosition(0, y);
            if(temPos!=INVALID_POSITION){
                mCurrentPosition = temPos;
            }
            // 数据交换
            if (mCurrentPosition != mLastPosition) {

                if (adapter==null){
                    adapter = (DragAdapter) getAdapter();
                }
                if (adapter!=null){
                    if(adapter.exchange(mLastPosition, mCurrentPosition)){
                        View lastView = mItemView;
                        mItemView = getChildAt(mCurrentPosition - getFirstVisiblePosition());
                        //动画
                        exchangeAnimal(mLastPosition, mCurrentPosition, lastView, mItemView);
                        mLastPosition = mCurrentPosition;
                    }
                }
            }

        }

        checkScroller(moveY); // listview移动.

    }

    private int mAutoScrollUpY; // 拖动的时候，开始向上滚动的边界
    private int mAutoScrollDownY; // 拖动的时候，开始向下滚动的边界
    private int mTouchSlop;
    private boolean mScrolling = false;
    private Runnable mScrollRunnable;

    /***
     * 移动到底部或顶部时自动滚动列表
     * 当移动到底部时，ListView向上滑动，当移动到顶部时，ListView要向下滑动
     */
    public void checkScroller(final int y) {

        int offset = 0;
        if (y < mAutoScrollUpY) { // 拖动到顶部，ListView需要下滑
            if (y <= mDownY - mTouchSlop) {
                offset = dp2px(getContext(), 3); // 滑动的距离
            }
        } else if (y > mAutoScrollDownY) { // 拖动到底部，ListView需要上滑
            if (y >= mDownY + mTouchSlop) {
                offset = -dp2px(getContext(), 3); // 滑动的距离
            }
        }

        Log.e("mLastScrollTime", offset+","+y+","+mAutoScrollDownY+","+mAutoScrollUpY+","+mDownY+","+mTouchSlop);

        if (offset != 0) {
            View view = getChildAt(mCurrentPosition - getFirstVisiblePosition());
            if (view != null) {
                // 滚动列表
                setSelectionFromTop(mCurrentPosition, view.getTop() + offset);
//                if (!mScrolling) {
//                    mScrolling = true;
//                    long passed = System.currentTimeMillis() - mLastScrollTime;
//                    postDelayed(mScrollRunnable, passed > 15 ? 15 : 15 - passed);
//                }
            }
        }
    }

    public int dp2px(Context context, float dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mAutoScrollUpY = dp2px(getContext(), 80); // 取得向上滚动的边际，大概为该控件的1/3
        mAutoScrollDownY = h - mAutoScrollUpY; // 取得向下滚动的边际，大概为该控件的2/3
    }

    void exchangeAnimal(int srcPosition, int position, View srcItemView, View itemView){
        if (srcItemView!=null) {
            int height = srcPosition > position ? -srcItemView.getHeight() : srcItemView.getHeight();
            TranslateAnimation animation = new TranslateAnimation(0, 0, height, 0);
            animation.setDuration(200);
            srcItemView.clearAnimation();
            srcItemView.startAnimation(animation);
            srcItemView.setVisibility(View.VISIBLE);
        }

        if (itemView != null) {
            itemView.setVisibility(View.INVISIBLE);
        }
    }

    public void onDrop(int y){

        if (mItemView != null) {
            mItemView.setVisibility(View.VISIBLE);
            if(Math.abs(y - mItemView.getTop()) > mItemView.getHeight() / 5 ){
                AlphaAnimation animation = new AlphaAnimation(0.5f, 1);
                animation.setDuration(150);
                mItemView.clearAnimation();
                mItemView.startAnimation(animation);
            }
            mItemView = null;
        }
        stopDrag();
    }

    public void stopDrag(){
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;

        }
        if (mItemView != null) {
            mItemView.setVisibility(View.VISIBLE);
            mItemView = null;
        }
    }

//
//
//    private void checkExchange(int y){
//        if (mCurrentPosition!=mLastPosition){
//            if (mDragItemListener!=null){
//                if(mDragItemListener.canExchange(mLastPosition, mCurrentPosition)){
//
//                }
//            }
//        }
//
//    }
//
//
//    public interface DragItemListener{
//
//        boolean canExchange(int srcPosition, int position);
//
//        void onExchange(int srcPosition, int position, View srcItemView, View itemView);
//
//        void onRelease(int position, View itemView, int itemViewY, int releaseX, int releaseY);
//
//        boolean canDrag(View itemView, int x, int y);
//
//        void startDrag(int position, View itemView);
//
//        void beforeDrawingCache(View itemView);
//
//        Bitmap afterDrawingCache(View itemView, Bitmap bitmap);
//
//
//    }




}
