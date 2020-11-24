package com.mygameoflife;

import com.badlogic.gdx.math.Vector2;

public class Cell {

    /*
    Variables
     */
    private final float INIT_ENERGY = 50f;
    private final float INIT_HEAT = 100f;
    private final float MOVE_COST = 2;

    private String ID;
    private float energy;
    private float heat;
    private Vector2 position;


    /*
    Constructor
     */
    public Cell(String id, Vector2 initPosition) {
        this.ID = id;
        this.energy = INIT_ENERGY;
        this.heat = INIT_HEAT;
        this.position = initPosition;
    }


    /*
    Movement Methods
     */

    public Cell movePosX() {
        this.position.add(1f, 0f);
        energy -= MOVE_COST;
        return this;
    }

    public Cell moveNegX() {
        this.position.sub(1f, 0f);
        energy -= MOVE_COST;
        return this;
    }

    public Cell movePosY() {
        this.position.add(0f, 1f);
        energy -= MOVE_COST;
        return this;
    }

    public Cell moveNegY() {
        this.position.sub(0f, 1f);
        energy -= MOVE_COST;
        return this;
    }

    public Cell moveTo(Vector2 goal) {
        while (this.position.x != goal.x || this.position.y != goal.y) {
            if (this.position.x < goal.x) {
                this.movePosX();
            }
            else if (this.position.x > goal.x) {
                this.moveNegX();
            }
            if (this.position.y < goal.y) {
                this.movePosY();
            }
            else if (this.position.y > goal.y) {
                this.moveNegY();
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

    public String getID() {
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
