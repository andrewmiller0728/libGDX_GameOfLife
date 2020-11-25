package com.mygameoflife;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Stack;

public class EnergyField {

    private Stack<Energy>[][] field;
    private ArrayList<Energy> entries;
    private Vector2 bounds;

    public EnergyField(Vector2 bounds_, int count) {
        this.bounds = bounds_;
        field = new Stack[(int) bounds.x * 2][(int) bounds.y * 2];
        entries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int randX = MathUtils.round(MathUtils.random(bounds.x / 8f));
            int randY = MathUtils.round(MathUtils.random(bounds.y / 8f));
            Energy newEnergy = new Energy(
                    50f,
                    new Vector2(
                            randX - (bounds.x / 16f),
                            randY - (bounds.y / 16f)
                    )
            );
            if (field[randX][randY] == null) {
                field[randX][randY] = new Stack<>();
            }
            field[randX][randY].add(newEnergy);
            entries.add(newEnergy);
        }
    }

    public Energy getEnergy(Vector2 position) {
        int adjX = (int) (position.x + (bounds.x / 2f));
        int adjY = (int) (position.y + (bounds.y / 2f));
        if (field[adjX][adjY] != null) {
            return field[adjX][adjY].pop();
        }
        return null;
    }

    public ArrayList<Energy> getEntries() {
        return entries;
    }
}