package pl.lonski.edunomator.game.words;

import java.util.List;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import pl.lonski.edunomator.Edunomator;
import pl.lonski.edunomator.common.Speaker;
import pl.lonski.edunomator.game.Game;
import pl.lonski.edunomator.game.GameStage;

public class WordsGame implements Game {

	private static BitmapFont font;

	private final Edunomator edunomator;
	private final Speaker.Provider speakerProvider;
	private Speaker speaker;
	private GameStage stage;
	private Config config;

	public WordsGame(Edunomator edunomator) {
		this.speakerProvider = edunomator.getSpeakerProvider();
		this.edunomator = edunomator;
	}

	public static BitmapFont getFont() {
		if (font == null) {
			font = new BitmapFont(Gdx.files.internal("font/noto.fnt"));
		}
		return font;
	}

	Speaker getSpeaker() {
		return speaker;
	}

	void learnWords(Config.Dataset dataset) {
		setStage(new WordLearnStage(dataset, this));
	}

	void startExam(List<Word> words) {
		setStage(new ExamStage(words, this));
	}

	void gameMenu() {
		setStage(new MenuStage(this));
	}

	private void setStage(GameStage stage) {
		this.stage = stage;
		edunomator.setInputListener(stage.getInputAdapter());
	}

	Config getConfig() {
		return config;
	}

	void setConfig(String filename) {
		this.config = Config.load(Gdx.files.internal(filename));
		this.speaker = speakerProvider.get(new Locale(config.language));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(1 / 60f);
		stage.draw();
	}

	@Override
	public Game start(String lang) {
		setConfig("words/config/" + lang + ".json");
		gameMenu();
		return this;
	}
}

