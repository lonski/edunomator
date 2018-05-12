package pl.lonski.edunomator.game.colors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static pl.lonski.edunomator.game.colors.PlayStage.FigureAction.*;

import java.security.SecureRandom;
import java.util.*;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.SnapshotArray;

import pl.lonski.edunomator.Speaker;
import pl.lonski.edunomator.game.GameStage;

class PlayStage extends GameStage {

	private static final float ANIMATION_SPEED = 0.3f;
	private final List<Integer> brushIds;
	private final Random random;
	private Figure figure;
	private int figureIdx;
	private ColorsGame game;

	PlayStage(ColorsGame game) {
		super(0.5f);
		this.game = game;
		this.random = new SecureRandom();
		this.brushIds = new ArrayList<>();
		this.figureIdx = -1;
		for (Brush brush : game.getBrushes()) {
			brushIds.add(brush.getId());
		}
		next();
	}

	private void next() {
		if (figure != null && !figure.isColored()) {
			return;
		}

		for (Actor actor : new SnapshotArray<Actor>(getActors())) {
			actor.remove();
		}
		figureIdx++;
		if (figureIdx == game.getFigures().size()) {
			Collections.shuffle(game.getFigures());
			figureIdx = 0;
		}
		figure = createFigure();
		addActor(figure);
		for (Brush brush : createBrushes(figure.getBrushId())) {
			addActor(brush);
		}
	}

	private Figure createFigure() {
		Figure figure = game.getFigures().get(figureIdx);
		float x = (getScreenWidth() - figure.getWidth() + game.getBrushes().get(0).getWidth() * 2) / 2;
		float y = (getScreenHeight() - figure.getHeight()) / 2;
		figure.setPosition(x, -figure.getHeight());
		figure.addAction(moveTo(x, y, ANIMATION_SPEED * 2));
		figure.setColored(false);
		return figure;
	}

	private Brush findBrush(int id) {
		for (Brush brush : game.getBrushes()) {
			if (brush.getId() == id) {
				return brush;
			}
		}
		throw new RuntimeException("Brush id " + id + " is missing!");
	}

	private List<Brush> pickBrushSet(int figureBrushId) {
		List<Brush> brushes = new ArrayList<>();
		List<Integer> ids = new ArrayList<>(brushIds);
		brushes.add(findBrush(figureBrushId));
		ids.remove(Integer.valueOf(figureBrushId));
		for (int i = 0; i < 2; i++) {
			int index = random.nextInt(ids.size());
			int id = ids.get(index);
			ids.remove(index);
			brushes.add(findBrush(id));
		}
		return brushes;
	}

	private List<Brush> createBrushes(final int figureBrushId) {
		List<Brush> brushes = pickBrushSet(figureBrushId);
		Collections.shuffle(brushes);
		final float spacing = getScreenHeight() / 10;
		final float margin = (getScreenHeight()
				- spacing * Math.max(0, brushes.size() - 1)
				- brushes.get(0).getHeight() * brushes.size()) / 2;
		for (int i = 0; i < brushes.size(); i++) {
			final Brush brush = brushes.get(i);
			brush.setScale(0.01f);
			brush.setVisible(false);
			brush.setPosition(brush.getWidth() / 2, margin + i * (brush.getHeight() + spacing));
			brush.setOriginalPosition(brush.getX(), brush.getY());
			brush.addAction(sequence(delay(ANIMATION_SPEED * i), show(), scaleTo(1, 1, ANIMATION_SPEED)));
			brush.addListener(new DragListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					game.getSpeaker().speak(brush.getSpokenName());
					return super.touchDown(event, x, y, pointer, button);
				}

				public void drag(InputEvent event, float x, float y, int pointer) {
					brush.moveBy(x - brush.getWidth() / 2, y - brush.getHeight() / 2);
				}

				public void dragStop(InputEvent event, float x, float y, int pointer) {
					if (isPainting(brush) && figure.getBrushId() == brush.getId()) {
						brush.remove();
						figure.addAction(sequence(
								fadeOut(ANIMATION_SPEED),
								colorFigure(figure),
								fadeIn(ANIMATION_SPEED),
								speakFigure(game.getSpeaker(), figure),
								waitForSpeaker(game.getSpeaker()),
								moveTo(getScreenWidth(), figure.getY(), ANIMATION_SPEED * 2),
								nextFigure(PlayStage.this)
						));
					} else {
						brush.moveToOriginalPosition();
					}
				}
			});
		}
		return brushes;
	}

	private boolean isPainting(Brush brush) {
		return overlaps(brush, figure);
	}

	private boolean overlaps(Actor a1, Actor a2) {
		Rectangle r1 = new Rectangle(a1.getX(), a1.getY(), a1.getWidth(), a1.getHeight());
		Rectangle r2 = new Rectangle(a2.getX(), a2.getY(), a2.getWidth(), a2.getHeight());
		return r1.overlaps(r2);
	}

	static class FigureAction {
		static Action speakFigure(final Speaker speaker, final Figure figure) {
			return new Action() {
				@Override
				public boolean act(float delta) {
					speaker.speak(figure.getSpokenName());
					return true;
				}
			};
		}

		static Action colorFigure(final Figure figure) {
			return new Action() {
				@Override
				public boolean act(float delta) {
					figure.setColored(true);
					return true;
				}
			};
		}

		static Action waitForSpeaker(final Speaker speaker) {
			return new Action() {
				@Override
				public boolean act(float delta) {
					return !speaker.isSpeaking();
				}
			};
		}

		static Action nextFigure(final PlayStage playStage) {
			return new Action() {
				@Override
				public boolean act(float delta) {
					playStage.next();
					return true;
				}
			};
		}
	}
}

