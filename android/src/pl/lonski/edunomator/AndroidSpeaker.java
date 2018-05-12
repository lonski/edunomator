package pl.lonski.edunomator;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class AndroidSpeaker implements Speaker, TextToSpeech.OnInitListener {

	private final TextToSpeech tts;
	private Locale locale;
	private boolean isInitialized;
	private String lastSpoken;

	AndroidSpeaker(Context context, Locale locale) {
		this.locale = locale;
		this.tts = new TextToSpeech(context, this);
	}

	boolean isInitialized() {
		return isInitialized;
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int result = tts.setLanguage(locale);
			isInitialized = result == 0;
			if (!isInitialized) {
				Log.e("TTS", "Failed to set language " + locale + " status = " + result);
			}
		} else {
			Log.e("TTS", "Initilization Failed!");
			isInitialized = false;
		}
	}

	@Override
	public void speak(String text) {
		speak(text, TextToSpeech.QUEUE_FLUSH);
	}

	@Override
	public void speakQueued(String text) {
		speak(text, TextToSpeech.QUEUE_ADD);
	}

	@Override
	public boolean isSpeaking() {
		return tts.isSpeaking();
	}

	private void speak(String text, int queueMode) {
		if (!tts.isSpeaking() || !text.equals(lastSpoken)) {
			tts.speak(text, queueMode, null);
			lastSpoken = text;
		}
	}
}

