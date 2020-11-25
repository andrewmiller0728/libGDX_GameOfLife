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
    private final int LEADERBOARD_SIZE = (int) Math.pow(2, 10);
    private int initColonySize;
    private Vector2 initPosition;
    private ArrayList<Cell> colony, leaderboard;
    private EnergyField energyField;

    /*
    Constructor
     */
    public GameMaster(int count) {
        initColonySize = count;
        colony = new ArrayList<>();
        initPosition = INIT_POSITION.cpy();
        for (int i = 0; i < initColonySize; i++) {
            colony.add(new Cell(i, initPosition.cpy()));
        }
        energyField = new EnergyField(MAX_GAME_SIZE, 4096);
        leaderboard = new ArrayList<>();

    }


    /*
    Cell Management
     */
    public void update() {
        clearDead();
        updateCellData();
        nextMoveAll(false);
        if (colony.isEmpty()) {
            if (leaderboard.isEmpty()) {
                generateCells(initColonySize);
            }
            else {
                for (Cell leaderCell : leaderboard) {
                    generateChildren(leaderCell, 2);
                }
            }
        }
    }

    public void updateCellData() {
        for (int i = 0; i < colony.size(); i++) {
            colony.get(i).setEnergyNeighborhood(getNeighborhood(colony.get(i)));
            colony.get(i).incrementAge();
            if (leaderboard.size() < LEADERBOARD_SIZE) {
                leaderboard.add(colony.get(i));
            }
            else {
                for (int j = 0; j < leaderboard.size(); j++) {
                    if (getScore(colony.get(i)) > getScore(leaderboard.get(j))) {
                        leaderboard.set(j, colony.get(i));
                    }
                }
            }
        }
    }

    private double getScore(Cell cell) {
        return (Math.abs(cell.getPosition().x) * 0.00)
                + (Math.abs(cell.getPosition().y) * 0.00)
                + (cell.getAge() * 1.00)
                + (cell.getEnergy().getAmount() * 1.00)
                - (cell.getTurnsSinceMove() * 10.00);
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
            Vector2 pos = cell.getPosition().cpy().add(offsets[i].cpy());
            Energy detect = energyField.getEnergy(pos);
            if (detect != null) {
                neighborhood[i] = detect;
            }
        }
        return neighborhood;
    }

    public GameMaster clearDead() {
        for (int i = 0; i < colony.size(); i++) {
            if (colony.get(i).getEnergy().getAmount() <= 0
                    || colony.get(i).getPosition().x > MAX_GAME_SIZE.x
                    || colony.get(i).getPosition().x < -1f * MAX_GAME_SIZE.x
                    || colony.get(i).getPosition().y < -1f * MAX_GAME_SIZE.y
                    || colony.get(i).getPosition().y > MAX_GAME_SIZE.y) {
                colony.remove(i);
            }
        }
        return this;
    }

    public GameMaster generateCells(int count) {
        for (int i = 0; i < count; i++) {
            colony.add(new Cell(1, INIT_POSITION.cpy()));
        }
        return this;
    }

    public GameMaster generateChildren(Cell parent, int count) {
        for (int i = 0; i < count; i++) {
            colony.add(parent.divide());
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
        nextMoveHelper(MathUtils.random(25), cell);
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
//            case 5:
//                colony.add(cell.divide());
//                break;
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

    public EnergyField getEnergyField() {
        return energyField;
    }

    public void setInitPosition(Vector2 initPosition) {
        this.initPosition = initPosition;
    }
}
