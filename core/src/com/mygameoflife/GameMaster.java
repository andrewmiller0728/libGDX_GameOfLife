package com.mygameoflife;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class GameMaster {

    /*
    Variables
     */
    public final Vector2 MAX_GAME_SIZE = new Vector2(512f, 512f);
    private final Vector2 INIT_POSITION = new Vector2(0f, 0f);
    private Vector2 initPosition;
    private ArrayList<Cell> colony;
    private ArrayList<Energy> energyStacks;


    /*
    Constructor
     */
    public GameMaster(int count) {
        colony = new ArrayList<>();
        initPosition = INIT_POSITION.cpy();
        for (int i = 0; i < count; i++) {
            colony.add(new Cell(i, initPosition.cpy()));
        }
        energyStacks = new ArrayList<>();
        for (int i = 0; i < 1024; i++) {
            energyStacks.add(
                    new Energy(
                            25,
                            new Vector2(
                                    MathUtils.round(MathUtils.random(
                                            -1f * MAX_GAME_SIZE.x / 16f,
                                            MAX_GAME_SIZE.x / 16f
                                    )),
                                    MathUtils.round(MathUtils.random(
                                            -1f * MAX_GAME_SIZE.y / 16f,
                                            MAX_GAME_SIZE.y / 16f
                                    ))
                            )
                    )
            );
        }
    }


    /*
    Cell Management
     */
    public GameMaster clearDead() {
        for (int i = 0; i < colony.size(); i++) {
            if (colony.get(i).getEnergy().getAmount() <= 0) {
                colony.remove(i);
            }
        }
        for (int i = 0; i < energyStacks.size(); i++) {
            if (energyStacks.get(i).getAmount() <= 0) {
                energyStacks.remove(i);
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
    
    public GameMaster nextMoveAll() {
        return nextMoveAll(false);
    }

    public GameMaster nextMoveAll(boolean random) {
        for (Cell cell : colony) {
            if (random) {
                randomNextMove(cell);
            }
            else {
                chooseNextMove(cell);
            }
        }
        return this;
    }

    public GameMaster chooseNextMove(Cell cell) {
        switch (cell.getOutputChoice()) {
            case 0:
                cell.movePosY();
                break;
            case 1:
                cell.movePosX();
                break;
            case 2:
                cell.moveNegY();
                break;
            case 3:
                cell.moveNegX();
                break;
            case 4:
                cell.sleep();
                break;
            default:
                break;
        }
        return this;
    }

    public GameMaster randomNextMove(Cell cell) {
        switch (MathUtils.random(50)) {
            case 0:
                cell.movePosY();
                break;
            case 1:
                cell.movePosX();
                break;
            case 2:
                cell.moveNegY();
                break;
            case 3:
                cell.moveNegX();
                break;
            case 4:
            default:
                cell.sleep();
                break;
        }
        return this;
    }


    /*
    Getters and Setters
     */
    public ArrayList<Cell> getColony() {
        return colony;
    }

    public ArrayList<Energy> getEnergyStacks() {
        return energyStacks;
    }

    public void setInitPosition(Vector2 initPosition) {
        this.initPosition = initPosition;
    }
}
