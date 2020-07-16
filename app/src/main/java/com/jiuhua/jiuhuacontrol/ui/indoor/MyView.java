package com.jiuhua.jiuhuacontrol.ui.indoor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.StringTokenizer;

import static android.view.View.MeasureSpec.getSize;

public class MyView extends View {

    private float screenWidth;
    private float screenHeight;  //转化成内部的变量不容易理解
    private int clickedX = 0;//被点击的点的坐标（格子数）
    private int clickedY = 0;
    private Paint linePaint;
    private Paint linePaint1;
    private Paint textPaint;
    private Paint textPaint1;
    private String period_S;
    private int[][] period = new int[7][30];
    private Paint rectPaint;


    public MyView(Context context, AttributeSet attrs) { //这个attrs参数，如何把布局传进来？？
        super(context, attrs);
        initPaint();  //在构造参数里面初始化画笔
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth = getSize(widthMeasureSpec);  //获取屏幕区域的宽度
        screenHeight = (getSize(widthMeasureSpec) - 60) * 3; //设置了自定义控件的高度
        setMeasuredDimension((int) screenWidth, (int) screenHeight);//并且保存才能够提供给父控件。
        Log.e("MyView----->", "2  " + screenWidth + "  " + screenHeight);

    }

    private void initPaint() {
        linePaint = new Paint();     //画线的笔
        linePaint.setColor(Color.GRAY);//灰色
        linePaint.setStyle(Paint.Style.FILL);//填充满，实线画笔
        linePaint.setAntiAlias(true);//抗锯齿
        linePaint.setStrokeWidth(1.0f);//宽度

        linePaint1 = new Paint();
        linePaint1.setColor(Color.GRAY);
        linePaint1.setStyle(Paint.Style.FILL);
        linePaint1.setAntiAlias(true);
        linePaint1.setStrokeWidth(10.0f);

        textPaint = new Paint();       //时间文字的笔
        textPaint.setColor(Color.GRAY);
        textPaint.setTextAlign(Paint.Align.CENTER);//文字在中心
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(40);

        textPaint1 = new Paint();       //写时间段文字的笔
        textPaint1.setColor(Color.BLACK);
        textPaint1.setTextAlign(Paint.Align.CENTER);//文字在中心
        textPaint1.setAntiAlias(true);
        textPaint1.setTextSize(35);

        rectPaint = new Paint();  //画 时间块的矩形 的笔
        rectPaint.setColor(Color.argb(255, 0, 255, 0));
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setAntiAlias(true);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onDraw(Canvas canvas) {
        float square = (screenWidth - 60) / 8;  //每个小方格的宽度

        //画横线
        for (int i = 0; i <= 24; i++) {
            canvas.drawLine(30, square * i, screenWidth - 30, square * i, linePaint);
            canvas.drawText(String.format("%d:00", i), 80, square * i, textPaint);
        }
        //画竖线
        for (int i = 1; i <= 8; i++) {
            canvas.drawLine(square * i + 30, 0, square * i + 30, square * 24, linePaint);
        }
        //画时间段的块，时段名称，设置温度
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                String timeFrame = null;//这个变量是时间端的名称
                if (j == 0) {
                    rectPaint.setColor(Color.LTGRAY);
                    timeFrame = "时段一";
                }
                if (j == 1) {
                    rectPaint.setColor(Color.CYAN);
                    timeFrame = "时段二";
                }
                if (j == 2) {
                    rectPaint.setColor(Color.LTGRAY);
                    timeFrame = "时段三";
                }
                if (j == 3) {
                    rectPaint.setColor(Color.GREEN);
                    timeFrame = "时段四";
                }
                if (j == 4) {
                    rectPaint.setColor(Color.YELLOW);
                    timeFrame = "时段五";
                }
                if (j == 5) {
                    rectPaint.setColor(Color.GRAY);
                    timeFrame = "时段六";
                }
                //画时间段的块
                canvas.drawRoundRect(square * (i + 1) + 30,
                        period[i][j * 5] * square + (period[i][j * 5 + 1]) * square / 60,
                        square * (i + 2) + 30,
                        period[i][j * 5 + 2] * square + period[i][j * 5 + 3] * square / 60,
                        20,
                        20,
                        rectPaint);
                //计算机的除法是取整“/”和取余数“%”，所以先乘为大数在取整60

                if (!(period[i][j * 5 + 4] == 0)) {  //判断一下，去除未设置，0，的干扰
                    //写时间段的名称
                    canvas.drawText(timeFrame, square * (i + 1) + 90,
                            period[i][j * 5] * square + period[i][j * 5 + 1] * square / 60 + 50, textPaint1);
                    //写 设置的温度
                    canvas.drawText(String.format("%d C", period[i][j * 5 + 4]), square * (i + 1) + 80,
                            period[i][j * 5] * square + period[i][j * 5 + 1] * square / 60 + 90, textPaint1);
                }
            }
        }
        //画“ + ” 号
        if (clickedX != 0) {
            //十字的横线
            canvas.drawLine((float) (30 + clickedX * square + square / 6), (float) ((clickedY + 0.5) * square),
                    (float) (30 + (clickedX + 1) * square - square / 6), (float) ((clickedY + 0.5) * square), linePaint1);
            //十字的竖线
            canvas.drawLine((float) (30 + (clickedX + 0.5) * square), clickedY * square + square / 6,
                    (float) (30 + (clickedX + 0.5) * square), (clickedY + 1) * square - square / 6, linePaint1);
        }

    }

    //从调用的activity获取周期数据，并存储于本类的数组
    public void getdata(String string) {
        this.period_S = string;
        StringTokenizer tokenizer = new StringTokenizer(period_S, ",");
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 30; j++) {
                if (tokenizer.hasMoreTokens()) {//这里需要判断一下，如果没有元素了，在执行就会报错了
                    this.period[i][j] = Integer.valueOf(tokenizer.nextToken());
                }
            }
        }
        invalidate();//重绘
    }

    //自定义view点击的通用的做法。
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();
        float square = (screenWidth - 60) / 8;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (clickedX == (int) ((x - 30) / square) && clickedX != 0 && clickedY == (int) (y / square)) {
                    //传出clickedX 1是周一，7是周日  clieckedY 0是0:00  23是23:00
                    //把信息传送到调用的fragment，由fragment去处理，解耦。这个类只管图形和传出图形相关的数据。
                    clickCrossListener.onClick(clickedX, clickedY);
//                    Toast.makeText(getContext(), "clickedX = " + clickedX + "  clickedY = " + clickedY, Toast.LENGTH_SHORT).show();

                } else {
                    clickedX = (int) ((x - 30) / square);
                    clickedY = (int) (y / square);
                    invalidate();//重绘
                }
                break;
        }
//        return super.onTouchEvent(event); //究竟返回什么？？
        return true;
    }

    //定义一个接口对象
    private ClickCrossListener clickCrossListener;
    //设置接口的函数（供外部程序调用），传入了接口
    public void setClickCrossListener(ClickCrossListener l) {
        this.clickCrossListener = l;
    }
    //创建一个接口，并写一个函数原型（此原型可以用来传参）
    public interface ClickCrossListener {
//        public void onClick(int weekday, int hour);
        public void onClick(int weekday, int hour);
    }

}
