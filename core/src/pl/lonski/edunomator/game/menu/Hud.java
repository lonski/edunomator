package pl.lonski.edunomator.game.menu;

import static pl.lonski.edunomator.game.Game.Type.GAME_MENU;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import pl.lonski.edunomator.Edunomator;
import pl.lonski.edunomator.game.GameStage;
import pl.lonski.edunomator.util.RectangleActor;

public class Hud extends GameStage {

	public Hud(Edunomator edunomator) {
		new GameMenuButton(this, edunomator);
	}

	private static class GameMenuButton extends RectangleActor {

		GameMenuButton(GameStage stage, final Edunomator edunomator) {
			super(getSize(stage), getSize(stage), Color.RED);
			setAlpha(0.5f);
			setPosition(stage.getScreenWidth() - getWidth() * 1.2f, stage.getScreenHeight() - getHeight() * 1.2f);
			stage.addActor(this);
			addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					edunomator.start(GAME_MENU, "");
					return true;
				}
			});
		}

		private static int getSize(GameStage stage) {
			return (int) (stage.getScreenHeight() / 12);
		}
	}
}
