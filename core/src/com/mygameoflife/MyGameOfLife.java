package com.mygameoflife;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class MyGameOfLife extends ApplicationAdapter {
	private int windowWidth, windowHeight;
	private float aspectRatio, viewWidth, viewHeight;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture cellTexture, energyTexture;
	private ShapeRenderer shapeRenderer;

	private GameMaster gm;
	
	@Override
	public void create () {
		windowWidth = Gdx.graphics.getWidth();
		windowHeight = Gdx.graphics.getHeight();
		aspectRatio = (float) windowHeight / (float) windowWidth;
		viewWidth = 128;
		viewHeight = viewWidth * aspectRatio;

		camera = new OrthographicCamera(viewWidth, viewHeight);
		batch = new SpriteBatch();
		cellTexture = new Texture("icon_cell.jpg");
		energyTexture = new Texture("icon_energy.jpg");
		shapeRenderer = new ShapeRenderer();

		gm = new GameMaster(512);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		drawGrid();
		shapeRenderer.end();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Cell cell : gm.getColony()) {
			drawCell(cell);
			expandCamera(cell);
		}
		for (Energy stack : gm.getEnergyStacks()) {
			drawEnergy(stack);
		}
		batch.end();

		gm.nextMoveAll(true);
		gm.clearDead();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		cellTexture.dispose();
	}

	private void drawCell(Cell cell_) {
		batch.setColor(1f, 1f,1f,0.2f);
		batch.draw(
				cellTexture,
				(cell_.getPosition().x * 8f) - (cellTexture.getWidth() / 2f),
				(cell_.getPosition().y * 8f) - (cellTexture.getHeight() / 2f)
		);
	}

	private void drawEnergy(Energy stack) {
		batch.setColor(1f, 1f,1f,0.8f);
		batch.draw(
				energyTexture,
				(stack.getPosition().x * 8f) - (energyTexture.getWidth() / 2f),
				(stack.getPosition().y * 8f) - (energyTexture.getHeight() / 2f)
		);
	}

	private void expandCamera(Cell cell) {
		if (cell.getPosition().x < (-1f * viewWidth / 16f)
				|| cell.getPosition().x > (viewWidth / 16f)
				|| cell.getPosition().y < (-1f * viewHeight / 16f)
				|| cell.getPosition().y > (viewHeight / 16f)) {
			if (camera.viewportWidth < gm.MAX_GAME_SIZE.x
					&& camera.viewportHeight < gm.MAX_GAME_SIZE.y) {
				camera.viewportWidth = viewWidth + 8f;
				camera.viewportHeight = viewHeight + 8f;
				camera.update();
				viewWidth = camera.viewportWidth;
				viewHeight = camera.viewportHeight;
			}
		}
	}

	private void drawGrid() {
		shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1);
		for (int x = MathUtils.round(-1 * viewWidth / 2f); x < MathUtils.round(viewWidth / 2f); x += 8) {
			shapeRenderer.rectLine(
					new Vector2(x, MathUtils.round(-1 * viewHeight / 2f)),
					new Vector2(x, MathUtils.round(viewHeight / 2f)),
					1);
		}
		for (int y = MathUtils.round(-1 * viewHeight / 2f); y < MathUtils.round(viewHeight / 2f); y += 8) {
			shapeRenderer.rectLine(
					new Vector2(MathUtils.round(-1 * viewWidth / 2f), y),
					new Vector2(MathUtils.round(viewWidth / 2f), y),
					1
			);
		}
	}
}
