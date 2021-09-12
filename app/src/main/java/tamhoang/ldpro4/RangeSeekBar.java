package tamhoang.ldpro4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;

import java.lang.Number;
import java.math.BigDecimal;

public class RangeSeekBar<T extends Number> extends androidx.appcompat.widget.AppCompatImageView {
    public static final int ACTION_POINTER_INDEX_MASK = 65280;
    public static final int ACTION_POINTER_INDEX_SHIFT = 8;
    public static final int ACTION_POINTER_UP = 6;
    public static final int DEFAULT_COLOR = Color.argb(255, 51, 181, 229);
    public static final Integer DEFAULT_MAXIMUM = 100;
    public static final Integer DEFAULT_MINIMUM = 0;
    private static final int DEFAULT_TEXT_DISTANCE_TO_BUTTON_IN_DP = 8;
    private static final int DEFAULT_TEXT_DISTANCE_TO_TOP_IN_DP = 8;
    private static final int DEFAULT_TEXT_SIZE_IN_DP = 14;
    public static final int HEIGHT_IN_DP = 30;
    private static final int INITIAL_PADDING_IN_DP = 8;
    public static final int INVALID_POINTER_ID = 255;
    public static final int TEXT_LATERAL_PADDING_IN_DP = 3;
    private float INITIAL_PADDING;
    private final int LINE_HEIGHT_IN_DP = 1;
    private T absoluteMaxValue;
    private double absoluteMaxValuePrim;
    private T absoluteMinValue;
    private double absoluteMinValuePrim;
    private OnRangeSeekBarChangeListener<T> listener;
    private int mActivePointerId;
    private int mDistanceToTop;
    private float mDownMotionX;
    private boolean mIsDragging;
    private RectF mRect;
    private int mScaledTouchSlop;
    private boolean mSingleThumb;
    private int mTextOffset;
    private int mTextSize;
    private double normalizedMaxValue;
    private double normalizedMinValue;
    private boolean notifyWhileDragging;
    private NumberType numberType;
    private float padding;
    private final Paint paint = new Paint(1);
    private Thumb pressedThumb;
    private final Bitmap thumbDisabledImage = BitmapFactory.decodeResource(getResources(), R.drawable.seek_thumb_disabled);
    private final float thumbHalfHeight;
    private final float thumbHalfWidth;
    private final Bitmap thumbImage = BitmapFactory.decodeResource(getResources(), R.drawable.seek_thumb_normal);
    private final Bitmap thumbPressedImage = BitmapFactory.decodeResource(getResources(), R.drawable.seek_thumb_pressed);
    private final float thumbWidth;

    public interface OnRangeSeekBarChangeListener<T> {
        void onRangeSeekBarValuesChanged(RangeSeekBar<?> rangeSeekBar, T t, T t2);
    }

    /* access modifiers changed from: private */
    public enum Thumb {
        MIN,
        MAX
    }

    public RangeSeekBar(Context context) {
        super(context);
        float width = (float) this.thumbImage.getWidth();
        this.thumbWidth = width;
        this.thumbHalfWidth = width * 0.5f;
        this.thumbHalfHeight = ((float) this.thumbImage.getHeight()) * 0.5f;
        this.normalizedMinValue = 0.0d;
        this.normalizedMaxValue = 1.0d;
        this.pressedThumb = null;
        this.notifyWhileDragging = false;
        this.mActivePointerId = 255;
        init(context, null);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        float width = (float) this.thumbImage.getWidth();
        this.thumbWidth = width;
        this.thumbHalfWidth = width * 0.5f;
        this.thumbHalfHeight = ((float) this.thumbImage.getHeight()) * 0.5f;
        this.normalizedMinValue = 0.0d;
        this.normalizedMaxValue = 1.0d;
        this.pressedThumb = null;
        this.notifyWhileDragging = false;
        this.mActivePointerId = 255;
        init(context, attrs);
    }

    public RangeSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        float width = (float) this.thumbImage.getWidth();
        this.thumbWidth = width;
        this.thumbHalfWidth = width * 0.5f;
        this.thumbHalfHeight = ((float) this.thumbImage.getHeight()) * 0.5f;
        this.normalizedMinValue = 0.0d;
        this.normalizedMaxValue = 1.0d;
        this.pressedThumb = null;
        this.notifyWhileDragging = false;
        this.mActivePointerId = 255;
        init(context, attrs);
    }

    private T extractNumericValueFromAttributes(TypedArray a, int attribute, int defaultValue) {
        TypedValue tv = a.peekValue(attribute);
        if (tv == null) {
            return (T) Integer.valueOf(defaultValue);
        }
        if (tv.type == 4) {
            return (T) Float.valueOf(a.getFloat(attribute, (float) defaultValue));
        }
        return (T) Integer.valueOf(a.getInteger(attribute, defaultValue));
    }

    @SuppressLint("ResourceType")
    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) {
            setRangeToDefaultValues();
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RangeSeekBar, 0, 0);
            setRangeValues(extractNumericValueFromAttributes(a, 1, DEFAULT_MINIMUM.intValue()), extractNumericValueFromAttributes(a, 0, DEFAULT_MAXIMUM.intValue()));
            this.mSingleThumb = a.getBoolean(2, false);
            a.recycle();
        }
        setValuePrimAndNumberType();
        this.INITIAL_PADDING = (float) PixelUtil.dpToPx(context, 8);
        this.mTextSize = PixelUtil.dpToPx(context, 14);
        this.mDistanceToTop = PixelUtil.dpToPx(context, 8);
        this.mTextOffset = this.mTextSize + PixelUtil.dpToPx(context, 8) + this.mDistanceToTop;
        float lineHeight = (float) PixelUtil.dpToPx(context, 1);
        this.mRect = new RectF(this.padding, (((float) this.mTextOffset) + this.thumbHalfHeight) - (lineHeight / 2.0f), ((float) getWidth()) - this.padding, ((float) this.mTextOffset) + this.thumbHalfHeight + (lineHeight / 2.0f));
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void setRangeValues(T minValue, T maxValue) {
        this.absoluteMinValue = minValue;
        this.absoluteMaxValue = maxValue;
        setValuePrimAndNumberType();
    }

    private void setRangeToDefaultValues() {
        this.absoluteMinValue = (T) DEFAULT_MINIMUM;
        this.absoluteMaxValue = (T) DEFAULT_MAXIMUM;
        setValuePrimAndNumberType();
    }

    private void setValuePrimAndNumberType() {
        this.absoluteMinValuePrim = this.absoluteMinValue.doubleValue();
        this.absoluteMaxValuePrim = this.absoluteMaxValue.doubleValue();
        this.numberType = NumberType.fromNumber(this.absoluteMinValue);
    }

    public void resetSelectedValues() {
        setSelectedMinValue(this.absoluteMinValue);
        setSelectedMaxValue(this.absoluteMaxValue);
    }

    public boolean isNotifyWhileDragging() {
        return this.notifyWhileDragging;
    }

    public void setNotifyWhileDragging(boolean flag) {
        this.notifyWhileDragging = flag;
    }

    public T getAbsoluteMinValue() {
        return this.absoluteMinValue;
    }

    public T getAbsoluteMaxValue() {
        return this.absoluteMaxValue;
    }

    public T getSelectedMinValue() {
        return normalizedToValue(this.normalizedMinValue);
    }

    public void setSelectedMinValue(T value) {
        if (0.0d == this.absoluteMaxValuePrim - this.absoluteMinValuePrim) {
            setNormalizedMinValue(0.0d);
        } else {
            setNormalizedMinValue(valueToNormalized(value));
        }
    }

    public T getSelectedMaxValue() {
        return normalizedToValue(this.normalizedMaxValue);
    }

    public void setSelectedMaxValue(T value) {
        if (0.0d == this.absoluteMaxValuePrim - this.absoluteMinValuePrim) {
            setNormalizedMaxValue(1.0d);
        } else {
            setNormalizedMaxValue(valueToNormalized(value));
        }
    }

    public void setOnRangeSeekBarChangeListener(OnRangeSeekBarChangeListener<T> listener2) {
        this.listener = listener2;
    }

    public boolean onTouchEvent(MotionEvent event) {
        OnRangeSeekBarChangeListener<T> onRangeSeekBarChangeListener;
        if (!isEnabled()) {
            return false;
        }
        int action = event.getAction() & 255;
        if (action == 0) {
            int pointerId = event.getPointerId(event.getPointerCount() - 1);
            this.mActivePointerId = pointerId;
            float x = event.getX(event.findPointerIndex(pointerId));
            this.mDownMotionX = x;
            Thumb evalPressedThumb = evalPressedThumb(x);
            this.pressedThumb = evalPressedThumb;
            if (evalPressedThumb == null) {
                return super.onTouchEvent(event);
            }
            setPressed(true);
            invalidate();
            onStartTrackingTouch();
            trackTouchEvent(event);
            attemptClaimDrag();
        } else if (action == 1) {
            if (this.mIsDragging) {
                trackTouchEvent(event);
                onStopTrackingTouch();
                setPressed(false);
            } else {
                onStartTrackingTouch();
                trackTouchEvent(event);
                onStopTrackingTouch();
            }
            this.pressedThumb = null;
            invalidate();
            OnRangeSeekBarChangeListener<T> onRangeSeekBarChangeListener2 = this.listener;
            if (onRangeSeekBarChangeListener2 != null) {
                onRangeSeekBarChangeListener2.onRangeSeekBarValuesChanged(this, getSelectedMinValue(), getSelectedMaxValue());
            }
        } else if (action != 2) {
            if (action == 3) {
                if (this.mIsDragging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate();
            } else if (action == 5) {
                int index = event.getPointerCount() - 1;
                this.mDownMotionX = event.getX(index);
                this.mActivePointerId = event.getPointerId(index);
                invalidate();
            } else if (action == 6) {
                onSecondaryPointerUp(event);
                invalidate();
            }
        } else if (this.pressedThumb != null) {
            if (this.mIsDragging) {
                trackTouchEvent(event);
            } else if (Math.abs(event.getX(event.findPointerIndex(this.mActivePointerId)) - this.mDownMotionX) > ((float) this.mScaledTouchSlop)) {
                setPressed(true);
                invalidate();
                onStartTrackingTouch();
                trackTouchEvent(event);
                attemptClaimDrag();
            }
            if (this.notifyWhileDragging && (onRangeSeekBarChangeListener = this.listener) != null) {
                onRangeSeekBarChangeListener.onRangeSeekBarValuesChanged(this, getSelectedMinValue(), getSelectedMaxValue());
            }
        }
        return true;
    }

    private final void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & 65280) >> 8;
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mDownMotionX = ev.getX(newPointerIndex);
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private final void trackTouchEvent(MotionEvent event) {
        float x = event.getX(event.findPointerIndex(this.mActivePointerId));
        if (Thumb.MIN.equals(this.pressedThumb) && !this.mSingleThumb) {
            setNormalizedMinValue(screenToNormalized(x));
        } else if (Thumb.MAX.equals(this.pressedThumb)) {
            setNormalizedMaxValue(screenToNormalized(x));
        }
    }

    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    /* access modifiers changed from: package-private */
    public void onStartTrackingTouch() {
        this.mIsDragging = true;
    }

    /* access modifiers changed from: package-private */
    public void onStopTrackingTouch() {
        this.mIsDragging = false;
    }

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    public synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
        if (View.MeasureSpec.getMode(widthMeasureSpec) != 0) {
            width = View.MeasureSpec.getSize(widthMeasureSpec);
        }
        int height = this.thumbImage.getHeight() + PixelUtil.dpToPx(getContext(), 30);
        if (View.MeasureSpec.getMode(heightMeasureSpec) != 0) {
            height = Math.min(height, View.MeasureSpec.getSize(heightMeasureSpec));
        }
        setMeasuredDimension(width, height);
    }

    /* access modifiers changed from: protected */
    public synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.setTextSize((float) this.mTextSize);
        this.paint.setStyle(Paint.Style.FILL);
        int colorToUseForButtonsAndHighlightedLine = -7829368;
        this.paint.setColor(-7829368);
        boolean selectedValuesAreDefault = true;
        this.paint.setAntiAlias(true);
        String minLabel = getContext().getString(R.string.demo_min_label);
        String maxLabel = getContext().getString(R.string.demo_max_label);
        float minMaxLabelSize = Math.max(this.paint.measureText(minLabel), this.paint.measureText(maxLabel));
        float minMaxHeight = ((float) this.mTextOffset) + this.thumbHalfHeight + ((float) (this.mTextSize / 3));
        canvas.drawText(minLabel, 0.0f, minMaxHeight, this.paint);
        canvas.drawText(maxLabel, ((float) getWidth()) - minMaxLabelSize, minMaxHeight, this.paint);
        float f = this.INITIAL_PADDING + minMaxLabelSize + this.thumbHalfWidth;
        this.padding = f;
        this.mRect.left = f;
        this.mRect.right = ((float) getWidth()) - this.padding;
        canvas.drawRect(this.mRect, this.paint);
        if (!getSelectedMinValue().equals(getAbsoluteMinValue()) || !getSelectedMaxValue().equals(getAbsoluteMaxValue())) {
            selectedValuesAreDefault = false;
        }
        if (!selectedValuesAreDefault) {
            colorToUseForButtonsAndHighlightedLine = DEFAULT_COLOR;
        }
        this.mRect.left = normalizedToScreen(this.normalizedMinValue);
        this.mRect.right = normalizedToScreen(this.normalizedMaxValue);
        this.paint.setColor(colorToUseForButtonsAndHighlightedLine);
        canvas.drawRect(this.mRect, this.paint);
        if (!this.mSingleThumb) {
            drawThumb(normalizedToScreen(this.normalizedMinValue), Thumb.MIN.equals(this.pressedThumb), canvas, selectedValuesAreDefault);
        }
        drawThumb(normalizedToScreen(this.normalizedMaxValue), Thumb.MAX.equals(this.pressedThumb), canvas, selectedValuesAreDefault);
        if (!selectedValuesAreDefault) {
            this.paint.setTextSize((float) this.mTextSize);
            this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
            int offset = PixelUtil.dpToPx(getContext(), 3);
            String minText = String.valueOf(getSelectedMinValue());
            String maxText = String.valueOf(getSelectedMaxValue());
            float minTextWidth = this.paint.measureText(minText) + ((float) offset);
            float maxTextWidth = this.paint.measureText(maxText) + ((float) offset);
            if (!this.mSingleThumb) {
                canvas.drawText(minText, normalizedToScreen(this.normalizedMinValue) - (minTextWidth * 0.5f), (float) (this.mDistanceToTop + this.mTextSize), this.paint);
            }
            canvas.drawText(maxText, normalizedToScreen(this.normalizedMaxValue) - (0.5f * maxTextWidth), (float) (this.mDistanceToTop + this.mTextSize), this.paint);
        }
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER", super.onSaveInstanceState());
        bundle.putDouble("MIN", this.normalizedMinValue);
        bundle.putDouble("MAX", this.normalizedMaxValue);
        return bundle;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcel) {
        Bundle bundle = (Bundle) parcel;
        super.onRestoreInstanceState(bundle.getParcelable("SUPER"));
        this.normalizedMinValue = bundle.getDouble("MIN");
        this.normalizedMaxValue = bundle.getDouble("MAX");
    }

    private void drawThumb(float screenCoord, boolean pressed, Canvas canvas, boolean areSelectedValuesDefault) {
        Bitmap buttonToDraw;
        if (areSelectedValuesDefault) {
            buttonToDraw = this.thumbDisabledImage;
        } else {
            buttonToDraw = pressed ? this.thumbPressedImage : this.thumbImage;
        }
        canvas.drawBitmap(buttonToDraw, screenCoord - this.thumbHalfWidth, (float) this.mTextOffset, this.paint);
    }

    private Thumb evalPressedThumb(float touchX) {
        boolean minThumbPressed = isInThumbRange(touchX, this.normalizedMinValue);
        boolean maxThumbPressed = isInThumbRange(touchX, this.normalizedMaxValue);
        if (minThumbPressed && maxThumbPressed) {
            return touchX / ((float) getWidth()) > 0.5f ? Thumb.MIN : Thumb.MAX;
        } else if (minThumbPressed) {
            return Thumb.MIN;
        } else {
            if (maxThumbPressed) {
                return Thumb.MAX;
            }
            return null;
        }
    }

    private boolean isInThumbRange(float touchX, double normalizedThumbValue) {
        return Math.abs(touchX - normalizedToScreen(normalizedThumbValue)) <= this.thumbHalfWidth;
    }

    private void setNormalizedMinValue(double value) {
        this.normalizedMinValue = Math.max(0.0d, Math.min(1.0d, Math.min(value, this.normalizedMaxValue)));
        invalidate();
    }

    private void setNormalizedMaxValue(double value) {
        this.normalizedMaxValue = Math.max(0.0d, Math.min(1.0d, Math.max(value, this.normalizedMinValue)));
        invalidate();
    }

    private T normalizedToValue(double normalized) {
        double d = this.absoluteMinValuePrim;
        double v = d + ((this.absoluteMaxValuePrim - d) * normalized);
        NumberType numberType2 = this.numberType;
        double round = (double) Math.round(v * 100.0d);
        Double.isNaN(round);
        return (T) numberType2.toNumber(round / 100.0d);
    }

    private double valueToNormalized(T value) {
        if (0.0d == this.absoluteMaxValuePrim - this.absoluteMinValuePrim) {
            return 0.0d;
        }
        double doubleValue = value.doubleValue();
        double d = this.absoluteMinValuePrim;
        return (doubleValue - d) / (this.absoluteMaxValuePrim - d);
    }

    private float normalizedToScreen(double normalizedCoord) {
        double d = (double) this.padding;
        double width = (double) (((float) getWidth()) - (this.padding * 2.0f));
        Double.isNaN(width);
        Double.isNaN(d);
        return (float) (d + (width * normalizedCoord));
    }

    private double screenToNormalized(float screenCoord) {
        int width = getWidth();
        float f = this.padding;
        if (((float) width) <= f * 2.0f) {
            return 0.0d;
        }
        return Math.min(1.0d, Math.max(0.0d, (double) ((screenCoord - f) / (((float) width) - (f * 2.0f)))));
    }

    /* access modifiers changed from: private */
    public enum NumberType {
        LONG,
        DOUBLE,
        INTEGER,
        FLOAT,
        SHORT,
        BYTE,
        BIG_DECIMAL;

        public static <E extends Number> NumberType fromNumber(E value) throws IllegalArgumentException {
            if (value instanceof Long) {
                return LONG;
            }
            if (value instanceof Double) {
                return DOUBLE;
            }
            if (value instanceof Integer) {
                return INTEGER;
            }
            if (value instanceof Float) {
                return FLOAT;
            }
            if (value instanceof Short) {
                return SHORT;
            }
            if (value instanceof Byte) {
                return BYTE;
            }
            if (value instanceof BigDecimal) {
                return BIG_DECIMAL;
            }
            throw new IllegalArgumentException("Number class '" + value.getClass().getName() + "' is not supported");
        }

        public Number toNumber(double value) {
            switch (AnonymousClass1.$SwitchMap$tamhoang$ldpro4$RangeSeekBar$NumberType[ordinal()]) {
                case 1:
                    return Long.valueOf((long) value);
                case 2:
                    return Double.valueOf(value);
                case 3:
                    return Integer.valueOf((int) value);
                case 4:
                    return Float.valueOf((float) value);
                case 5:
                    return Short.valueOf((short) ((int) value));
                case 6:
                    return Byte.valueOf((byte) ((int) value));
                case 7:
                    return BigDecimal.valueOf(value);
                default:
                    throw new InstantiationError("can't convert " + this + " to a Number object");
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: tamhoang.ldpro4.RangeSeekBar$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$tamhoang$ldpro4$RangeSeekBar$NumberType;

        static {
            int[] iArr = new int[NumberType.values().length];
            $SwitchMap$tamhoang$ldpro4$RangeSeekBar$NumberType = iArr;
            try {
                iArr[NumberType.LONG.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$tamhoang$ldpro4$RangeSeekBar$NumberType[NumberType.DOUBLE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$tamhoang$ldpro4$RangeSeekBar$NumberType[NumberType.INTEGER.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$tamhoang$ldpro4$RangeSeekBar$NumberType[NumberType.FLOAT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$tamhoang$ldpro4$RangeSeekBar$NumberType[NumberType.SHORT.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$tamhoang$ldpro4$RangeSeekBar$NumberType[NumberType.BYTE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$tamhoang$ldpro4$RangeSeekBar$NumberType[NumberType.BIG_DECIMAL.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }
}