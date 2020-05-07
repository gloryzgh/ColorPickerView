package com.ghzhang.colorpickerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColorPickerView extends View {
    public static String TAG = ColorPickerView.class.getName();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float touchY;
    private float touchX;
    private int current;
    private int lastChoice = -1;
    private boolean mCPVcilckable;
    private ColorSelectListener mColorSelectListener;

    private static final int[] colors = {
            Color.parseColor("#FFFFFF"),
            Color.parseColor("#000000"),
            Color.parseColor("#00DBB4"),
            Color.parseColor("#FF0000"),
            Color.parseColor("#FF7600"),
            Color.parseColor("#FDFF00"),
            Color.parseColor("#00AD12"),
            Color.parseColor("#0041FF"),
            Color.parseColor("#5B2A92"),
            Color.parseColor("#8D0084"),
            Color.parseColor("#DA007D"),
            Color.parseColor("#E84057"),
    };

    public int rectMargin,rectWidth,strokeWidth;
    public int RECT_DEFAULT_MARGIN =10;
    public int RECT_STROKE_WIDTH =5;
    private List<Rect> list = new ArrayList<>();

    public ColorPickerView(Context context) {
        super(context);
        initColorPickerView();
    }
    public ColorPickerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }
    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerView, defStyleAttr, 0);
        rectMargin = a.getDimensionPixelSize(R.styleable.ColorPickerView_cpv_margin, RECT_DEFAULT_MARGIN);
        strokeWidth = a.getDimensionPixelSize(R.styleable.ColorPickerView_cpv_stroke_with, RECT_STROKE_WIDTH);
        mCPVcilckable = a.getBoolean(R.styleable.ColorPickerView_cpv_clickable, true);
        a.recycle();
        initColorPickerView();
    }
    private void initColorPickerView() {
        mSelectPaint.setColor(Color.BLUE);
        mSelectPaint.setStyle(Paint.Style.STROKE);
        mSelectPaint.setStrokeWidth(strokeWidth);
        for (int i = 0; i < colors.length; i++) {
            Rect rect =  new Rect();
            list.add(rect);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw   : " );
        rectWidth = getWidth() / 12;
        for (int i = 0; i < colors.length; i++) {
            mPaint.setColor(colors[i]);
            Rect rect = list.get(i);
//            Log.d(TAG, "onDraw   : "+ i );
            if (i == current) {
                rect.set(rectWidth*i,0,rectWidth*i+rectWidth, getHeight());
                canvas.drawRect(rect,mPaint);
                rect.set(rectWidth*i+ strokeWidth/2, strokeWidth / 2,rectWidth*i+rectWidth- strokeWidth/2, getHeight()-strokeWidth/2);
                canvas.drawRect(rect,mSelectPaint);
            } else {
                rect.set(rectWidth*i,rectMargin,rectWidth*i+rectWidth, getHeight()-rectMargin);
                canvas.drawRect(rect,mPaint);
            }
        }
    }


    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchHoverEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mCPVcilckable) {
            return false;
        }
        current = -1;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                touchY = ev.getY();
                touchX = ev.getX();
                current = (int) Math.floor(touchX/rectWidth);

                Log.d(TAG, "move   : "+ touchX   + "           当前选中的是   ：  "   + current+ "           上次选中的是   ：  "   + lastChoice);
                if (lastChoice != current) {
                    invalidate();
                    Log.d(TAG, "move   : " + "           invalidate  ：  "   );
                    lastChoice = current;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                current = (int) Math.floor(touchX/rectWidth);
                if (mColorSelectListener != null) {
                    mColorSelectListener.onColorChange(colors[current]);
                }
                break;

            default:
                break;

        }

        return true;

    }

    public void setClickable(boolean isClickable) {
        mCPVcilckable = isClickable;
    }

    public interface ColorSelectListener {
        void onColorChange(int currentValue);
    }

    public void setOnColorSelectListener(ColorSelectListener colorSelectListener) {
        mColorSelectListener = colorSelectListener;
    }



}
