package pl.lonski.edunomator.game.words;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

class Config {

	static Config load(FileHandle file) {
		return new Json().fromJson(Config.class, file.readString());
	}

	Config() {
		this.spokenSentences = new SpokenSentences();
		this.datasets = new ArrayList<>();
	}

	String language;
	SpokenSentences spokenSentences;
	List<Dataset> datasets;

	static class SpokenSentences {
		String examAskSingular;
		String examAskPlural;
		String examGuessCorrect;
		String examGuessIncorrect;
		String loading;
	}

	static class Dataset {
		String name;
		String directory;
		List<WordFile> wordFiles;

		static class WordFile {
			String filename;
			String name;
			boolean isPlural;
		}
	}
}

