package pl.lonski.edunomator;

import com.badlogic.gdx.ApplicationAdapter;

import pl.lonski.edunomator.game.Game;
import pl.lonski.edunomator.game.colors.ColorsGame;
import pl.lonski.edunomator.game.menu.GameMenu;
import pl.lonski.edunomator.game.numbers.NumbersGame;
import pl.lonski.edunomator.game.words.WordsGame;
import pl.lonski.edunomator.util.Speaker;

public class Edunomator extends ApplicationAdapter {

	private final Speaker.Provider speakerProvider;
	private Game game;

	Edunomator(Speaker.Provider speakerProvider) {
		this.speakerProvider = speakerProvider;
	}

	public void start(Game.Type gameType, String lang) {
		switch (gameType) {
		case COLORS_GAME:
			game = new ColorsGame(speakerProvider).start(lang);
			break;
		case NUMBERS_GAME:
			game = new NumbersGame(speakerProvider).start(lang);
			break;
		case WORDS_GAME:
			game = new WordsGame(speakerProvider).start(lang);
			break;
		default:
		}
	}

	@Override
	public void create() {
		game = new GameMenu(this);
		game.start("");
	}

	@Override
	public void render() {
		game.render();
	}
}
