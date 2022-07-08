package com.example.bls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class GameView extends View {
public GameWorld GW;
public int Selection = 0;

Paint mPaint;
boolean RotMode = false; double SRot =0; double SElRot =0;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs); // TODO Auto-generated constructor stub
    }


    double Radius(Point p, double X, double Y){
        double x = X-p.x;
        double y  = Y-p.y;
        double c = Math.sqrt(x*x+y*y);
        return c;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mPaint==null){
            mPaint=new Paint();
        }

        canvas.drawColor(Color.BLACK);

        for(int i = 0;i<GW.LEs.size() ;i++){
            GW.LEs.get(i).Draw(canvas);
        }
        for(int i = 0;i<GW.wires.size() ;i++){
            GW.wires.get(i).Draw(canvas);
        }
    if(Selection==0||Selection==1) {
        mPaint.setColor(Color.LTGRAY);
        if (GW.SelW != null && GW.WEnd == 1) {
            canvas.drawRect(GW.SelW.p1.x-30,GW.SelW.p1.y+30,GW.SelW.p1.x+30,GW.SelW.p1.y+90,mPaint);
        }
        if (GW.SelW != null && GW.WEnd == 2) {
            canvas.drawRect(GW.SelW.p2.x-30,GW.SelW.p2.y+30,GW.SelW.p2.x+30,GW.SelW.p2.y+90,mPaint);
        }
    }
        invalidate ();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!GW.isEditMode)return true;
        // координаты Touch-события
        double evX = event.getX();
        double evY = event.getY();
        boolean runq = true;


        switch (event.getAction()) {
            // касание началось
            case MotionEvent.ACTION_DOWN:

                if( Selection==0){

                    for(int i = 0;i<GW.wires.size() ;i++){
                        if( Radius(GW.wires.get(i).p1,evX,evY)<=30){
                            GW.SelW=GW.wires.get(i); GW.WEnd =1; runq=false;
                            if(GW.SelEl!=null) {GW.SelEl.Selected = false; GW.SelEl=null;}
                            break;}else
                        if( Radius(GW.wires.get(i).p2,evX,evY)<=30){
                            GW.SelW=GW.wires.get(i); GW.WEnd =2; runq=false;
                            if(GW.SelEl!=null) {GW.SelEl.Selected = false; GW.SelEl=null;}
                            break;}
                    }

                    if (GW.SelW != null && GW.WEnd == 1) {
                        GW.Cpoint.set(GW.SelW.p1.x,GW.SelW.p1.y);
                        GW.Cpoint.offset(0,60);
                        if( Radius(GW.Cpoint,evX,evY)<=35){
                            GW.wm= true; runq=false;
                        }
                    }else if (GW.SelW != null && GW.WEnd == 2) {
                        GW.Cpoint.set(GW.SelW.p2.x,GW.SelW.p2.y);
                        GW.Cpoint.offset(0,60);
                        if( Radius(GW.Cpoint,evX,evY)<=35){
                            GW.wm= true; runq=false;
                        }
                    }else{GW.wm= false;}

                    if(runq!=false){GW.SelW=null;}


                    for(int i = 0;runq&&i<GW.LEs.size() ;i++){
                       Point p = GW.LEs.get(i).Pos;
                       if( Radius(p,evX,evY)<=35){
                           if(GW.SelEl!=null) {GW.SelEl.Selected = false; GW.SelEl=null;}
                           GW.SelEl=GW.LEs.get(i); GW.SelEl.Selected = true;  RotMode=false;
                           break;}else
                       if(GW.SelEl!=null) {GW.SelEl.Selected = false; GW.SelEl=null;}
                    }
                    //for(GW.wires)
                }else if(Selection==1){
                    if(GW.SelW!=null) {
                        if (GW.WEnd == 1) {
                            GW.Cpoint.set(GW.SelW.p1.x,GW.SelW.p1.y);
                            GW.Cpoint.offset(0,60);
                            if( Radius(GW.Cpoint,evX,evY)<=35){
                                GW.wm= true; runq=false;
                            }else{GW.WEnd=2;}
                        }else if (GW.WEnd == 2) {
                            GW.Cpoint.set(GW.SelW.p2.x,GW.SelW.p2.y);
                            GW.Cpoint.offset(0,60);
                            if( Radius(GW.Cpoint,evX,evY)<=35){
                                GW.wm= true; runq=false;
                            }else{GW.wm= false; GW.SelW=null;}
                        }

                    }else{
                        GW.SelW = new Wire(new Point((int) evX, (int) evY - 100));
                        GW.WEnd = 1;
                        GW.wires.add(GW.SelW);
                    }

                }
                    else if(Selection==2){
                    for(int i = 0;i<GW.wires.size() ;i++){
                        if( Radius(GW.wires.get(i).p1,evX,evY)<=30||Radius(GW.wires.get(i).p2,evX,evY)<=30){
                            GW.wires.remove(i); runq=false;
                            break;}
                    }

                    for(int i = 0;runq&&i<GW.LEs.size() ;i++) {
                        Point p = GW.LEs.get(i).Pos;
                        if (Radius(p, evX, evY) <= 35) {
                            GW.LEs.remove(i);
                            break;
                        }
                    }

                }else if(Selection>2){
                        if(GW.SelEl!=null){
                            Point p = GW.SelEl.Pos;
                            if(! (Radius(p,evX,evY)<=35)){
                                GW.SelEl.Selected = false;
                                GW.SelEl=null;
                            }
                            }else{
                            GW.SelEl = new LogicElement((Selection - 5), new Point((int) evX, (int) evY));
                            GW.SelEl.Selected = true;
                            GW.LEs.add(GW.SelEl);
                        }
                }

                break;
            // тащим
            case MotionEvent.ACTION_MOVE:
                if(Selection==0){

                    if(GW.SelW!=null&&GW.WEnd==1&&GW.wm){
                        GW.SelW.p1.set((int)evX,(int)evY-100);
                    }if(GW.SelW!=null&&GW.WEnd==2&&GW.wm){
                        GW.SelW.p2.set((int)evX,(int)evY-100);
                    }

                    if(GW.SelEl!=null&&RotMode){
                       double ERot = Math.toDegrees(Math.atan2(evX,evY)) ;
                        GW.SelEl.rot = SElRot +((SRot-ERot)*10);
                        //SRot=ERot;
                    }else if(GW.SelEl!=null){
                        GW.SelEl.Pos.set((int)evX,(int)evY);
                    }


                }else if (Selection==1){
                    if(GW.SelW!=null&&GW.WEnd==1&&GW.wm){
                        GW.SelW.p1.set((int)evX,(int)evY-100);
                    }if(GW.SelW!=null&&GW.WEnd==2&&GW.wm){
                        GW.SelW.p2.set((int)evX,(int)evY-100);
                    }


                }else if(Selection>2){
                    if(GW.SelEl!=null) {
                        GW.SelEl.Pos.set((int) evX, (int) evY);
                    }
                }

                break;

            // касание завершено
            case MotionEvent.ACTION_UP:
                                // выключаем режим перетаскивания

                break;
        }
        return true;
    }
}

