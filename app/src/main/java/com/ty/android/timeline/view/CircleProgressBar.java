package com.ty.android.timeline.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.ProgressBar;

import com.ty.android.timeline.R;
import com.ty.android.timeline.utils.Tool;

import static android.R.attr.width;
import static android.content.ContentValues.TAG;

/**
 * Created by Android on 2017/4/1.
 */

public class CircleProgressBar extends ProgressBar{

    //默认圆的背景色
    public static final int DEFAULT_CIRCLE_COLOR =0xff00cccc;
    //默认进度的颜色
    public static final int DEFAULT_PROGRESS_COLOR = 0xff00CC66;
    //默认文字的颜色
    public static final int DEFAULT_TEXT_COLOR = 0xffffffff;
    //默认文字的大小
    public static final int DEFAULT_TEXT_SIZE = 18;
    public static final int DEFAULT_RIPPLE_TOPHEIGHT = 10;

    private Context mContext;

    private Canvas mPaintCanvas;
    private Bitmap mBitmap;

    //画圆的画笔
    private Paint mCirclePaint;
    //画圆的画笔的颜色
    private int mCircleColor = 0xff00CCCC;

    //画进度的画笔
    private Paint mProgressPaint;
    //画进度的画笔的颜色
    private int mProgressColor = 0xff00CC66;
    //画进度的path
    private Path mProgressPath;
    private int mRippleTop = 10;

    //进度文字的画笔
    private Paint mTextPaint;
    //进度文字的颜色
    private int mTextColor = 0xffffffff;
    private int mTextSize = 18;
    //当前进度
    private volatile int mCurrentProgress = 0;
    //最大进度
    private int mMaxProgress = 100;
    //目标进度，也就是双击时处理任务的进度，会影响曲线的振幅
    private float mTargetProgress = 70f;
    private GestureDetector mGestureDetector;
    //正在进行单击动画的标志
    private boolean isSingleTapAnimation;
    //单击动画进行的次数，默认为50
    private int mSingleTapAnimationCount = 50;


    private Paint paint;

    public CircleProgressBar(Context context) {
        this(context,null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        getAttrsValue(attrs);

        initPaints();
        mProgressPath = new Path();

        startDoubleTapAnimation();
    }

    public void setmTargetProgress(float mTargetProgress) {
        this.mTargetProgress = mTargetProgress;
        invalidate();
    }

    private void initPaints() {
        mCirclePaint = new Paint();
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);

        mProgressPaint = new Paint();
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setDither(true);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(mTextSize);
    }

    private void getAttrsValue(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        mCircleColor = ta.getColor(R.styleable.CircleProgressBar_circle_color,DEFAULT_CIRCLE_COLOR);
        mProgressColor = ta.getColor(R.styleable.CircleProgressBar_progress_color,DEFAULT_PROGRESS_COLOR);
        mTextColor = ta.getColor(R.styleable.CircleProgressBar_text_color,DEFAULT_TEXT_COLOR);
        mTextSize = (int) ta.getDimension(R.styleable.CircleProgressBar_text_size, Tool.sp2px(mContext,DEFAULT_TEXT_SIZE));
        mRippleTop = (int) ta.getDimension(R.styleable.CircleProgressBar_top_height,Tool.dp2px(mContext,DEFAULT_RIPPLE_TOPHEIGHT));
        ta.recycle();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = Tool.dp2px(mContext,160);
        int height = width;
        setMeasuredDimension(width,height);


        mBitmap = Bitmap.createBitmap(width-getPaddingLeft()-getPaddingRight(),height-getPaddingBottom()-getPaddingTop(), Bitmap.Config.ARGB_8888);
        mPaintCanvas = new Canvas(mBitmap);
    }



    @Override
    protected synchronized void onDraw(Canvas canvas) {
        float ratio = getProgress()*1.0f/getMax();

        int width = getWidth()-getPaddingLeft()-getPaddingRight();
        int height = getHeight()-getPaddingTop()-getPaddingBottom();

        //画外圆
        mPaintCanvas.drawCircle(width/2,height/2,height/2,mCirclePaint);

        //画进度
        mProgressPath.reset();
        int rightTop = (int) ((1-ratio)*height);
        mProgressPath.moveTo(width,rightTop);
        mProgressPath.lineTo(width,height);
        mProgressPath.lineTo(0,height);
        mProgressPath.lineTo(0,rightTop);

        int count = (int) Math.ceil(height*1.0f/(mRippleTop*4));

        float top = (mTargetProgress-getProgress())*1.0f/mTargetProgress* mRippleTop;
        for(int i=0; i<count; i++) {
            mProgressPath.rQuadTo(mRippleTop, -top, 2 * mRippleTop, 0);
            mProgressPath.rQuadTo(mRippleTop, top, 2 * mRippleTop, 0);
        }
            mProgressPath.close();
            mPaintCanvas.drawPath(mProgressPath,mProgressPaint);

            //画进度文字
            String text = ((int)(ratio*100))+"%";
            //获得文字的宽度
            float textWidth = mTextPaint.measureText(text);
            Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
            //descent+ascent为负数，所以是减而不是加
            float baseLine = height*1.0f/2 - (metrics.descent+metrics.ascent)/2;
            mPaintCanvas.drawText(text,width/2-textWidth/2,baseLine,mTextPaint);

            canvas.drawBitmap(mBitmap,0,0,null);



    }



    private Handler doubleTapHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    /**
     * 实现双击动画
     */
    private void startDoubleTapAnimation() {
        setProgress(0);
        doubleTapHandler.postDelayed(doubleTapRunnable,60);
    }

    //双击处理线程，隔60ms发送一次数据
    private Runnable doubleTapRunnable = new Runnable() {
        @Override
        public void run() {
            if(getProgress() < mTargetProgress) {
                invalidate();
                setProgress(getProgress()+1);
                doubleTapHandler.postDelayed(doubleTapRunnable,60);
            } else {
                doubleTapHandler.removeCallbacks(doubleTapRunnable);
            }
        }
    };

    public void update(){
        invalidate();
    }
}
