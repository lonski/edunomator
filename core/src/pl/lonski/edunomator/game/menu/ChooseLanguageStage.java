package pl.lonski.edunomator.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import pl.lonski.edunomator.game.GameStage;
import pl.lonski.edunomator.util.TextureActor;

class ChooseLanguageStage extends GameStage {

	private final GameMenu menu;

	ChooseLanguageStage(GameMenu menu) {
		super(0.5f);
		this.menu = menu;

		TextureActor pl = createFlag("pl");
		pl.setX(getScreenWidth() / 4 - pl.getWidth() / 2);
		addActor(pl);

		TextureActor en = createFlag("en");
		en.setX(getScreenWidth() * 0.75f - en.getWidth() / 2);
		addActor(en);
	}

	private TextureActor createFlag(final String country) {
		TextureActor flag = new TextureActor(new Texture(Gdx.files.internal("flag/" + country + ".png")));
		flag.setPosition(getScreenWidth() / 2 - flag.getWidth() / 2, getScreenHeight() / 2 - flag.getHeight() / 2);
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

