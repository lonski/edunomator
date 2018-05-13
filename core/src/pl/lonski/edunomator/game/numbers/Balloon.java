package pl.lonski.edunomator.game.numbers;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static pl.lonski.edunomator.common.RandomUtils.nextBoolean;
import static pl.lonski.edunomator.common.RandomUtils.nextInt;
import static pl.lonski.edunomator.common.physics.WorldManager.PIXELS_TO_METERS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import pl.lonski.edunomator.common.physics.BodyUserData;
import pl.lonski.edunomator.common.physics.WorldManager;

class Balloon extends Actor {

	private static final TextureAtlas FRAMES = new TextureAtlas(Gdx.files.internal("numbers/balloons/balloon.atlas"));
	private static final Sound POP_SOUND = Gdx.audio.newSound(Gdx.files.internal("numbers/balloons/balloon.mp3"));

	private Animation<TextureRegion> popAnimation;
	private float stateTime;
	private Body body;

	Balloon(float x, float y, WorldManager worldManager) {
		popAnimation = new Animation<TextureRegion>(1 / 20f, FRAMES.findRegions("balloon"), Animation.PlayMode.NORMAL);
		TextureRegion firstFrame = popAnimation.getKeyFrame(0);
		setBounds(0, 0, firstFrame.getRegionWidth(), firstFrame.getRegionHeight());
		setPosition(x, y);
		setOrigin(getWidth() / 2, getHeight() / 2);
		body = createBody(worldManager.getWorld());
	}

	private Body createBody(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set((getX() - getWidth() / 2) / PIXELS_TO_METERS,
				(getY() - getHeight() / 2) / PIXELS_TO_METERS);

		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(getWidth() / 2 / PIXELS_TO_METERS, getHeight() / 2 / PIXELS_TO_METERS);
		fixtureDef.shape = shape;
		fixtureDef.restitution = 0.8f;

		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		shape.dispose();

		return body;
	}

	void poke() {
		float force = nextInt(100);
		if (nextBoolean()) {
			body.applyForceToCenter(0, force, true);
		} else {
			body.applyForceToCenter(force, 0, true);
		}
	}

	private void pop(final Runnable afterPop) {
		POP_SOUND.play();
		addAction(sequence(
				parallel(createPlayAnimationAction(), fadeOut(popAnimation.getAnimationDuration())),
				new Action() {
					@Override
					public boolean act(float delta) {
						afterPop.run();
						return true;
					}
				},
				new Action() {
					@Override
					public boolean act(float delta) {
						body.setUserData(new BodyUserData().setDeleteFlag(true));
						return true;
					}
				},
				removeActor()
		));
	}

	void pop() {
		pop(new Runnable() {
			@Override
			public void run() {
			}
		});
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (popAnimation.getAnimationDuration() >= stateTime) {
			TextureRegion currentFrame = popAnimation.getKeyFrame(stateTime, false);
			batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(),
					getScaleY(), getRotation());
		}
	}

	@Override
	public void act(float delta) {
		setPosition(body.getPosition().x * PIXELS_TO_METERS - getWidth() / 2,
				body.getPosition().y * PIXELS_TO_METERS - getHeight() / 2);
		setRotation((float) Math.toDegrees(body.getAngle()));
		if (getAcceleration() > 20) {
			poke();
		}
		super.act(delta);
	}

	private float getAcceleration() {
		return Math.max(Math.abs(Gdx.input.getAccelerometerX()),
				Math.max(Math.abs(Gdx.input.getAccelerometerY()), Math.abs(Gdx.input.getAccelerometerZ())));
	}

	private Action createPlayAnimationAction() {
		return new Action() {
			@Override
			public boolean act(float delta) {
				stateTime += delta;
				return stateTime >= popAnimation.getAnimationDuration();
			}
		};
	}
}

