package com.legency.madrit.mkanalogclockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Calendar;

/**
 * Created by Madrit on 17/10/2017.
 */

public class MKAnalogClockView extends View {

    //customisation
    ClockType mClockType = ClockType.analog;
    //analog
    boolean alwaysMovingHands = true;
    //colors, size for geometric parts
    // 1 - circle
    private boolean circleEnabled = true;
    private int circleColor = Color.BLACK;
    private int circleStroke = 5;
    private Paint.Style circleStyle = Paint.Style.STROKE;
    private int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    // 2 - center
    private int centerColor = Color.BLACK;
    private int centerSize = 10;
    private Paint.Style centerStyle = Paint.Style.FILL;

    // 3 numerals
    private int fontSize = 13;
    private int numeralsColor = Color.GRAY;
    private int numeralSpacingToCircle = 30;

    // 4 seconds line along the circle
    private boolean secLinesEnabled = true;
    private int secLineHeight = 10;
    private int secLineColor = Color.BLACK;
    private int secLineStroke = 2;
    private Paint.Style secLineStyle = Paint.Style.FILL;
    //drawing data
    private int height, width = 0;
    private int padding = 10;

    //hands
    private int handHourPadding = 60;
    private int hourHandColor = Color.BLACK;
    private Paint.Style hourHandStyle = Paint.Style.FILL;
    private int hourHandStroke = 4;

    private int handMinuteTruncation = 40;
    private int minuteHandPadding = Color.BLACK;
    private Paint.Style minuteHandStyle = Paint.Style.FILL;
    private int minuteHandStroke = 3;

    private boolean secondsEnabled = true;
    private int handSecondsPadding = 20;
    private int secondHandColor = Color.GRAY;
    private Paint.Style secondHandStyle = Paint.Style.FILL;
    private int secondHandStroke = 2;


    private int diameter = 0;
    private int radius = 0;

    private boolean isInit = false;
    private Paint paint;
    private Rect rect = new Rect();

    public MKAnalogClockView(Context context) {
        super(context);
    }

    public MKAnalogClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MKAnalogClockView);
        readAttributes(array);
        array.recycle();

    }

    public MKAnalogClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MKAnalogClockView);
        readAttributes(array);
        array.recycle();

    }

    public MKAnalogClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                             int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MKAnalogClockView,
                defStyleAttr, defStyleRes);
        readAttributes(array);
        array.recycle();

    }

    // reading attributes

    private void readAttributes(TypedArray array){
        //getting clock type
        int clockType = array.getInt(R.styleable.MKAnalogClockView_clockType, 0);
        if (clockType == 0){
            this.mClockType = ClockType.analog;
        }else{
            this.mClockType = ClockType.digital;
        }

        // analog values
        this.alwaysMovingHands = array.getBoolean(R.styleable.MKAnalogClockView_alwaysMovingHands, false);
        // 1 circle
        this.circleEnabled = array.getBoolean(R.styleable.MKAnalogClockView_circleEnabled, true);
        this.circleColor = array.getColor(R.styleable.MKAnalogClockView_circleColor, Color.BLACK);
        this.circleStroke = array.getInteger(R.styleable.MKAnalogClockView_circleStroke, 3);
        int circleStyle = array.getInt(R.styleable.MKAnalogClockView_circlePaintStyle, 1);
        if (circleStyle == 0){
            this.circleStyle = Paint.Style.FILL;
        }else if (circleStyle == 1){
            this.circleStyle = Paint.Style.STROKE;
        }else if (circleStyle == 2){
            this.circleStyle = Paint.Style.FILL_AND_STROKE;
        }

        //2 center point
        this.centerColor = array.getColor(R.styleable.MKAnalogClockView_centerColor, Color.BLACK);
        this.centerSize = array.getInteger(R.styleable.MKAnalogClockView_centerSize, 10);
        int paintStyle = array.getInteger(R.styleable.MKAnalogClockView_centerPaintStyle, 0);
        if (paintStyle == 0){
            this.centerStyle = Paint.Style.FILL;
        }else if (paintStyle == 1){
            this.centerStyle = Paint.Style.STROKE;
        }else if (paintStyle == 2){
            this.centerStyle = Paint.Style.FILL_AND_STROKE;
        }

        //3 numerals
        this.fontSize = array.getDimensionPixelSize(R.styleable.MKAnalogClockView_numeralsFontSize, 13);
        this.numeralsColor = array.getColor(R.styleable.MKAnalogClockView_numeralsColor, Color.GRAY);
        this.numeralSpacingToCircle = array.getDimensionPixelSize(R.styleable.MKAnalogClockView_numeralsSpacingToCircle, 20);

        // 4 secondsLineAlong the circle
        this.secLinesEnabled = array.getBoolean(R.styleable.MKAnalogClockView_secLinesDecorationEnabled, false);
        this.secLineHeight = (int) array.getDimension(R.styleable.MKAnalogClockView_secLinesDecorationHeight, 10);
        this.secLineColor = array.getColor(R.styleable.MKAnalogClockView_secLineDecorationColor, Color.BLACK);
        this.secLineStroke = array.getInteger(R.styleable.MKAnalogClockView_secLineStroke, 2);
        int secLineStyle = array.getInt(R.styleable.MKAnalogClockView_secLineDecorationPaintStyle, 0);
        if (secLineStyle == 0){
            this.secLineStyle = Paint.Style.FILL;
        }else if (secLineStyle == 1){
            this.secLineStyle = Paint.Style.STROKE;
        }else if (secLineStyle == 2){
            this.secLineStyle = Paint.Style.FILL_AND_STROKE;
        }

        //hour Hand
        this.handHourPadding = array.getDimensionPixelSize(R.styleable.MKAnalogClockView_handHourPadding, 60);
        this.hourHandColor = array.getColor(R.styleable.MKAnalogClockView_handHourColor, Color.BLACK);
        this.hourHandStroke = array.getInteger(R.styleable.MKAnalogClockView_handHourStroke, 4);
        int handHourStyle = array.getInteger(R.styleable.MKAnalogClockView_handHourPaintStyle, 0);
        if (handHourStyle == 0){
            this.hourHandStyle = Paint.Style.FILL;
        }else if (handHourStyle == 1){
            this.hourHandStyle = Paint.Style.STROKE;
        }else{
            this.hourHandStyle = Paint.Style.FILL_AND_STROKE;
        }

        //Minutes Hand
        this.handMinuteTruncation = array.getDimensionPixelSize(R.styleable.MKAnalogClockView_handMinutesPadding, 40);
        this.minuteHandPadding = array.getColor(R.styleable.MKAnalogClockView_handMinutesColor, Color.BLACK);
        this.minuteHandStroke = array.getInteger(R.styleable.MKAnalogClockView_handMinutesStroke, 3);
        int handMinuteStyle = array.getInteger(R.styleable.MKAnalogClockView_handMinutesPaintStyle, 0);
        if (handMinuteStyle == 0){
            this.minuteHandStyle = Paint.Style.FILL;
        }else if (handMinuteStyle == 1){
            this.minuteHandStyle = Paint.Style.STROKE;
        }else{
            this.minuteHandStyle = Paint.Style.FILL_AND_STROKE;
        }

        //Seconds Hand
        this.secondsEnabled = array.getBoolean(R.styleable.MKAnalogClockView_secondsEnabled, true);
        this.handSecondsPadding = array.getDimensionPixelSize(R.styleable.MKAnalogClockView_handSecondsPadding, 20);
        this.secondHandColor = array.getColor(R.styleable.MKAnalogClockView_handSecondsColor, Color.GRAY);
        this.secondHandStroke = array.getInteger(R.styleable.MKAnalogClockView_handSecondsStroke, 2);
        int handSecondsStyle = array.getInteger(R.styleable.MKAnalogClockView_handSecondsPaintStyle, 0);
        if (handSecondsStyle == 0){
            this.secondHandStyle = Paint.Style.FILL;
        }else if (handSecondsStyle == 1){
            this.secondHandStyle = Paint.Style.STROKE;
        }else{
            this.secondHandStyle = Paint.Style.FILL_AND_STROKE;
        }
    }

    private void initClock(){
        height = getHeight();
        width = getWidth();
        diameter = Math.min(width, height);
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, fontSize,
                getResources().getDisplayMetrics());

        padding = Math.max(Math.max(getPaddingBottom(), getPaddingTop()),
                Math.max(getPaddingStart(), getPaddingEnd()));
        radius = diameter / 2 - padding;
        paint = new Paint();
        isInit = true;

        // fixing user insert data
        numeralSpacingToCircle += secLineHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit){
            initClock();
        }
        canvas.drawColor(Color.WHITE);
        if (circleEnabled) {
            drawCircle(canvas);
        }
        drawCenter(canvas);
        drawNumeral(canvas);
        drawHands(canvas);
        if (secLinesEnabled) {
            drawSecondsLines(canvas);
        }
        invalidate();
    }

    private void drawCenter(Canvas canvas) {
        paint.setColor(centerColor);
        paint.setStyle(centerStyle);
        canvas.drawCircle((diameter / 2), (diameter / 2), centerSize, paint);
    }

    private void drawCircle(Canvas canvas) {
        paint.reset();
        paint.setColor(circleColor);
        paint.setStrokeWidth(circleStroke);
        paint.setStyle(circleStyle);
        paint.setAntiAlias(true);
        canvas.drawCircle((diameter / 2), (diameter / 2), (radius) - circleStroke, paint);
    }

    //numbers in a an analog clock
    private void drawNumeral(Canvas canvas) {
        paint.setColor(numeralsColor);
        paint.setTextSize(fontSize);
        for (int number : numbers){
            String tmp = String.valueOf(number);
            paint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = (Math.PI / 6 * (number - 3));
            int x = (int) ((diameter / 2) + Math.cos(angle) * (radius - numeralSpacingToCircle - 5) - (rect.width() / 2));
            int y = (int) ((diameter / 2) + Math.sin(angle) * (radius - numeralSpacingToCircle - 5) + (rect.height() / 2));
            canvas.drawText(tmp, x, y, paint);
        }
    }

    //lines of seconds in the analogClock's circle
    void drawSecondsLines(Canvas canvas) {
        paint.setColor(secLineColor);
        paint.setStyle(secLineStyle);
        paint.setAntiAlias(true);
        int radiusWithOffset = radius - 2;
        for (int i = 1; i <= 60; i++) {
            double angle = Math.PI * i / 30 - Math.PI / 2;
            float xStart = (float) ((diameter / 2) + Math.cos(angle) * radiusWithOffset);
            float yStart = (float) ((diameter / 2) + Math.sin(angle) * radiusWithOffset);
            int secHeight = secLineHeight;
            if (contains(i)) {
                secHeight += 5;
                paint.setStrokeWidth(secLineStroke + 2);

            }else{
                paint.setStrokeWidth(secLineStroke);
            }
            float x = (float) ((diameter / 2) + Math.cos(angle) * (radiusWithOffset - secHeight));
            float y = (float) ((diameter / 2) + Math.sin(angle) * (radiusWithOffset - secHeight));
            canvas.drawLine(xStart, yStart, x, y, paint);
        }
    }

    private void drawHands(Canvas canvas) {
        Calendar cal = Calendar.getInstance();
        if (!alwaysMovingHands) {
            // in case we don't want moving hands to move to be dynamically
            float hour = cal.get(Calendar.HOUR_OF_DAY);
            hour = hour > 12 ? hour - 12 : hour;
            drawHand(canvas, (hour + cal.get(Calendar.MINUTE) / 60) * 5f, HandType.hourHand);
            drawHand(canvas, cal.get(Calendar.MINUTE), HandType.minuteHand);
            if (secondsEnabled) {
                drawHand(canvas, cal.get(Calendar.SECOND), HandType.secondsHand);
            }
        }else {
            drawHoursHands(canvas, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
            drawMinutesHands(canvas, cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
            if (secLinesEnabled) {
                drawSecondsHand(canvas, cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND));
            }
        }
    }

    private void drawHand(Canvas canvas, double loc, HandType handType){
        double angle = Math.PI * loc / 30 - Math.PI / 2;
        int handRadius = 0;

        switch (handType){
            case hourHand:
                handRadius = radius - handHourPadding - numeralSpacingToCircle;
                paint.setColor(hourHandColor);
                paint.setStrokeWidth(hourHandStroke);
                paint.setStyle(hourHandStyle);
                break;
            case minuteHand:
                handRadius = radius - handMinuteTruncation - numeralSpacingToCircle;
                paint.setColor(minuteHandPadding);
                paint.setStrokeWidth(minuteHandStroke);
                paint.setStyle(minuteHandStyle);
                break;
            case secondsHand:
                handRadius = radius - handMinuteTruncation - numeralSpacingToCircle;
                paint.setColor(secondHandColor);
                paint.setStrokeWidth(secondHandStroke);
                paint.setStyle(secondHandStyle);
                break;
        }

        canvas.drawLine(diameter / 2, diameter /2 ,
                (float)((diameter / 2) + Math.cos(angle) * handRadius),
                (float)((diameter / 2) + Math.sin(angle) * handRadius),
                paint);
    }

    void drawHoursHands(Canvas canvas, double hour, double min){
        double hourAngle = ((Math.PI*12) * ((hour * 60) + min) / 360 - (Math.PI / 2)*12)/12;
        int handRadius = radius - handHourPadding - numeralSpacingToCircle;

        paint.setColor(hourHandColor);
        paint.setStrokeWidth(hourHandStroke);
        paint.setStyle(hourHandStyle);

        canvas.drawLine(diameter / 2, diameter /2 ,
                (float)((diameter / 2) + Math.cos(hourAngle) * handRadius),
                (float)((diameter / 2) + Math.sin(hourAngle) * handRadius),
                paint);
    }

    void drawMinutesHands(Canvas canvas, double min, double sec){
        double minAngle = ((Math.PI*60) * ((min * 60) + sec) / 1800 - (Math.PI / 2)*60)/60;
        int handRadius = radius - handMinuteTruncation - numeralSpacingToCircle;

        paint.setColor(minuteHandPadding);
        paint.setStrokeWidth(minuteHandStroke);
        paint.setStyle(minuteHandStyle);

        canvas.drawLine(diameter / 2, height /2 ,
                (float)((diameter / 2) + Math.cos(minAngle) * handRadius),
                (float)((diameter / 2) + Math.sin(minAngle) * handRadius),
                paint);
    }

    void drawSecondsHand(Canvas canvas, double second ,double mil){
        double secAngle = ((Math.PI*1000) * ((second * 1000) + mil) / 30000 - (Math.PI / 2)*1000)/1000;
        int handRadius = radius - handSecondsPadding - numeralSpacingToCircle;

        paint.setColor(secondHandColor);
        paint.setStrokeWidth(secondHandStroke);
        paint.setStyle(secondHandStyle);

        canvas.drawLine(diameter / 2, height /2 ,
                (float)((diameter / 2) + Math.cos(secAngle) * handRadius),
                (float)((diameter / 2) + Math.sin(secAngle) * handRadius),
                paint);
    }


    boolean contains(int number){
        for (int sec : numbers){
            if (sec * 5 == number){
                return true;
            }
        }
        return false;
    }

    public ClockType getmClockType() {
        return mClockType;
    }

    public void setmClockType(ClockType mClockType) {
        this.mClockType = mClockType;
    }

    public boolean isAlwaysMovingHands() {
        return alwaysMovingHands;
    }

    public void setAlwaysMovingHands(boolean alwaysMovingHands) {
        this.alwaysMovingHands = alwaysMovingHands;
    }

    public boolean isCircleEnabled() {
        return circleEnabled;
    }

    public void setCircleEnabled(boolean circleEnabled) {
        this.circleEnabled = circleEnabled;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public int getCircleStroke() {
        return circleStroke;
    }

    public void setCircleStroke(int circleStroke) {
        this.circleStroke = circleStroke;
    }

    public Paint.Style getCircleStyle() {
        return circleStyle;
    }

    public void setCircleStyle(Paint.Style circleStyle) {
        this.circleStyle = circleStyle;
    }

    public int getCenterColor() {
        return centerColor;
    }

    public void setCenterColor(int centerColor) {
        this.centerColor = centerColor;
    }

    public int getCenterSize() {
        return centerSize;
    }

    public void setCenterSize(int centerSize) {
        this.centerSize = centerSize;
    }

    public Paint.Style getCenterStyle() {
        return centerStyle;
    }

    public void setCenterStyle(Paint.Style centerStyle) {
        this.centerStyle = centerStyle;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getNumeralsColor() {
        return numeralsColor;
    }

    public void setNumeralsColor(int numeralsColor) {
        this.numeralsColor = numeralsColor;
    }

    public int getNumeralSpacingToCircle() {
        return numeralSpacingToCircle;
    }

    public void setNumeralSpacingToCircle(int numeralSpacingToCircle) {
        this.numeralSpacingToCircle = numeralSpacingToCircle;
    }

    public boolean isSecLinesEnabled() {
        return secLinesEnabled;
    }

    public void setSecLinesEnabled(boolean secLinesEnabled) {
        this.secLinesEnabled = secLinesEnabled;
    }

    public int getSecLineHeight() {
        return secLineHeight;
    }

    public void setSecLineHeight(int secLineHeight) {
        this.secLineHeight = secLineHeight;
    }

    public int getSecLineColor() {
        return secLineColor;
    }

    public void setSecLineColor(int secLineColor) {
        this.secLineColor = secLineColor;
    }

    public int getSecLineStroke() {
        return secLineStroke;
    }

    public void setSecLineStroke(int secLineStroke) {
        this.secLineStroke = secLineStroke;
    }

    public Paint.Style getSecLineStyle() {
        return secLineStyle;
    }

    public void setSecLineStyle(Paint.Style secLineStyle) {
        this.secLineStyle = secLineStyle;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public void setWidth(int width) {
        this.width = width;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public int getHandHourPadding() {
        return handHourPadding;
    }

    public void setHandHourPadding(int handHourPadding) {
        this.handHourPadding = handHourPadding;
    }

    public int getHourHandColor() {
        return hourHandColor;
    }

    public void setHourHandColor(int hourHandColor) {
        this.hourHandColor = hourHandColor;
    }

    public Paint.Style getHourHandStyle() {
        return hourHandStyle;
    }

    public void setHourHandStyle(Paint.Style hourHandStyle) {
        this.hourHandStyle = hourHandStyle;
    }

    public int getHourHandStroke() {
        return hourHandStroke;
    }

    public void setHourHandStroke(int hourHandStroke) {
        this.hourHandStroke = hourHandStroke;
    }

    public int getHandMinuteTruncation() {
        return handMinuteTruncation;
    }

    public void setHandMinuteTruncation(int handMinuteTruncation) {
        this.handMinuteTruncation = handMinuteTruncation;
    }

    public int getMinuteHandPadding() {
        return minuteHandPadding;
    }

    public void setMinuteHandPadding(int minuteHandPadding) {
        this.minuteHandPadding = minuteHandPadding;
    }

    public Paint.Style getMinuteHandStyle() {
        return minuteHandStyle;
    }

    public void setMinuteHandStyle(Paint.Style minuteHandStyle) {
        this.minuteHandStyle = minuteHandStyle;
    }

    public int getMinuteHandStroke() {
        return minuteHandStroke;
    }

    public void setMinuteHandStroke(int minuteHandStroke) {
        this.minuteHandStroke = minuteHandStroke;
    }

    public boolean isSecondsEnabled() {
        return secondsEnabled;
    }

    public void setSecondsEnabled(boolean secondsEnabled) {
        this.secondsEnabled = secondsEnabled;
    }

    public int getHandSecondsPadding() {
        return handSecondsPadding;
    }

    public void setHandSecondsPadding(int handSecondsPadding) {
        this.handSecondsPadding = handSecondsPadding;
    }

    public int getSecondHandColor() {
        return secondHandColor;
    }

    public void setSecondHandColor(int secondHandColor) {
        this.secondHandColor = secondHandColor;
    }

    public Paint.Style getSecondHandStyle() {
        return secondHandStyle;
    }

    public void setSecondHandStyle(Paint.Style secondHandStyle) {
        this.secondHandStyle = secondHandStyle;
    }

    public int getSecondHandStroke() {
        return secondHandStroke;
    }

    public void setSecondHandStroke(int secondHandStroke) {
        this.secondHandStroke = secondHandStroke;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    //enums
    private enum HandType{
        hourHand,
        minuteHand,
        secondsHand
    }

    private enum ClockType{
        analog,
        digital
    }
}
