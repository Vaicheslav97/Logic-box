package com.example.bls;

import android.graphics.Canvas;

public interface GameElement {

    public void Emulate();
    public void EventResponse();
    public void Draw(Canvas canvas);
    //public String toSave();

}
