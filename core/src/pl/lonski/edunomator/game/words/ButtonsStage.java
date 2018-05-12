package pl.lonski.edunomator.game.words;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import pl.lonski.edunomator.game.GameStage;

abstract class ButtonsStage extends GameStage {

	private static final float WIDTH = 320;
	private static final float HEIGHT = 240;
	private final Skin skin;
	private float yOffset;

	ButtonsStage() {
		((OrthographicCamera) getCamera()).setToOrtho(false, WIDTH, HEIGHT);
		skin = new Skin(Gdx.files.internal("skins/visui/uiskin.json"));
		yOffset = HEIGHT * 0.95f;
	}

	void addTitle(String text) {
		Label title = new Label(text, skin);
		title.setColor(Color.YELLOW);
		title.setPosition(getCenterX(title.getWidth()), yOffset - title.getHeight());
		addActor(title);
		yOffset -= title.getHeight() * 1.4f;
	}

	private float getCenterX(float widgetWidth) {
		return (WIDTH - widgetWidth) / 2;
	}

	void addButton(String text, final Runnable runnable) {
		TextButton button = new TextButton(text, skin);
		button.setPosition(getCenterX(button.getWidth()), yOffset - button.getHeight());
		yOffset -= button.getHeight() * 1.2f;
		button.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				runnable.run();
			}
		});
		addActor(button);
	}

}

