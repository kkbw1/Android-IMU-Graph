package com.kkb.imugraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GraphView extends View {

    float pX;
    float pY;

    float mX;
    float mYR;
    float mYG;
    float mYB;

    Paint mainPaint = new Paint();
    Paint drawPaint = new Paint();

    Path drawPathR = new Path();
    Path drawPathG = new Path();
    Path drawPathB = new Path();

    int w;
    int h;

    float x0;
    float x1;

    float yR;
    float yG;
    float yB;

    float xTxt;

    //********************************************************************************************//
    //                                                                                            //
    //                                   Class Constructors                                       //
    //                                                                                            //
    //********************************************************************************************//
    public GraphView(Context context)
    {
        super(context);
    }

    public GraphView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    //********************************************************************************************//
    //                                                                                            //
    //                                   Overriden Methods                                        //
    //                                                                                            //
    //********************************************************************************************//
    @Override
    protected void onDraw(Canvas canvas) {
        w = this.getWidth();
        h = this.getHeight();

        x0 = (float)(w * 0.9);
        x1 = (float)(x0 + w * 0.05);

        yR = (float)(h * 0.05);
        yG = (float)(h * 0.1);
        yB = (float)(h * 0.15);

        xTxt = (float)(x1 + w*0.005);

        mainPaint.setColor(Color.BLACK);
        mainPaint.setStrokeWidth(2);
        canvas.drawLine(0, h/2, w, h/2, mainPaint);

        mainPaint.setColor(Color.RED);
        mainPaint.setStrokeWidth(3);
        canvas.drawLine(x0, yR, x1, yR, mainPaint);
        mainPaint.setColor(Color.GREEN);
        canvas.drawLine(x0, yG, x1, yG, mainPaint);
        mainPaint.setColor(Color.BLUE);
        canvas.drawLine(x0, yB, x1, yB, mainPaint);

        mainPaint.setColor(Color.BLACK);
        mainPaint.setTextSize(30);
//		canvas.drawText(Float.toString(mX) + "&" + Float.toString(mY), 10, 30, mainPaint);

        canvas.drawText("X", xTxt, (float) (h * 0.06), mainPaint);
        canvas.drawText("Y", xTxt, (float) (h * 0.11), mainPaint);
        canvas.drawText("Z", xTxt, (float) (h * 0.16), mainPaint);

        drawPaint.setColor(Color.RED);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeWidth(3);
        canvas.drawPath(drawPathR, drawPaint);

        drawPaint.setColor(Color.GREEN);
        canvas.drawPath(drawPathG, drawPaint);

        drawPaint.setColor(Color.BLUE);
        canvas.drawPath(drawPathB, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if(me.getAction() == MotionEvent.ACTION_DOWN)
        {
            return true;
        }
        else if(me.getAction() == MotionEvent.ACTION_MOVE)
        {
//			changeValue(me.getY());

            return true;
        }
        else if(me.getAction() == MotionEvent.ACTION_UP)
        {
            return true;
        }
        return false;
    }

    //********************************************************************************************//
    //                                                                                            //
    //                               User Defined Sub-routines                                    //
    //                                                                                            //
    //********************************************************************************************//
    public void startGraph() {
        drawPathR.reset();
        drawPathG.reset();
        drawPathB.reset();

        pX = 0;
        pY = this.getHeight() / 2;

        drawPathR.moveTo(pX, pY);
        drawPathG.moveTo(pX, pY);
        drawPathB.moveTo(pX, pY);
    }

    public void changeValue(float newYR, float newYG, float newYB) {
        mX++;

        mYR = newYR;
        mYG = newYG;
        mYB = newYB;

        drawPathR.lineTo(mX, mYR);
        drawPathG.lineTo(mX, mYG);
        drawPathB.lineTo(mX, mYB);
        this.invalidate();

        if(mX >= this.getWidth())
        {
            mX = 0;

            drawPathR.reset();
            drawPathG.reset();
            drawPathB.reset();

            drawPathR.moveTo(pX, pY);
            drawPathG.moveTo(pX, pY);
            drawPathB.moveTo(pX, pY);
        }
    }

    public void resetGraph() {
        mX = 0;

        drawPathR.reset();
        drawPathG.reset();
        drawPathB.reset();
        this.invalidate();
    }
}
