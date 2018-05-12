package pl.lonski.edunomator.util;

import com.badlogic.gdx.input.GestureDetector;

public class SimpleDirectionGestureDetector extends GestureDetector {

	public SimpleDirectionGestureDetector(DirectionListener directionListener) {
		super(new DirectionGestureListener(directionListener));
	}

	private static class DirectionGestureListener extends GestureAdapter {
		DirectionListener directionListener;

		DirectionGestureListener(DirectionListener directionListener) {
			this.directionListener = directionListener;
		}

		@Override
		public boolean fling(float velocityX, float velocityY, int button) {
			if (Math.abs(velocityX) > Math.abs(velocityY)) {
				if (velocityX > 0) {
					directionListener.onRight();
				} else {
					directionListener.onLeft();
				}
			} else {
				if (velocityY > 0) {
					directionListener.onDown();
				} else {
					directionListener.onUp();
				}
			}
			return super.fling(velocityX, velocityY, button);
		}

	}

}

