package test.lisong.elastic.utils;

import java.util.Random;

public class RandomUtils {

	private static Random r = new Random();
	
	public static int randomInt() {
		return r.nextInt();
	}
	
	/**
	 * 范围 [min:max)
	 * @author 李嵩
	 * @param min
	 * @param max
	 * @return
	 * @date Jun 30, 2016
	 */
	public static int randomRange(int min, int max) {
		return min + r.nextInt(max);
	}
	
	/**
	 * 范围 [0:bound)
	 * @author 李嵩
	 * @param bound
	 * @return
	 * @date Jun 30, 2016
	 */
	public static int randomBound(int bound) {
		return r.nextInt(bound);
	}
}
