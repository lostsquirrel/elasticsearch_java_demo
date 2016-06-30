package test.lisong.elastic.utils;

import java.util.Random;

public class RandomUtils {

	private static Random r = new Random();
	
	public static int randomInt() {
		return r.nextInt();
	}
}
