package pl.lonski.edunomator.game.menu;

import static pl.lonski.edunomator.game.Game.Type.COLORS_GAME;
import static pl.lonski.edunomator.game.Game.Type.NUMBERS_GAME;
import static pl.lonski.edunomator.game.Game.Type.WORDS_GAME;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import pl.lonski.edunomator.game.Game;
import pl.lonski.edunomator.game.GameStage;
import pl.lonski.edunomator.util.TextureActor;

class ChooseGameStage extends GameStage {

	private final GameMenu menu;

	ChooseGameStage(final GameMenu menu) {
		super(0.5f);
		this.menu = menu;

		List<Actor> buttons = Arrays.asList(
				createButton("game_icons/colors.png", COLORS_GAME),
				createButton("game_icons/numbers.png", NUMBERS_GAME),
				createButton("game_icons/words.png", WORDS_GAME)
		);

		final float paddingRatio = 1.4f;
		final float margin = (getScreenWidth() - buttons.get(0).getWidth() * paddingRatio * buttons.size()) / 2;
		for (int i = 0; i < buttons.size(); i++) {
			Actor btn = buttons.get(i);
			btn.setPosition(
					margin + (i > 0 ? buttons.get(0).getWidth() * paddingRatio * i : 0),
					getScreenHeight() / 2 - btn.getHeight() / 2
			);
			addActor(btn);
		}
	}

	private Actor createButton(String iconFile, final Game.Type type) {
		TextureActor btn = new TextureActor(new Texture(Gdx.files.internal(iconFile)));
		btn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				menu.start(type);
				return true;
			}
		});
		return btn;
	}
}
