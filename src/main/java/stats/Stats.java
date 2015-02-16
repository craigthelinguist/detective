package src.main.java.stats;

import java.util.Arrays;

public class Stats {

	// Constructor.
	// --------------------------------------------------------------------------------
	private Stats() {}
	
	
	// Static methods.
	// --------------------------------------------------------------------------------
	
	/**
	 * Calculate the mean of an array of doubles.
	 * @param dubs: an array of doubles.
	 * @return mean as a double.
	 */
	public static double mean(double[] dubs) {
		double sum = 0.0;
		for (double d : dubs) sum += d;
		return sum / dubs.length;
	}

	/**
	 * Calculate the median of an array of doubles.
	 * @param dubs: an array of doubles.
	 * @return median as a double.
	 */
	public static double median (double[] dubs) {
		Arrays.sort(dubs);
		if (dubs.length % 2 == 0) {
			double mid1 = dubs[dubs.length/2];
			double mid2 = dubs[dubs.length/2 - 1];
			return (mid1 + mid2) / 2;
		}
		else {
			return dubs[dubs.length/2];
		}
	}
	
	/**
	 * Calculate the standard deviation of an array of doubles.
	 * @param dubs: an array of doubles.
	 * @return stdev as a double.
	 */
	public static double stdev (double[] dubs) {
		return Math.sqrt(Stats.variance(dubs));
	}
	
	/**
	 * Calculate the variance of an array of doubles.
	 * @param dubs: an array of doubles.
	 * @return variance as a double.
	 */
	public static double variance (double[] dubs) {
		double mean = Stats.mean(dubs);
		double[] errata = new double[dubs.length];
		for (int i = 0; i < dubs.length; i++) {
			errata[i] = Math.pow(mean - dubs[i], 2);
		}
		return Stats.mean(errata);
	}


	/**
	 * Calculate the mean of an array of doubles.
	 * @param dubs: an array of doubles.
	 * @return mean as a double.
	 */
	public static double mean(int[] dubs) {
		double sum = 0.0;
		for (double d : dubs) sum += d;
		return sum / dubs.length;
	}

	/**
	 * Calculate the median of an array of doubles.
	 * @param dubs: an array of doubles.
	 * @return median as a double.
	 */
	public static double median (int[] dubs) {
		Arrays.sort(dubs);
		if (dubs.length % 2 == 0) {
			double mid1 = dubs[dubs.length/2];
			double mid2 = dubs[dubs.length/2 - 1];
			return (mid1 + mid2) / 2;
		}
		else {
			return dubs[dubs.length/2];
		}
	}
	
	/**
	 * Calculate the standard deviation of an array of doubles.
	 * @param dubs: an array of doubles.
	 * @return stdev as a double.
	 */
	public static double stdev (int[] dubs) {
		return Math.sqrt(Stats.variance(dubs));
	}
	
	/**
	 * Calculate the variance of an array of doubles.
	 * @param dubs: an array of doubles.
	 * @return variance as a double.
	 */
	public static double variance (int[] dubs) {
		double mean = Stats.mean(dubs);
		double[] errata = new double[dubs.length];
		for (int i = 0; i < dubs.length; i++) {
			errata[i] = Math.pow(mean - dubs[i], 2);
		}
		return Stats.mean(errata);
	}



}
