package pl.lonski.edunomator.util;

import java.util.Random;

public class RandomUtils {

	private static final Random random = new Random();

	public static int nextInt(int min, int max) {
		return random.nextInt(max - min + 1) + min;
	}

	public static int nextInt(int bound) {
		return random.nextInt(bound);
	}

	public static boolean nextBoolean() {
		return random.nextBoolean();
	}

	public static float nextFloat(float min, float max) {
		return random.nextFloat() * (max - min) + min;
	}
}

