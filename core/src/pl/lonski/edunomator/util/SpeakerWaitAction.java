package pl.lonski.edunomator.util;

import com.badlogic.gdx.scenes.scene2d.Action;

public class SpeakerWaitAction extends Action {

	private float time;
	private Speaker speaker;

	public SpeakerWaitAction(Speaker speaker) {
		this.speaker = speaker;
	}

	@Override
	public boolean act(float delta) {
		time += delta;
		return time > 1 / 30f * 5 && !speaker.isSpeaking();
	}
}


