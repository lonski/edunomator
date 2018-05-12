package pl.lonski.edunomator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextureActor extends Actor {

	private TextureRegion texture;

	public TextureActor(Texture texture) {
		this(new TextureRegion(texture));
	}

	public TextureActor(TextureRegion texture) {
		this.texture = texture;
		setBounds(0, 0, texture.getRegionWidth(), texture.getRegionHeight());
		setOrigin(getWidth() / 2, getHeight() / 2);
	}

	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
				getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());
	}

}

