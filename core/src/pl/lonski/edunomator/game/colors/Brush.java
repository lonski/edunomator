package pl.lonski.edunomator.game.colors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Brush extends Actor {

	private final int id;
	private final String name;
	private final TextureRegion texture;
	private float originalX;
	private float originalY;

	Brush(int id, String name, TextureRegion texture) {
		this.id = id;
		this.name = name;
		this.texture = texture;
		setBounds(0, 0, texture.getRegionWidth(), texture.getRegionHeight());
	}

	int getId() {
		return id;
	}

	String getSpokenName() {
		return name;
	}

	void setOriginalPosition(float x, float y) {
		originalX = x;
		originalY = y;
	}

	void moveToOriginalPosition() {
		addAction(moveTo(originalX, originalY, 0.2f));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture,
				getX(), getY(),
				getOriginX() + getWidth() / 2, getOriginY() + getHeight() / 2,
				getWidth(), getHeight(),
				getScaleX(), getScaleY(), getRotation());
	}
}

