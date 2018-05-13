package pl.lonski.edunomator.game.words;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import pl.lonski.edunomator.common.actor.Text;

public class Word extends Actor {

	private final FileHandle textureFile;
	private Texture texture;
	private Text label;
	private final String name;
	private boolean drawLabel;
	private boolean isPlural;

	Word(FileHandle file, String name, boolean isPlural, float width, float height) {
		setWidth(width);
		setHeight(height);
		this.textureFile = file;
		this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
		this.drawLabel = true;
		this.isPlural = isPlural;
		setPosition(getWidth(), 0);
	}

	void load() {
		if (texture == null) {
			texture = new Texture(textureFile);
			label = new Text(name, Color.RED, WordsGame.getFont());
		}
	}

	String getWordName() {
		return name;
	}

	boolean isPlural() {
		return isPlural;
	}

	void setLabelVisible(boolean visible) {
		drawLabel = visible;
	}

	@Override
	public void draw(Batch batch, float alpha) {
		if (texture != null) {
			batch.draw(texture, getX(), getY(), getWidth() * getScaleX(),
					getHeight() * getScaleY());
			if (drawLabel) {
				label.draw(batch, getX(), getY(), getScaleX(), getScaleY());
			}
		}
	}
}

