package pl.lonski.edunomator.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class GameStage extends Stage {

	private static final float BASE_SCREEN_WIDTH = 1280 * 2;
	private static final float BASE_SCREEN_HEIGHT = 720 * 2;

	private float screenWidth;
	private float screenHeight;

	public GameStage() {
		this(1f);
	}

	public GameStage(float screenScale) {
		configureViewport(screenScale);
	}

	public float getScreenWidth() {
		return screenWidth;
	}

	public float getScreenHeight() {
		return screenHeight;
	}

	public InputAdapter getInputAdapter() {
		return this;
	}

	protected void configureViewport(float screenScale) {
		Viewport viewport = getViewport();
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		viewport.apply();

		OrthographicCamera camera = (OrthographicCamera) getCamera();
		camera.setToOrtho(false, BASE_SCREEN_WIDTH * screenScale, BASE_SCREEN_HEIGHT * screenScale);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

		this.screenWidth = getCamera().viewportWidth;
		this.screenHeight = getCamera().viewportHeight;
	}
}
