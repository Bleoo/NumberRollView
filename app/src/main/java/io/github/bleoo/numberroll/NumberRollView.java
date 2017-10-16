package io.github.bleoo.numberroll;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by bleoo on 2017/10/13.
 */

public class NumberRollView extends View {

    private static final String TAG = "NumberRollView";

    private int number_old;
    private int number;
    private float progress;
    private Paint mPaint;
    public boolean isLiked;

    private OnLikeChangedListener linster;

    public NumberRollView(Context context) {
        super(context);
        initAttr(context, null);
    }

    public NumberRollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public NumberRollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumberRollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, @Nullable AttributeSet attrs) {
        if(attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberRollView);
            number = typedArray.getInt(R.styleable.NumberRollView_number, 0);
            number_old = number;
            typedArray.recycle();
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(45f);
        mPaint.setColor(Color.GRAY);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    minusOne();
                } else {
                    addOne();
                }
                isLiked = !isLiked;
                linster.OnLikeChanged(isLiked);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure");
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) mPaint.measureText(String.valueOf(number)) + getPaddingLeft() + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) (-mPaint.ascent() + mPaint.descent()) * 3 + getPaddingTop() + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    public void draw(Canvas canvas) {
        Log.e(TAG, "draw");
        super.draw(canvas);

        float ascent = -mPaint.ascent() + mPaint.descent();

        String oldStr = String.valueOf(number_old);
        String newStr = String.valueOf(number);

        int diffIndex = 0;
        if (oldStr.length() == newStr.length()) {
            for (int i = 0; i < oldStr.length(); i++) {
                if (oldStr.charAt(i) != newStr.charAt(i)) {
                    diffIndex = i;
                    break;
                }
            }
        }

        float frontWidth = 0;

        if (diffIndex != 0) {
            String frontStr = oldStr.substring(0, diffIndex);

            mPaint.setAlpha(255);
            canvas.drawText(frontStr, frontWidth, ascent * 2, mPaint);

            frontWidth += mPaint.measureText(frontStr);
        }

        String oldBackStr = oldStr.substring(diffIndex);
        String newBackStr = newStr.substring(diffIndex);

        if (number > number_old) {
            mPaint.setAlpha((int) (progress * 255));
            canvas.drawText(oldBackStr, frontWidth, ascent + ascent * progress, mPaint);
            mPaint.setAlpha((int) ((1 - progress) * 255));
            canvas.drawText(newBackStr, frontWidth, ascent * 2 + ascent * progress, mPaint);
        } else {
            mPaint.setAlpha((int) ((1 - progress) * 255));
            canvas.drawText(oldBackStr, frontWidth, ascent * 2 + ascent * progress, mPaint);
            mPaint.setAlpha((int) (progress * 255));
            canvas.drawText(newBackStr, frontWidth, ascent + ascent * progress, mPaint);
        }
    }

    public void addOne() {
        number_old = number;
        number++;
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "progress", 1f, 0);
        animator.setDuration(500);
        animator.start();
    }

    public void minusOne() {
        number_old = number;
        number--;
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "progress", 0, 1f);
        animator.setDuration(500);
        animator.start();
    }

    public void setNumber(int number){
        this.number_old = this.number;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public void setOnLikeChangedListener(OnLikeChangedListener linster) {
        this.linster = linster;
    }

    interface OnLikeChangedListener {
        void OnLikeChanged(boolean isLiked);
    }
}
