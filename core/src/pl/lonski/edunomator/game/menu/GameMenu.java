package pl.lonski.edunomator.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.lonski.edunomator.Edunomator;
import pl.lonski.edunomator.game.Game;

public class GameMenu implements Game {

	private final Edunomator edunomator;
	private Stage stage;
	private String lang;

	public GameMenu(Edunomator edunomator) {
		this.edunomator = edunomator;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public Game start(String lang) {
		setStage(new ChooseLanguageStage(this));
		return this;
	}

	public void show(String lang) {
		this.lang = lang;
		setStage(new ChooseGameStage(this));
	}

	public void start(Type type) {
		edunomator.start(type, lang);
	}

	private void setStage(Stage stage) {
		this.stage = stage;
		Gdx.input.setInputProcessor(this.stage);
	}

}
