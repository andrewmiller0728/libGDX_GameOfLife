package com.mygameoflife;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class GameMaster {

    /*
    Variables
     */
    public final Vector2 MAX_GAME_SIZE = new Vector2(800f, 800f);
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
    public void update() {
        clearDead();
        updateEnergyNeighborhoods();
        nextMoveAll(false);
        ageCells();
    }

    public void updateEnergyNeighborhoods() {
        for (Cell cell : colony) {
            cell.setEnergyNeighborhood(getNeighborhood(cell));
        }
    }

    /*
    offsets:
        0 1 2
        3 4 5
        6 7 8
     */
    private Energy[] getNeighborhood(Cell cell) {
        Energy[] neighborhood = new Energy[9];
        Vector2[] offsets = {
                new Vector2(-1f, 1f),
                new Vector2(0f, 1f),
                new Vector2(1f, 1f),
                new Vector2(-1f, 0f),
                new Vector2(0f, 0f),
                new Vector2(1f, 0f),
                new Vector2(-1f, -1f),
                new Vector2(0f, -1f),
                new Vector2(1f, -1f)
        };
        for (int i = 0; i < offsets.length; i++) {
            for (Energy stack : energyStacks) {
                if (stack.getPosition().equals(
                        cell.getPosition().cpy().add(offsets[i])
                )) {
                    neighborhood[i] = stack;
                }
            }
        }
        return neighborhood;
    }

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

    public GameMaster clearOutofBounds() {
        for (int i = 0; i < colony.size(); i++) {
            Cell currCell = colony.get(i);
            if (currCell.getPosition().x > MAX_GAME_SIZE.x
                    || currCell.getPosition().x < -1f * MAX_GAME_SIZE.x
                    || currCell.getPosition().y < -1f * MAX_GAME_SIZE.y
                    || currCell.getPosition().y > MAX_GAME_SIZE.y) {
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

    public GameMaster ageCells() {
        for (Cell cell : colony) {
            cell.incrementAge();
        }
        return this;
    }

    private GameMaster consumeEnergyStack(Cell cell, Energy stack) {
        if (cell.getPosition().equals(stack.getPosition())) {
            cell.gainEnergy(stack.dump());
        }
        return this;
    }
    
    public GameMaster nextMoveAll() {
        return nextMoveAll(false);
    }

    public GameMaster nextMoveAll(boolean random) {
        for (int i = 0; i < colony.size(); i++) {
            if (random) {
                randomNextMove(colony.get(i));
            }
            else {
                chooseNextMove(colony.get(i));
            }
        }
        return this;
    }

    private GameMaster chooseNextMove(Cell cell) {
        nextMoveHelper(cell.getOutputChoice(), cell);
        return this;
    }

    private GameMaster randomNextMove(Cell cell) {
        nextMoveHelper(MathUtils.random(50), cell);
        return this;
    }

    private void nextMoveHelper(int choice, Cell cell) {
        switch (choice) {
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
                if (cell.getEnergyNeighborhood()[4] != null) {
                    consumeEnergyStack(
                            cell,
                            cell.getEnergyNeighborhood()[4]
                    );
                }
                break;
            case 5:
                colony.add(cell.divide());
                break;
            default:
                cell.sleep();
                break;
        }
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
