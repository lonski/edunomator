package pl.lonski.edunomator;

public interface Game {

	void render();

	enum Type {
		COLORS_GAME,
		WORDS_GAME,
		NUMBERS_GAME,
	}
}
