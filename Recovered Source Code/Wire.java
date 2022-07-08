package com.example.bls;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class Wire implements GameElement {
   static Paint mPaint;



    public Point p1 ,p2;
    public Integer value = null;



    Wire(Point p1){
        this.p1=p1;
        p2= new Point(p1.x+10,p1.y+10);
    }

    Wire(Point p1,  Point p2){
        this.p1=p1;
        this.p2= p2;
    }

    Wire(Wire w){
        this.p1=p1;
        p2= new Point(p1.x+10,p1.y+10);
        value=w.value;
    }


    @Override
    public void Emulate() {value=0;}

    @Override
    public void EventResponse() {}

    @Override
    public void Draw(Canvas canvas) {
        if(mPaint==null) {
            mPaint = new Paint();
            mPaint.setStrokeWidth(5);
        }
        //if(value>0){mPaint.setColor(Color.WHITE);}else{}
        mPaint.setColor(Color.LTGRAY);
        canvas.drawLine(p1.x, p1.y,p2.x,p2.y,mPaint);
        canvas.drawCircle(p1.x, p1.y, 6,mPaint);
        canvas.drawCircle(p2.x, p2.y, 6,mPaint);
    }
