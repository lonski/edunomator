package pl.lonski.edunomator.game.colors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

class Figure extends Actor {

	private final TextureRegion texture;
	private final TextureRegion textureColor;
	private final int brushId;
	private final String name;
	private boolean isColored;

	Figure(int brushId, String name, TextureRegion texture, TextureRegion textureColor) {
		this.texture = texture;
		this.textureColor = textureColor;
		this.isColored = false;
		this.brushId = brushId;
		this.name = name;
		setBounds(0, 0, texture.getRegionWidth(), texture.getRegionHeight());
	}

	int getBrushId() {
		return brushId;
	}

	String getSpokenName() {
		return name;
	}

	void setColored(boolean colored) {
		isColored = colored;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color old = batch.getColor();
		batch.setColor(getColor());
		batch.draw(isColored ? textureColor : texture,
				getX(), getY(),
				getOriginX() + getWidth() / 2, getOriginY() + getHeight() / 2,
				getWidth(), getHeight(),
				getScaleX(), getScaleY(), getRotation());
		batch.setColor(old);
	}

	public boolean isColored() {
		return isColored;
	}
}

