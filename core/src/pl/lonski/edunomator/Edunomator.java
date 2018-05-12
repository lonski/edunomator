package pl.lonski.edunomator;

import com.badlogic.gdx.ApplicationAdapter;

import pl.lonski.edunomator.colors.ColorsGame;
import pl.lonski.edunomator.menu.GameMenu;

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
		default:
		}
	}

	@Override
	public void create() {
		game = new GameMenu(this);
	}

	@Override
	public void render() {
		game.render();
	}
}
