package pl.lonski.edunomator.game.words;

class MenuStage extends ButtonsStage {

	private final WordsGame game;

	MenuStage(final WordsGame game) {
		super();
		this.game = game;
		addTitle("Worducator");
		for (Config.Dataset dataset : game.getConfig().datasets) {
			addButton(dataset.name, loadWords(dataset));
		}
	}

	private Runnable loadWords(final Config.Dataset dataset) {
		return new Runnable() {
			@Override
			public void run() {
				MenuStage.this.game.learnWords(dataset);
			}
		};
	}
}

