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
	private Texture cellTexture;
	private ShapeRenderer shapeRenderer;

	private ArrayList<Cell> colony;
	
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
		shapeRenderer = new ShapeRenderer();

		colony = new ArrayList<>();
		for (int i = 0; i < 512; i++) {
			colony.add(
					new Cell(
							"myCell",
							new Vector2(
									MathUtils.round(MathUtils.random(-1 * viewWidth / 16f + 1, viewWidth / 16f - 1)),
									MathUtils.round(MathUtils.random(-1 * viewWidth / 16f + 1, viewWidth / 16f - 1))
							)
					)
			);
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.2f, 0.6f, 0.6f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1);
		for (int x = MathUtils.round(-1 * viewWidth / 2f); x < MathUtils.round(viewWidth / 2f); x += 8) {
			shapeRenderer.rectLine(
					new Vector2(x, MathUtils.round(-1 * viewHeight / 2f)),
					new Vector2(x, MathUtils.round(viewHeight / 2f)),
					2);
		}
		for (int y = MathUtils.round(-1 * viewHeight / 2f); y < MathUtils.round(viewHeight / 2f); y += 8) {
			shapeRenderer.rectLine(
					new Vector2(MathUtils.round(-1 * viewWidth / 2f), y),
					new Vector2(MathUtils.round(viewWidth / 2f), y),
					2);
		}
		shapeRenderer.end();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Cell cell : colony) {
			drawCell(cell);
			switch (MathUtils.random(10)) {
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
					break;
			}
			expandCamera(cell);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		cellTexture.dispose();
	}

	private void drawCell(Cell cell_) {
		batch.draw(
				cellTexture,
				(cell_.getPosition().x * 8f) - (cellTexture.getWidth() / 2f),
				(cell_.getPosition().y * 8f) - (cellTexture.getHeight() / 2f)
		);
	}

	private void expandCamera(Cell cell) {
		if (cell.getPosition().x < (-1f * viewWidth / 16f)
				|| cell.getPosition().x > (viewWidth / 16f)
				|| cell.getPosition().y < (-1f * viewHeight / 16f)
				|| cell.getPosition().y > (viewHeight / 16f)) {
			camera.viewportWidth = viewWidth + 8f;
			camera.viewportHeight = viewHeight + 8f;
			camera.update();
			viewWidth = camera.viewportWidth;
			viewHeight = camera.viewportHeight;
		}
	}
}
