package pl.lonski.edunomator.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.lonski.edunomator.game.Game;
import pl.lonski.edunomator.TextureActor;

class ChooseGameStage extends Stage {

	private final float screenWidth = Gdx.graphics.getWidth();
	private final float screenHeight = Gdx.graphics.getHeight();
	private final GameMenu menu;

	ChooseGameStage(final GameMenu menu) {
		this.menu = menu;

		TextureActor colorsBtn = new TextureActor(new Texture(Gdx.files.internal("game_icons/colors.png")));
		colorsBtn.setPosition(
				screenWidth / 2 - colorsBtn.getWidth() / 2,
				screenHeight / 2 - colorsBtn.getHeight() / 2
		);
		colorsBtn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				menu.start(Game.Type.COLORS_GAME);
				return true;
			}
		});
		addActor(colorsBtn);

		TextureActor numbersBtn = new TextureActor(new Texture(Gdx.files.internal("game_icons/numbers.png")));
		numbersBtn.setPosition(
				colorsBtn.getX() + colorsBtn.getWidth() * 1.2f,
				screenHeight / 2 - numbersBtn.getHeight() / 2
		);
		numbersBtn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				menu.start(Game.Type.NUMBERS_GAME);
				return true;
			}
		});
		addActor(numbersBtn);
	}
}
