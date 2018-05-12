package pl.lonski.edunomator.game.numbers;

import static pl.lonski.edunomator.Edunomator.SCREEN_HEIGHT;
import static pl.lonski.edunomator.Edunomator.SCREEN_WIDTH;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import pl.lonski.edunomator.game.Game;
import pl.lonski.edunomator.Speaker;

public class NumbersGame implements Game {

	private Stage stage;
	private Speaker.Provider speakerProvider;
	private Speaker speaker;
	private List<Class<? extends Stage>> stageQueue;

	public NumbersGame(Speaker.Provider speakerProvider) {
		this.speakerProvider = speakerProvider;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(1 / 30f);
		stage.draw();
	}

	@Override
	public Game start(String lang) {
		speaker = speakerProvider.get(new Locale(lang));
		populateStageQueue();
		nextStage();
		return this;
	}

	Speaker getSpeaker() {
		return speaker;
	}

	void nextStage() {
		Class<? extends Stage> stageClass = stageQueue.remove(0);
		if (stageQueue.isEmpty()) {
			populateStageQueue();
			stageQueue.remove(stageClass);
		}
		try {
			launchStage(stageClass.getConstructor(NumbersGame.class).newInstance(this));
		} catch (Exception e) {
			System.out.println("Failed to create next stage: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void launchStage(Stage stage) {
		this.stage = stage;

		Viewport viewport = stage.getViewport();
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		viewport.apply();

		OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

		Gdx.input.setInputProcessor(stage);
	}

	private void populateStageQueue() {
		stageQueue = new ArrayList<>(Arrays.asList(
				BearStage.class,
				AppleStage.class,
				BalloonStage.class
		));
		Collections.shuffle(stageQueue);
	}
}

