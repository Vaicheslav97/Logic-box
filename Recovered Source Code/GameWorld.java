package com.example.bls;

import android.graphics.Point;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class GameWorld {

    public boolean isEditMode;
    public Wire SelW = null; public int WEnd =1; public boolean wm= false;
    public  LogicElement SelEl = null;
    public Point Cpoint = new Point(0,0);

    ArrayList<Wire> wires = new <Wire> ArrayList();
    ArrayList<LogicElement> LEs = new <LogicElement> ArrayList();

    GameWorld(){}
    GameWorld(String fn){
        try {
            FileInputStream FIS = new FileInputStream(fn);
            DataInputStream DIS = new DataInputStream(FIS);

            int WS = DIS.readInt();
            for(int i=0; WS>i;i++){

                int x1=DIS.readInt();
                int y1=DIS.readInt();
                int x2=DIS.readInt();
                int y2=DIS.readInt();

                wires.add(new Wire(new Point(x1,y1),new Point(x2,y2)));
            }

            int ElS = DIS.readInt();
            for(int i=0; ElS>i;i++){
                LogicElement temp = new LogicElement(DIS.readInt(),new Point(DIS.readInt(),DIS.readInt()));
                temp.rot = DIS.readDouble();
                LEs.add(temp);
            }
            DIS.close();
            FIS.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

   public void tick(){ }

   public void save(String fn){
       try {
           FileOutputStream FOS = new FileOutputStream(fn);
           DataOutputStream DOS = new DataOutputStream(FOS);

           DOS.writeInt(wires.size());
       for(int i=0; wires.size()>i;i++){
           DOS.writeInt(wires.get(i).p1.x);
           DOS.writeInt(wires.get(i).p1.y);
           DOS.writeInt(wires.get(i).p2.x);
           DOS.writeInt(wires.get(i).p2.y);
        }

           DOS.writeInt(LEs.size());
           for(int i=0; LEs.size()>i;i++){
               DOS.writeInt(LEs.get(i).Type);
               DOS.writeInt(LEs.get(i).Pos.x);
               DOS.writeInt(LEs.get(i).Pos.y);
               DOS.writeDouble(LEs.get(i).rot);
           }

           DOS.close();
           FOS.close();

   } catch (IOException e) {e.printStackTrace();}
   
}

}
