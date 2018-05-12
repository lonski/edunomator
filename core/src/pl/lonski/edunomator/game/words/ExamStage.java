package pl.lonski.edunomator.game.words;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;

import java.util.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import pl.lonski.edunomator.game.GameStage;
import pl.lonski.edunomator.util.RandomUtils;
import pl.lonski.edunomator.util.Speaker;

public class ExamStage extends GameStage {

	private final List<Word> words;
	private final WordsGame game;
	private final Speaker speaker;
	private Question question;
	private Config.SpokenSentences sentences;

	ExamStage(List<Word> words, WordsGame game) {
		super(0.5f);
		this.words = words;
		Collections.shuffle(this.words);
		this.game = game;
		this.speaker = game.getSpeaker();
		this.sentences = game.getConfig().spokenSentences;
		nextQuestion();
	}

	private boolean nextQuestion() {

		for (Actor actor : getActors()) {
			actor.remove();
		}

		if (words.size() >= 4) {
			question = new Question(getScreenWidth(), getScreenHeight());
			String name = question.asked.getWordName();
			String text = String.format(
					(question.asked.isPlural() ? sentences.examAskPlural : sentences.examAskSingular),
					name
			);
			speaker.speakQueued(text);
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (question.guess(screenX, screenY)) {
			if (!nextQuestion()) {
				game.gameMenu();
			}
		}
		return true;
	}

	private class Question {

		private float width;
		private float height;
		private Word asked;

		Question(float w, float h) {
			this.width = w;
			this.height = h;

			List<Vector2> corners = getCorners();

			asked = words.remove(RandomUtils.nextInt(words.size()));
			putWord(asked, corners);

			for (Word word : pickThreeRandomWords()) {
				putWord(word, corners);
			}
		}

		private void putWord(Word word, List<Vector2> corners) {
			word.setLabelVisible(false);
			word.setScale(1f);
			Vector2 corner = corners.remove(RandomUtils.nextInt(corners.size()));
			word.setPosition(corner.x, corner.y);
			word.addAction(getScaleAction());
			addActor(word);
		}

		private Action getScaleAction() {
			return sequence(
					touchable(Touchable.disabled),
					scaleBy(-0.5f, -0.5f, 0.3f),
					touchable(Touchable.enabled)
			);
		}

		boolean guess(float x, float y) {
			if (x >= asked.getX() && x < (asked.getX() + width / 2) && y < height - asked.getY() &&
					y > (height / 2 - asked.getY())) {
				speaker.speak(sentences.examGuessCorrect);
				return true;
			}
			speaker.speak(sentences.examGuessIncorrect);
			return false;
		}

		private List<Word> pickThreeRandomWords() {
			List<Word> picked = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				picked.add(words.remove(RandomUtils.nextInt(words.size())));
			}
			words.addAll(picked);
			return picked;
		}

		private List<Vector2> getCorners() {
			return new ArrayList<>(Arrays.asList(
					new Vector2(0, 0),
					new Vector2(0, height / 2),
					new Vector2(width / 2, 0),
					new Vector2(width / 2, height / 2)
			));
		}
	}
}

