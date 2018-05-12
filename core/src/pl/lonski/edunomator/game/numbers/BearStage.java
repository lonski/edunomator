package pl.lonski.edunomator.game.numbers;

import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody;
import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.KinematicBody;
import static pl.lonski.edunomator.physics.WorldManager.PIXELS_TO_METERS;
import static pl.lonski.edunomator.util.TextureUtils.readCoords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import pl.lonski.edunomator.game.GameStage;
import pl.lonski.edunomator.physics.BodyUserData;
import pl.lonski.edunomator.physics.WorldManager;
import pl.lonski.edunomator.util.RandomUtils;
import pl.lonski.edunomator.util.Speaker;
import pl.lonski.edunomator.util.TextureActor;

public class BearStage extends GameStage {

	private final NumbersGame game;
	private final WorldManager worldManager;
	private final NumberActor countLabel;
	private final Speaker speaker;
	private final Bowl bowl;
	private List<Bear> bears;
	private List<Bear> bearsBowl;
	private Bear lastBear;
	private int spawnedBears;
	private int minBearsInBowl;
	private boolean isGameEnded;

	public BearStage(NumbersGame game) {
		this.game = game;
		this.worldManager = new WorldManager(getScreenWidth(), getScreenHeight(), new Vector2(0f, -1f), false);
		configureViewport(1f);
		this.bears = new ArrayList<>();
		this.bearsBowl = new ArrayList<>();
		this.speaker = game.getSpeaker();
		this.minBearsInBowl = RandomUtils.nextInt(3, 6);

		countLabel = new NumberActor();
		countLabel.setScale(2f);
		countLabel.setAlpha(0.5f);
		updateCountLabel();
		addActor(countLabel);

		bowl = new Bowl();
		bowl.createBody(worldManager.getWorld());

		bowl.addListener(new DragListener() {

			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				float newX = bowl.getX() + x - getWidth() / 2;
				newX = Math.max(0, newX);
				newX = Math.min(newX, getScreenWidth() - getWidth());
				float diff = (newX - bowl.getX()) / getScreenWidth();
				bowl.body.setLinearVelocity(diff * PIXELS_TO_METERS, 0);

				if (bowl.getX() < 0) {
					bowl.body.setLinearVelocity(Math.max(bowl.body.getLinearVelocity().x, 0), 0);
				} else if (bowl.getX() > getScreenWidth() - getWidth()) {
					bowl.body.setLinearVelocity(Math.min(bowl.body.getLinearVelocity().x, 0), 0);
				}

				for (Bear bear : bears) {
					if (bear.getBounds().overlaps(bowl.getBounds())) {
						Body bearBody = bear.getBody();
						bearBody.setLinearVelocity(bowl.body.getLinearVelocity().x, bearBody.getLinearVelocity().y);
					}
				}
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				bowl.body.setLinearVelocity(0, 0);
				for (Bear bear : bears) {
					if (bear.getBounds().overlaps(bowl.getBounds())) {
						bear.getBody().setLinearVelocity(0, bear.getBody().getLinearVelocity().y);
					}
				}
				super.touchUp(event, x, y, pointer, button);
			}
		});
		addActor(bowl);
		bowl.setZIndex(10);
	}

	private void updateCountLabel() {
		countLabel.setNumber(bearsBowl.size());
		countLabel.setPosition(getScreenWidth() / 2 - countLabel.getWidth() * countLabel.getScaleX() / 2,
				getScreenHeight() / 2 - countLabel.getHeight() * countLabel.getScaleY() / 2);
	}

	private void spawnBear() {
		Bear bear = new Bear();
		bear.setPosition(
				RandomUtils.nextInt((int) bear.getWidth(), (int) (getScreenWidth() - bear.getWidth())),
				getScreenHeight() + bear.getHeight()
		);
		bear.setRotation(RandomUtils.nextInt(360));
		bear.createBody(worldManager.getWorld());
		addActor(bear);
		bears.add(bear);
		bear.setZIndex(0);
		spawnedBears++;
		lastBear = bear;
	}


	private void handleBearInBowl() {
		for (Bear bear : bears) {
			if (bear.getBounds().overlaps(bowl.getBounds()) &&
					!bearsBowl.contains(bear)) {
				onBowlAdd(bear);
			} else if (!bear.getBounds().overlaps(bowl.getBounds()) && bearsBowl.contains(bear)) {
				onBowlRemove(bear);
			}
		}
	}

	private void onBowlAdd(Bear bear) {
		bearsBowl.add(bear);
		updateCountLabel();
		speaker.speak(String.valueOf(bearsBowl.size()));
	}

	private void onBowlRemove(Bear bear) {
		bearsBowl.remove(bear);
		updateCountLabel();
		speaker.speak(String.valueOf(bearsBowl.size()));
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		worldManager.update(delta);
		if (!isGameEnded) {
			handleBearInBowl();
			if (spawnedBears < 10 || bearsBowl.size() < minBearsInBowl) {
				if (lastBear == null || bearsBowl.contains(lastBear)) {
					spawnBear();
				}
			} else if (lastBear == null || bearsBowl.contains(lastBear)) {
				endGame();
			}
		}
		if (lastBear != null && lastBear.getY() < -(lastBear.getHeight() * 1.1f)) {
			bears.remove(lastBear);
			lastBear.removeActor();
			lastBear = null;
		}
	}

	private void endGame() {
		isGameEnded = true;
		bowl.setTouchable(Touchable.disabled);
		final String text =
				"złapałeś " + String.valueOf(bearsBowl.size()) +
						(bearsBowl.size() > 4 ? " żelkowych misiów " : " żelkowe misie ") + "do miski.";
		addAction(Actions.sequence(
				new Action() {
					@Override
					public boolean act(float delta) {
						speaker.speak(text);
						return true;
					}
				},
				new Action() {
					float actionTime;

					@Override
					public boolean act(float delta) {
						actionTime += delta;
						if (actionTime > 1 / 30f * 5 && !speaker.isSpeaking()) {
							game.nextStage();
							return true;
						}
						return false;
					}
				}
		));
	}

	@Override
	public void draw() {
		super.draw();
//		worldManager.debugDraw();
	}

	static class Bowl extends TextureActor {

		private static final List<Vector2> VERTICES = readVertices();
		private Body body;

		Bowl() {
			super(new Texture(Gdx.files.internal("numbers/bears/bowl.png")));
		}

		@Override
		public void act(float delta) {
			if (body != null) {
				setPosition(body.getPosition().x * PIXELS_TO_METERS, getY());
				setRotation((float) Math.toDegrees(body.getAngle()));
			}
			super.act(delta);
		}

		private static List<Vector2> readVertices() {
			return readCoords("numbers/bears/bowl_vertices.txt", "numbers/bears/bowl.png");
		}

		private void createBody(World world) {
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = KinematicBody;
			bodyDef.position.set(getX() / PIXELS_TO_METERS, getY() / PIXELS_TO_METERS);

			FixtureDef fixtureDef = new FixtureDef();

			List<Vector2> mappedVertices = new ArrayList<>();
			for (Vector2 vertex : VERTICES) {
				mappedVertices.add(new Vector2(
						vertex.x / PIXELS_TO_METERS,
						(getHeight() - vertex.y) / PIXELS_TO_METERS)
				);
			}
			ChainShape shape = new ChainShape();
			shape.createChain(mappedVertices.toArray(new Vector2[]{}));
			fixtureDef.shape = shape;
			fixtureDef.friction = 0.5f;
			fixtureDef.density = 1;

			body = world.createBody(bodyDef);
			body.createFixture(fixtureDef);
			shape.dispose();
		}

	}

	static class Bear extends TextureActor {

		private Body body;

		Bear() {
			super(getRandomTexture());
		}

		private static Texture getRandomTexture() {
			List<String> files = Arrays.asList(
					"numbers/bears/bear_red.png",
					"numbers/bears/bear_orange.png",
					"numbers/bears/bear_yellow.png",
					"numbers/bears/bear_green.png"
			);
			return new Texture(Gdx.files.internal(files.get(RandomUtils.nextInt(files.size()))));
		}

		void removeActor() {
			addAction(Actions.sequence(new Action() {
				@Override
				public boolean act(float delta) {
					getBody().setUserData(new BodyUserData().setDeleteFlag(true));
					return true;
				}
			}, Actions.removeActor()));
		}

		void createBody(World world) {
			if (body == null) {
				BodyDef bodyDef = new BodyDef();
				bodyDef.type = DynamicBody;
				bodyDef.position.set(getX() / PIXELS_TO_METERS, getY() / PIXELS_TO_METERS);
				bodyDef.angle = (float) Math.toRadians(getRotation());

				FixtureDef fixtureDef = new FixtureDef();
				PolygonShape shape = new PolygonShape();
				shape.setAsBox(getWidth() / 2 / PIXELS_TO_METERS, getHeight() / 2 / PIXELS_TO_METERS);
				fixtureDef.shape = shape;
				fixtureDef.friction = 0.7f;
				fixtureDef.restitution = 0.2f;
				fixtureDef.density = 0.5f;

				body = world.createBody(bodyDef);
				body.createFixture(fixtureDef);
				shape.dispose();
			}
		}

		@Override
		public void act(float delta) {
			if (body != null) {
				setPosition(body.getPosition().x * PIXELS_TO_METERS - getWidth() / 2,
						body.getPosition().y * PIXELS_TO_METERS - getHeight() / 2);
				setRotation((float) Math.toDegrees(body.getAngle()));
			}
			super.act(delta);
		}

		public Body getBody() {
			return body;
		}
	}
}

