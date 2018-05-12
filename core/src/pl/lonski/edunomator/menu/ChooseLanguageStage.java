package pl.lonski.edunomator.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.lonski.edunomator.TextureActor;

class ChooseLanguageStage extends Stage {

	private final GameMenu menu;
	private final float screenWidth = Gdx.graphics.getWidth();
	private final float screenHeight = Gdx.graphics.getHeight();

	ChooseLanguageStage(GameMenu menu) {
		this.menu = menu;

		TextureActor pl = createFlag("pl");
		pl.setX(screenWidth / 4 - pl.getWidth() / 2);
		addActor(pl);

		TextureActor en = createFlag("en");
		en.setX(screenWidth * 0.75f - en.getWidth() / 2);
		addActor(en);
	}

	private TextureActor createFlag(final String country) {
		TextureActor flag = new TextureActor(new Texture(Gdx.files.internal("flag/" + country + ".png")));
		flag.setPosition(screenWidth / 2 - flag.getWidth() / 2, screenHeight / 2 - flag.getHeight() / 2);
		flag.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				menu.show(country);
				return true;
			}
		});
		return flag;
	}
}

