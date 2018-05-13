package pl.lonski.edunomator.game.numbers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.lonski.edunomator.common.RandomUtils;
import pl.lonski.edunomator.common.physics.WorldManager;
import pl.lonski.edunomator.game.GameStage;

class BalloonStage extends GameStage {

	private final NumbersGame game;
	private final NumberActor countLabel;
	private final WorldManager world;
	private final List<Balloon> balloons;

	public BalloonStage(NumbersGame game) {
		this.world = new WorldManager(getScreenWidth(), getScreenHeight(), new Vector2(0, 0));
		configureViewport(1f);
		this.game = game;
		this.balloons = new ArrayList<>();

		countLabel = new NumberActor();
		countLabel.setScale(2f);
		countLabel.setAlpha(0.5f);
		addActor(countLabel);

		int numBalloons = RandomUtils.nextInt(3, 10);
		for (int i = 0; i < numBalloons; i++) {
			float x = RandomUtils.nextInt((int) (getScreenWidth() * 0.3), (int) (getScreenWidth() * 0.7));
			float y = RandomUtils.nextInt((int) (getScreenHeight() * 0.3), (int) (getScreenHeight() * 0.7));
			final Balloon balloon = new Balloon(x, y, world);
			balloon.poke();
			balloon.addListener(onBalloonTouch(balloon));
			addActor(balloon);
			balloons.add(balloon);
		}

		updateCountLabel();
	}

	private void updateCountLabel() {
		countLabel.setNumber(balloons.size());
		countLabel.setPosition(getScreenWidth() / 2 - countLabel.getWidth() * countLabel.getScaleX() / 2,
				getScreenHeight() / 2 - countLabel.getHeight() * countLabel.getScaleY() / 2);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		world.update(delta);
		if (balloons.isEmpty() && !game.getSpeaker().isSpeaking()) {
			game.nextStage();
		}
	}

	@Override
	public void draw() {
		super.draw();
	}

	private ClickListener onBalloonTouch(final Balloon balloon) {
		return new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				balloons.remove(balloon);
				speakCount();
				balloon.pop();
				updateCountLabel();
				return true;
			}
		};
	}

	private void speakCount() {
		game.getSpeaker().speak(String.valueOf(balloons.size()));
	}
}

