package pl.lonski.edunomator.game.numbers;

import static pl.lonski.edunomator.Edunomator.SCREEN_HEIGHT;
import static pl.lonski.edunomator.Edunomator.SCREEN_WIDTH;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.lonski.edunomator.util.RandomUtils;
import pl.lonski.edunomator.physics.WorldManager;

class BalloonStage extends Stage {

	private final NumbersGame game;
	private final NumberActor countLabel;
	private final WorldManager world;
	private final List<Balloon> balloons;

	public BalloonStage(NumbersGame game) {
		this.world = new WorldManager(new Vector2(0, 0));
		this.game = game;
		this.balloons = new ArrayList<>();

		countLabel = new NumberActor();
		countLabel.setScale(2f);
		countLabel.setAlpha(0.5f);
		addActor(countLabel);

		int numBalloons = RandomUtils.nextInt(3, 10);
		for (int i = 0; i < numBalloons; i++) {
			float x = RandomUtils.nextInt((int) (SCREEN_WIDTH * 0.3), (int) (SCREEN_WIDTH * 0.7));
			float y = RandomUtils.nextInt((int) (SCREEN_HEIGHT * 0.3), (int) (SCREEN_HEIGHT * 0.7));
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
		countLabel.setPosition(SCREEN_WIDTH / 2 - countLabel.getWidth() * countLabel.getScaleX() / 2,
				SCREEN_HEIGHT / 2 - countLabel.getHeight() * countLabel.getScaleY() / 2);
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

