package pl.lonski.edunomator.util;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class TextureUtils {

	public static List<Vector2> readCoords(String verticesFile, String textureFile) {
		FileHandle verticesTxt = Gdx.files.internal(verticesFile);
		return verticesTxt.exists()
				? readCoordsFromTextFile(verticesTxt)
				: readCoordsFromTexture(textureFile);
	}

	private static List<Vector2> readCoordsFromTextFile(FileHandle verticesTxt) {
		List<Vector2> coords = new ArrayList<>();
		for (String line : verticesTxt.readString().split("\n")) {
			String[] tuple = line.split(" ");
			if (tuple.length == 2) {
				coords.add(new Vector2(Float.valueOf(tuple[0]), Float.valueOf(tuple[1])));
			}
		}
		return coords;
	}

	private static List<Vector2> readCoordsFromTexture(String textureFile) {
		List<Vector2> coords;
		Texture texture = new Texture(Gdx.files.internal(textureFile));
		texture.getTextureData().prepare();
		Pixmap pixmap = texture.getTextureData().consumePixmap();
		coords = getMarkedPixels(pixmap);
		Collections.sort(coords, new Comparator<Vector2>() {
			@Override
			public int compare(Vector2 o1, Vector2 o2) {
				return o2.x > o1.x ? 1 : -1;
			}
		});
		return coords;
	}

	private static List<Vector2> getMarkedPixels(Pixmap pixmap) {
		List<Vector2> pixels = new ArrayList<>();
		int vertexPix = pixmap.getPixel(0, 0);
		for (int y = 1; y < pixmap.getHeight(); y++) {
			for (int x = 1; x < pixmap.getWidth(); x++) {
				int pix = pixmap.getPixel(x, y);
				if (pix == vertexPix) {
					pixels.add(new Vector2(x, y));
				}
			}
		}
		return pixels;
	}
}

