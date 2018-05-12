package pl.lonski.edunomator.game.numbers;

import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody;
import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody;
import static pl.lonski.edunomator.physics.WorldManager.PIXELS_TO_METERS;
import static pl.lonski.edunomator.util.TextureUtils.readCoords;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import pl.lonski.edunomator.Speaker;
import pl.lonski.edunomator.TextureActor;
import pl.lonski.edunomator.game.GameStage;
import pl.lonski.edunomator.physics.WorldManager;
import pl.lonski.edunomator.util.RandomUtils;

class AppleStage extends GameStage {

	private static final List<Vector2> APPLE_COORDS = readAppleCoords();

	private Tree tree;
	private Basket basket;
	private List<Apple> apples;
	private List<Apple> applesBasket;
	private WorldManager worldManager;
	private NumberActor countLabel;
	private Speaker speaker;
	private NumbersGame game;

	public AppleStage(NumbersGame game) {
		this.game = game;
		worldManager = new WorldManager(getScreenWidth(), getScreenHeight());
		configureViewport(1f);
		speaker = game.getSpeaker();

		tree = new Tree();
		tree.setPosition(tree.getWidth() * 0.1f, 0);
		addActor(tree);

		basket = new Basket();
		basket.setPosition(getScreenWidth() - basket.getWidth(), 0);
		basket.createBody(worldManager.getWorld());
		addActor(basket);

		spawnApples();

		countLabel = new NumberActor();
		countLabel.setAlpha(0.5f);
		countLabel.setNumber(applesBasket.size());
		countLabel.setPosition(basket.getX() + basket.getWidth() / 2 - countLabel.getWidth() / 2,
				basket.getHeight() * 1.1f);
		addActor(countLabel);
	}

	private void handleAppleInBasket() {
		for (Apple apple : apples) {
			if (!Gdx.input.isTouched() && basket.collides(apple) &&
					!applesBasket.contains(apple)) {
				onBasketAdd(apple);
			} else if (!basket.collides(apple) && applesBasket.contains(apple)) {
				onBasketRemove(apple);
			}
		}
	}

	private void onBasketAdd(Apple apple) {
//		apple.setTouchable(Touchable.disabled);
		applesBasket.add(apple);
		countLabel.setNumber(applesBasket.size());
		speaker.speak(String.valueOf(applesBasket.size()));
		handleGameEnd();
	}

	private void onBasketRemove(Apple apple) {
//		apple.setTouchable(Touchable.enabled);
		applesBasket.remove(apple);
		countLabel.setNumber(applesBasket.size());
		speaker.speak(String.valueOf(applesBasket.size()));
	}

	private void handleGameEnd() {
		if (applesBasket.size() == apples.size()) {
			final String text = apples.size() > 4 ? " jabłek w koszyku" : " jabłka w koszyku";
			addAction(Actions.sequence(
					new Action() {
						@Override
						public boolean act(float delta) {
							speaker.speak(String.valueOf(apples.size()) + text);
//							worldManager.setFreezed(true);
							return true;
						}
					},
					new Action() {
						float time;

						@Override
						public boolean act(float delta) {
							time += delta;
							if (time > 1 / 30f * 5 && !speaker.isSpeaking()) {
								game.nextStage();
								return true;
							}
							return false;
						}
					}
			));
		}
	}

	@Override
	public void draw() {
		super.draw();
//		worldManager.debugDraw();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		worldManager.update(delta);
		handleAppleInBasket();
	}

	private void spawnApples() {
		this.apples = new ArrayList<>();
		this.applesBasket = new ArrayList<>();
		int appleCount = RandomUtils.nextInt(3, 10);
		Collections.shuffle(APPLE_COORDS);
		for (int i = 0; i < appleCount; i++) {
			Vector2 appleCoord = APPLE_COORDS.get(i);
			Apple apple = new Apple(worldManager);
			float x = tree.getX() + appleCoord.x - apple.getWidth() / 2;
			float y = tree.getY() + (tree.getHeight() - appleCoord.y) - apple.getHeight() / 2;
			apple.setPosition(x, y);
			addActor(apple);
			apples.add(apple);
		}
	}

	private static List<Vector2> readAppleCoords() {
		return readCoords("numbers/apples/apple_coords.txt", "numbers/apples/tree.png");
	}

	static class Apple extends TextureActor {

		private Body body;
		boolean isDragging;

		Apple(final WorldManager wm) {
			super(new TextureRegion(new Texture(Gdx.files.internal("numbers/apples/apple.png"))));
			addListener(new DragListener() {

				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					createBody(wm.getWorld());
					isDragging = true;
					return super.touchDown(event, x, y, pointer, button);
				}

				@Override
				public void touchDragged(InputEvent event, float x, float y, int pointer) {
					setPosition(getX() + x - getWidth() / 2, getY() + y - getHeight() / 2);
					setRotation(0);
					body.setAwake(false);
					body.setTransform((getX() + getWidth() / 2) / PIXELS_TO_METERS,
							(getY() + getHeight() / 2) / PIXELS_TO_METERS, 0);
				}

				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					isDragging = false;
					body.setAwake(true);
					super.touchUp(event, x, y, pointer, button);
				}
			});
		}

		@Override
		public void act(float delta) {
			if (body != null && !isDragging) {
				setPosition(body.getPosition().x * PIXELS_TO_METERS - getWidth() / 2,
						body.getPosition().y * PIXELS_TO_METERS - getHeight() / 2);
				setRotation((float) Math.toDegrees(body.getAngle()));
			}
			super.act(delta);
		}

		private void createBody(World world) {
			if (body == null) {
				BodyDef bodyDef = new BodyDef();
				bodyDef.type = DynamicBody;
				bodyDef.position.set(getX() / PIXELS_TO_METERS, getY() / PIXELS_TO_METERS);

				FixtureDef fixtureDef = new FixtureDef();
				CircleShape shape = new CircleShape();
				shape.setRadius(Math.min(getWidth(), getHeight()) / PIXELS_TO_METERS / 2);
				fixtureDef.shape = shape;
				fixtureDef.friction = 0.3f;
				fixtureDef.density = 1;

				body = world.createBody(bodyDef);
				body.createFixture(fixtureDef);
				shape.dispose();
			}
		}
	}

	static class Tree extends TextureActor {
		Tree() {
			super(new TextureRegion(new Texture(Gdx.files.internal("numbers/apples/tree.png"))));
		}
	}

	static class Basket extends TextureActor {

		private static final List<Vector2> VERTICES = readBasketVertices();
		private Polygon polygonBounds;

		Basket() {
			super(new TextureRegion(new Texture(Gdx.files.internal("numbers/apples/basket.png"))));
		}

		boolean collides(Apple apple) {
			if (polygonBounds == null) {
				return false;
			}
			Rectangle r = apple.getBounds();
			Polygon rPoly = new Polygon(new float[]{
					0, 0, r.width, 0, r.width, r.height, 0, r.height
			});
			rPoly.setPosition(r.x, r.y);
			return Intersector.overlapConvexPolygons(rPoly, polygonBounds);
		}

		void createBody(World world) {
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = StaticBody;
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

			Body body = world.createBody(bodyDef);
			body.createFixture(fixtureDef);
			shape.dispose();

			polygonBounds = createPolygonBounds();
		}

		private Polygon createPolygonBounds() {
			float[] floats = new float[VERTICES.size() * 2 + 2];
			for (int i = 0; i < VERTICES.size(); i++) {
				floats[i * 2] = getX() + VERTICES.get(i).x;
				floats[i * 2 + 1] = getY() + getHeight() - VERTICES.get(i).y + 40;
			}
			floats[floats.length - 2] = getX() + getWidth() / 2;
			floats[floats.length - 1] = getHeight() * 0.7f;
			return new Polygon(floats);
		}

		private static List<Vector2> readBasketVertices() {
			return readCoords("numbers/apples/basket_vertices.txt", "numbers/apples/basket.png");
		}
	}
}

