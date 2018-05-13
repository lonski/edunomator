package pl.lonski.edunomator.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class RectangleActor extends Actor {

	private final TextureRegion texture;
	private float alpha;

	public RectangleActor(int width, int height, Color color) {
		texture = new TextureRegion(createTexture(width, height, color));
		setBounds(0, 0, width, height);
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color c = batch.getColor();
		batch.setColor(c.r, c.g, c.b, alpha);
		batch.draw(
				texture,
				getX(), getY(),
				getOriginX(), getOriginY(),
				getWidth(), getHeight(),
				getScaleX(),
				getScaleY(),
				getRotation()
		);
		batch.setColor(c);
	}

	private Texture createTexture(int width, int height, Color color) {
		Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fillRectangle(0, 0, width, height);
		Texture texture = new Texture(pixmap);
		pixmap.dispose();
		return texture;
	}
}

