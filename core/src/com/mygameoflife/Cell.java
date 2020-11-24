package com.mygameoflife;

import com.badlogic.gdx.math.Vector2;

public class Cell {

    /*
    Variables
     */
    private final float INIT_ENERGY = 100f;
    private final float INIT_HEAT = 100f;
    private final float MOVE_COST = 2;

    private int ID;
    private float energy;
    private float heat;
    private Vector2 position;


    /*
    Constructor
     */
    public Cell(int ID_, Vector2 initPosition) {
        this.ID = ID_;
        this.energy = INIT_ENERGY;
        this.heat = INIT_HEAT;
        this.position = initPosition;
    }


    /*
    Movement Methods
     */

    public Cell movePosX() {
        position.add(1f, 0f);
        energy -= MOVE_COST;
        return this;
    }

    public Cell moveNegX() {
        position.sub(1f, 0f);
        energy -= MOVE_COST;
        return this;
    }

    public Cell movePosY() {
        position.add(0f, 1f);
        energy -= MOVE_COST;
        return this;
    }

    public Cell moveNegY() {
        position.sub(0f, 1f);
        energy -= MOVE_COST;
        return this;
    }

    public Cell moveTo(Vector2 goal) {
        while (position.x != goal.x || position.y != goal.y) {
            if (position.x < goal.x) {
                movePosX();
            }
            else if (position.x > goal.x) {
                moveNegX();
            }
            if (position.y < goal.y) {
                movePosY();
            }
            else if (position.y > goal.y) {
                moveNegY();
            }
        }
        return this;
    }


    /*
    Homeostasis Methods
     */

    public Cell gainHeat(float amount) {
        heat += amount;
        return this;
    }

    public Cell loseHeat(float amount) {
        heat += amount;
        return this;
    }

    public Cell gainEnergy(float amount) {
        energy += amount;
        return this;
    }

    public Cell loseEnergy(float amount) {
        energy += amount;
        return this;
    }


    /*
    Getters and Setters
     */

    public int getID() {
        return ID;
    }

    public float getEnergy() {
        return energy;
    }

    public float getHeat() {
        return heat;
    }

    public Vector2 getPosition() {
        return position;
    }
}
