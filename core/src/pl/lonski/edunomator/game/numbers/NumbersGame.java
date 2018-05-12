package pl.lonski.edunomator.game.numbers;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.lonski.edunomator.Speaker;
import pl.lonski.edunomator.game.Game;
import pl.lonski.edunomator.game.GameStage;

public class NumbersGame implements Game {

	private Stage stage;
	private Speaker.Provider speakerProvider;
	private Speaker speaker;
	private List<Class<? extends GameStage>> stageQueue;

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
			stage = stageClass.getConstructor(NumbersGame.class).newInstance(this);
			Gdx.input.setInputProcessor(stage);
		} catch (Exception e) {
			System.out.println("Failed to create next stage: " + e.getMessage());
			e.printStackTrace();
		}
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

