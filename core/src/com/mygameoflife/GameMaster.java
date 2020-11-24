package com.mygameoflife;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class GameMaster {

    /*
    Variables
     */
    private final Vector2 INIT_POSITION = new Vector2(0f, 0f);
    private Vector2 initPosition;
    private ArrayList<Cell> colony;


    /*
    Constructor
     */
    public GameMaster(int count) {
        colony = new ArrayList<>();
        initPosition = INIT_POSITION;
        for (int i = 0; i < count; i++) {
            colony.add(new Cell(i, initPosition.cpy()));
        }
    }


    /*
    Cell Management
     */
    public GameMaster clearDead() {
        for (int i = 0; i < colony.size(); i++) {
            if (colony.get(i).getEnergy() < 0) {
                colony.remove(i);
            }
        }
        return this;
    }

    public GameMaster generateCells(int count) {
        for (int i = 0; i < count; i++) {
            colony.add(new Cell(i, initPosition.cpy()));
        }
        return this;
    }


    /*
    Getters and Setters
     */
    public ArrayList<Cell> getColony() {
        return colony;
    }

    public void setInitPosition(Vector2 initPosition) {
        this.initPosition = initPosition;
    }
}
