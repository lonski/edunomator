package pl.lonski.edunomator;

import static pl.lonski.edunomator.game.Game.Type.GAME_MENU;

import com.badlogic.gdx.*;

import pl.lonski.edunomator.common.Speaker;
import pl.lonski.edunomator.game.Game;
import pl.lonski.edunomator.game.colors.ColorsGame;
import pl.lonski.edunomator.game.menu.GameMenu;
import pl.lonski.edunomator.game.menu.Hud;
import pl.lonski.edunomator.game.numbers.NumbersGame;
import pl.lonski.edunomator.game.words.WordsGame;

public class Edunomator extends ApplicationAdapter {

	private final Speaker.Provider speakerProvider;
	private Game game;
	private Hud hud;

	Edunomator(Speaker.Provider speakerProvider) {
		this.speakerProvider = speakerProvider;
	}

	public Speaker.Provider getSpeakerProvider() {
		return speakerProvider;
	}

	public void start(Game.Type gameType, String lang) {
		switch (gameType) {
		case GAME_MENU:
			game = new GameMenu(this).start(lang);
			break;
		case COLORS_GAME:
			game = new ColorsGame(this).start(lang);
			break;
		case NUMBERS_GAME:
			game = new NumbersGame(this).start(lang);
			break;
		case WORDS_GAME:
			game = new WordsGame(this).start(lang);
			break;
		default:
		}
	}

	public void setInputListener(InputProcessor processor) {
		if (hud != null) {
			Gdx.input.setInputProcessor(new InputMultiplexer(hud, processor));
		} else {
			Gdx.input.setInputProcessor(processor);
		}
	}

	@Override
	public void create() {
		start(GAME_MENU, "");
		hud = new Hud(this);
	}

	@Override
	public void render() {
		game.render();
		hud.act();
		hud.draw();
	}
}
