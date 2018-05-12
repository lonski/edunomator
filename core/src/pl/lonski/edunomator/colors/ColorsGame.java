package pl.lonski.edunomator.colors;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;

import pl.lonski.edunomator.Game;
import pl.lonski.edunomator.Speaker;

public class ColorsGame implements Game {

	private Stage stage;
	private Speaker.Provider speakerProvider;
	private Speaker speaker;
	private List<Brush> brushes;
	private List<Figure> figures;

	public ColorsGame(Speaker.Provider speakerProvider) {
		this.speakerProvider = speakerProvider;
	}

	List<Brush> getBrushes() {
		return brushes;
	}

	Speaker getSpeaker() {
		return speaker;
	}

	public Game start(String lang) {
		Config config = new Json().fromJson(Config.class,
				Gdx.files.internal("colors/config/" + lang + ".json").readString());

		brushes = new ArrayList<>();
		for (Config.BrushDef def : config.brushes) {
			TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("colors/" + def.textureFile)));
			brushes.add(new Brush(def.id, def.spokenName, texture));
		}

		figures = new ArrayList<>();
		for (Config.FigureDef def : config.figures) {
			TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("colors/" + def.textureFile)));
			TextureRegion textureColor =
					new TextureRegion(new Texture(Gdx.files.internal("colors/" + def.textureFileColor)));
			figures.add(new Figure(def.brushId, def.spokenName, texture, textureColor));
		}
		Collections.shuffle(figures);

		speaker = speakerProvider.get(new Locale(config.language));
		stage = new GameStage(this);
		Gdx.input.setInputProcessor(stage);

		return this;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public List<Figure> getFigures() {
		return figures;
	}
}

