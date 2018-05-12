package pl.lonski.edunomator.game.words;

import java.util.List;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import pl.lonski.edunomator.game.Game;
import pl.lonski.edunomator.game.GameStage;
import pl.lonski.edunomator.util.Speaker;

public class WordsGame implements Game {

	private static BitmapFont font;

	private final Speaker.Provider speakerProvider;
	private Speaker speaker;
	private GameStage stage;
	private Config config;

	public WordsGame(Speaker.Provider speakerProvider) {
		this.speakerProvider = speakerProvider;
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
		stage = new WordLearnStage(dataset, this);
		Gdx.input.setInputProcessor(stage.getInputAdapter());
	}

	void startExam(List<Word> words) {
		stage = new ExamStage(words, this);
		Gdx.input.setInputProcessor(stage.getInputAdapter());
	}

	void gameMenu() {
		stage = new MenuStage(this);
		Gdx.input.setInputProcessor(stage.getInputAdapter());
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

