package com.example.bls;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import java.util.ArrayList;

public class LogicElement implements GameElement {
    static Paint mPaint;

    static Path path;
    static Matrix matrix;
    static Path pathDst;

    boolean Selected = false;

    public int Type;
    public Point Pos;
    public double rot;
    public Wire In1, In2;
    int in1 =0 ,in2 =0;
    int OutValue =0;
    ArrayList<Wire> Outs;

   static float RoutX,RoutY;
    static float RoutX2,RoutY2;

    void Rot(float RinX, float RinY,float Teta){

        float Cos = (float) Math.cos(Math.toRadians(Teta));
        float Sin = (float) Math.sin(Math.toRadians(Teta));

         RoutX = (float) (RinX*Cos-Sin*RinY);
         RoutY = (float) (RinX*Sin+Cos*RinY);
    }



    LogicElement(int type, Point pos){
        this.Type = type;
        this.Pos= pos;
    }


    @Override
    public void Emulate() {}

    @Override
    public void EventResponse() {}





    @Override
    public void Draw(Canvas canvas) {
        if(mPaint==null||path==null) {
            mPaint = new Paint();
            mPaint.setStrokeWidth(5);
            path = new Path();
            pathDst = new Path();
            matrix = new Matrix();
            path.reset();
            path.addRect(-35,-35, +35,+35, Path.Direction.CW);

        }

        mPaint.setColor( Selected?Color.rgb(29,140,60):Color.BLUE);
        matrix.reset();     
        matrix.setRotate((float) rot, 0, 0);
        //matrix.setTranslate(Pos.x,Pos.y);
        matrix.postTranslate(Pos.x,Pos.y);
        path.transform(matrix, pathDst);
        canvas.drawPath(pathDst, mPaint);
        mPaint.setColor(Color.LTGRAY);
        mPaint.setTextSize(20); mPaint.setFakeBoldText(true);

        Rot(0, -60,(float)rot);
        RoutX2= RoutX; RoutY2=RoutY;
        Rot(0, -35,(float)rot);

        canvas.drawLine(Pos.x+RoutX,Pos.y+RoutY,Pos.x+RoutX2,(Pos.y)+RoutY2, mPaint);
        canvas.drawCircle(Pos.x+RoutX2,Pos.y+RoutY2,6,mPaint);

        if(false) {

            Rot(0, 60, (float) rot);
            RoutX2 = RoutX;
            RoutY2 = RoutY;
            Rot(0, 35, (float) rot);
            canvas.drawLine(Pos.x+RoutX,Pos.y+RoutY,Pos.x+RoutX2,(Pos.y)+RoutY2, mPaint);
            canvas.drawCircle(Pos.x+RoutX2,Pos.y+RoutY2,6,mPaint);
        }else{

            Rot(-21, 60, (float) rot);
            RoutX2 = RoutX;
            RoutY2 = RoutY;
            Rot(-21, 35, (float) rot);
            canvas.drawLine(Pos.x+RoutX,Pos.y+RoutY,Pos.x+RoutX2,(Pos.y)+RoutY2, mPaint);
            canvas.drawCircle(Pos.x+RoutX2,Pos.y+RoutY2,6,mPaint);

            Rot(21, 60, (float) rot);
            RoutX2 = RoutX;
            RoutY2 = RoutY;
            Rot(21, 35, (float) rot);
            canvas.drawLine(Pos.x+RoutX,Pos.y+RoutY,Pos.x+RoutX2,(Pos.y)+RoutY2, mPaint);
            canvas.drawCircle(Pos.x+RoutX2,Pos.y+RoutY2,6,mPaint);

        }


        switch(Type) {
         case   0:   canvas.drawText("NOT", Pos.x - 30, Pos.y + 10, mPaint); break;
         case   1:   canvas.drawText("BUF", Pos.x - 30, Pos.y + 10, mPaint); break;
         case   2:   canvas.drawText("AND", Pos.x - 30, Pos.y + 10, mPaint); break;
         case   3:   canvas.drawText("OR", Pos.x - 30, Pos.y + 10, mPaint); break;
         case   4:   canvas.drawText("XOR", Pos.x - 30, Pos.y + 10, mPaint); break;
         case   5:   canvas.drawText("NAND", Pos.x - 30, Pos.y + 10, mPaint); break;
         case   6:   canvas.drawText("NOR", Pos.x - 30, Pos.y + 10, mPaint); break;
         case   7:   canvas.drawText("NXOR", Pos.x - 30, Pos.y + 10, mPaint); break;
         default: canvas.drawText("", Pos.x - 30, Pos.y + 10, mPaint);  break;
        }
        
    }

}
