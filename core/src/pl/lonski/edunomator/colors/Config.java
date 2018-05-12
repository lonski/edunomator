package pl.lonski.edunomator.colors;

import java.util.List;

class Config {
	String language;
	List<BrushDef> brushes;
	List<FigureDef> figures;

	static class BrushDef {
		int id;
		String spokenName;
		String textureFile;
	}

	static class FigureDef {
		int brushId;
		String spokenName;
		String textureFile;
		String textureFileColor;
	}
}

