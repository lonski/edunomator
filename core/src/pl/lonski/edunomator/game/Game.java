package pl.lonski.edunomator.game;

public interface Game {

	void render();

	Game start(String lang);

	enum Type {
		GAME_MENU,
		COLORS_GAME,
		WORDS_GAME,
		NUMBERS_GAME,
	}
}
