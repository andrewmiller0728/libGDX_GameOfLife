package com.mygameoflife;

import com.badlogic.gdx.math.Vector2;

public class Energy {

    private float amount;
    private Vector2 position;

    public Energy(float initAmount, Vector2 initPosition) {
        amount = initAmount;
        position = initPosition;
    }

    public float getAmount() {
        return amount;
    }

    public void deplete(float delta) {
        amount -= delta;
    }

    public void charge(float delta) {
        amount += delta;
    }

    public float dump() {
        float temp = amount;
        amount = 0;
        return temp;
    }

    public Vector2 getPosition() {
        return position;
    }
}
