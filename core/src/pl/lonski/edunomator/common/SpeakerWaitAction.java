package pl.lonski.edunomator.common;

import com.badlogic.gdx.scenes.scene2d.Action;

public class SpeakerWaitAction extends Action {

	private float time;
	private pl.lonski.edunomator.common.Speaker speaker;

	public SpeakerWaitAction(pl.lonski.edunomator.common.Speaker speaker) {
		this.speaker = speaker;
	}

	@Override
	public boolean act(float delta) {
		time += delta;
		return time > 1 / 30f * 5 && !speaker.isSpeaking();
	}
}


