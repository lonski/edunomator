package pl.lonski.edunomator.game.numbers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

class NumberActor extends Actor {

	private static final TextureAtlas NUMBERS = new TextureAtlas(Gdx.files.internal("numbers/numbers.atlas"));

	private TextureRegion texture;
	private float alpha;

	NumberActor() {
		setNumber(0);
	}

	void setNumber(int i) {
		texture = NUMBERS.findRegion("number", i);
		setBounds(getX(), getY(), texture.getRegionWidth(), texture.getRegionHeight());
	}

	void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color c = batch.getColor();
		batch.setColor(c.r, c.g, c.b, alpha);
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());
		batch.setColor(c);
	}
}

