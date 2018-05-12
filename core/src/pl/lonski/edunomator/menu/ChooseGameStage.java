package pl.lonski.edunomator.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.lonski.edunomator.Game;
import pl.lonski.edunomator.TextureActor;

class ChooseGameStage extends Stage {

	private final float screenWidth = Gdx.graphics.getWidth();
	private final float screenHeight = Gdx.graphics.getHeight();
	private final GameMenu menu;

	ChooseGameStage(final GameMenu menu) {
		this.menu = menu;

		TextureActor btn = new TextureActor(new Texture(Gdx.files.internal("game_icons/colors.png")));
		btn.setPosition(screenWidth / 2 - btn.getWidth() / 2, screenHeight / 2 - btn.getHeight() / 2);
		btn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				menu.start(Game.Type.COLORS_GAME);
				return true;
			}
		});
		addActor(btn);
	}
}
